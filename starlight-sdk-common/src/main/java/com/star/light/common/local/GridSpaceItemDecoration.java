package com.star.light.common.local;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration{
    private static final String TAG = "GridSpaceItemDecoration==>";
    private int space;//声明间距 //使用构造函数定义间距
    private int size;
    private int bottomSpace;

    public GridSpaceItemDecoration(int space) {
        this.space = space;
        this.size = 2;
        this.bottomSpace = 0;
    }

    public GridSpaceItemDecoration(int space, int size){
        this.space = space;
        this.size = size;
    }

    public GridSpaceItemDecoration(int space, int size, int bottomSpace){
        this.space = space;
        this.size = size;
        this.bottomSpace = bottomSpace;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //获得当前item的位置
        int position = parent.getChildAdapterPosition(view);
        Log.d(TAG,"POSITION=>"+position);
        //根据position确定item需要留出的位置
        if(position < size - 1){
            if(position == 0){
                //位于第一行第一个
                outRect.right = this.space;
            }else{
                outRect.left = this.space;
                outRect.right = this.space;
            }
        }else if(position == size - 1){
            outRect.left = this.space;
        }else{
            if(position % size == 0){
                //位于第一行以后的第一个
                outRect.right = this.space;
            }else if((position + 1) % size == 0){
                //位于第一行以后的最后一个
                outRect.left = this.space;
            }else{
                outRect.left = this.space;
                outRect.right = this.space;
            }
        }
        //设置底部边距
        outRect.bottom = bottomSpace;
    }
}
