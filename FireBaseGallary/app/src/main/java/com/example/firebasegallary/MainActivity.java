package com.example.firebasegallary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    StorageReference storageRef;
    ProgressDialog progressDialog;

    private static final String TAG = "MyActivity";
    int downloadCount = 0;
    int column = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button downloadBtn = findViewById(R.id.button);
        Button btn3x5 = findViewById(R.id.size3x5);
        Button btn2x3 = findViewById(R.id.size2x3);

        btn3x5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                column = 3;
            }
        });

        btn2x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                column = 2;
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Fetching image ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                storageRef = FirebaseStorage.getInstance().getReference("image");

                storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        int totalDownloads = listResult.getItems().size();

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        File localFile;
                        try {
                            localFile = File.createTempFile("tmpImgFile", ".jpg");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        ArrayList<ImageModel> imageModelList = new ArrayList<>();
                        for (StorageReference file : listResult.getItems()) {
                            downloadCount++;

                            FileDownloadTask downloadTask = file.getFile(localFile);
                            downloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageModelList.add(new ImageModel(bitmap));

                                    if (downloadCount == totalDownloads) {
                                        GridView gridView = findViewById(R.id.gridView);
                                        gridView.setNumColumns(column);
                                        BitmapAdapter adapter = new BitmapAdapter(MainActivity.this, imageModelList);
                                        gridView.setAdapter(adapter);
                                    }
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                        Toast.makeText(MainActivity.this, "Failed to download image !!!", Toast.LENGTH_SHORT);
                        Log.e(TAG, "Error: ", e);
                    }
                });

            }
        });
    }
}