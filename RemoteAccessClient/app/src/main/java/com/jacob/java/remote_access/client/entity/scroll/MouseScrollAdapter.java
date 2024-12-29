package com.jacob.java.remote_access.client.entity.scroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacob.java.remote_access.client.R;

import java.util.List;

/**
 * For populating the text views in each row with some dummy text
 * The scroll is basically a RecycleView (need an adapter for its operation and display)
 */
public class MouseScrollAdapter extends RecyclerView.Adapter<MouseScrollAdapter.ViewHolder> {
    private List<String> scrollRows;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public MouseScrollAdapter(Context context, List<String> scrollRows) {
        this.inflater = LayoutInflater.from(context);
        this.scrollRows = scrollRows;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mouse_scroll_row, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Bind the data to the TextView in each row
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int dataSize = scrollRows.size();

        String dummyScrollRowData = scrollRows.get(position % dataSize);
        holder.data.setText(dummyScrollRowData);
    }

    /**
     * Controls the maximum number of items to be displayed in the scroll bar of RecyclerView
     *
     * To create infinite scrolling, we return Integer.MAX_VALUE instead of the actual size
     * We will retrieve the correct data in the caller function though (no need to worry)
     *
     * @return the maximum value of Integer
     */
    @Override
    public int getItemCount() {
//        return scrollRows.size();
        return Integer.MAX_VALUE;
    }


    /**
     * Stores and recycle views as they are scrolled off screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView data;

        ViewHolder(View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.tvScrollRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    /**
     * Get the corresponding String with the given index
     * A helper method
     *
     * @param id
     * @return
     */
    public String getItem(int id) {
        return scrollRows.get(id);
    }

    /**
     * Setter for itemClickListener
     *
     * @param itemClickListener
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
