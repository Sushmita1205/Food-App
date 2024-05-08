package com.chicken.chlorophyll;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.chicken.chlorophyll.adapters.OnRecipeListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CircleImageView category_image;
    public TextView category_title;

    OnRecipeListener mOnRecipeListener;

    public CategoryViewHolder(@NonNull View itemView,OnRecipeListener onRecipeListener) {
        super(itemView);

        category_image=itemView.findViewById(R.id.category_image);
        category_title=itemView.findViewById(R.id.category_title);
        mOnRecipeListener=onRecipeListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mOnRecipeListener.onCategoryClick(category_title.getText().toString());
    }
}
