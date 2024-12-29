package com.jacob.java.remote_access.client.entity.scroll;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InfiniteScrollLinearLayoutManager extends LinearLayoutManager {

    public InfiniteScrollLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * To implement infinite scrolling
     *
     * @param position Index (starting at 0) of the reference item.
     * @param offset   The distance (in pixels) between the start edge of the item view and
     *                 start edge of the RecyclerView.
     */
    @Override
    public void scrollToPositionWithOffset(int position, int offset) {
        if(getItemCount() > 0) {
            position = position % getItemCount();
        }

        super.scrollToPositionWithOffset(position, offset);
    }

}
