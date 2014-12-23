package com.fastebro.androidrgbtool.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fastebro.androidrgbtool.R;

/**
 * Created by danielealtomare on 21/06/14.
 */
public class SelectPictureListAdapter extends RecyclerView.Adapter<SelectPictureListAdapter.ViewHolder> {
    private String[] entries;
    private ItemClickListener listener;

    public SelectPictureListAdapter(String[] entries, ItemClickListener listener) {
        this.entries = entries;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.select_picture_row, viewGroup, false);

        return new ViewHolder(itemView, new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                listener.onClick(v, position, isLongClick);
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:
                viewHolder.icon.setImageResource(R.drawable.ic_action_content_picture);
                viewHolder.title.setText(entries[0]);
                break;
            case 1:
                viewHolder.icon.setImageResource(R.drawable.ic_action_device_access_sd_storage);
                viewHolder.title.setText(entries[1]);
                break;
            case 2:
                viewHolder.icon.setImageResource(R.drawable.ic_action_device_access_camera);
                viewHolder.title.setText(entries[2]);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return entries.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView icon;
        protected TextView title;
        protected ItemClickListener listener;

        public ViewHolder(View v, ItemClickListener listener) {
            super(v);
            this.listener = listener;
            icon = (ImageView) v.findViewById(R.id.entry_icon);
            title = (TextView) v.findViewById(R.id.entry_title);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getPosition(), false);
        }
    }

    public interface ItemClickListener {
        public void onClick(View v, int position, boolean isLongClick);
    }
}
