package com.chicken.chlorophyll.utils;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpacingItemDecorator extends RecyclerView.ItemDecoration {

    private final int VerticalHeight;

    public VerticalSpacingItemDecorator(int verticalHeigt) {
        VerticalHeight = verticalHeigt;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.top=this.VerticalHeight;
    }
}
