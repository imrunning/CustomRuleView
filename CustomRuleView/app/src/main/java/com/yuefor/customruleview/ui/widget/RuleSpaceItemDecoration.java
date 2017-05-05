package com.yuefor.customruleview.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RuleSpaceItemDecoration extends RecyclerView.ItemDecoration{

    private int mSpace;

    public RuleSpaceItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace/2;
        outRect.right = mSpace/2;
        outRect.top = mSpace;
        outRect.bottom = mSpace;
    }
}