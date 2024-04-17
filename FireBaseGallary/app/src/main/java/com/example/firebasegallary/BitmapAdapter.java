package com.example.firebasegallary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BitmapAdapter extends ArrayAdapter<ImageModel> {

    public BitmapAdapter(@NonNull Context context, ArrayList<ImageModel> imageList) {
        super(context, 0, imageList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item, parent, false);
        }

        ImageModel imageModel = getItem(position);
        ImageView imageView = listitemView.findViewById(R.id.imgItem);

        assert imageModel != null;
        imageView.setImageBitmap(imageModel.getBitmap());

        return listitemView;
    }
}