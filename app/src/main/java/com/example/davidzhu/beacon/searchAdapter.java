package com.example.davidzhu.beacon;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by amamonau on 5/16/2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int mNumCards;


    //private int mDatasetImg = R.drawable.ic_play_dark;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        //public ImageView mImageView;

        public ViewHolder1(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.cv).findViewById(R.id.search_item_text);
            //mImageView = (ImageView) v.findViewById(R.id.search_item_img);
        }
    }


    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        //public ImageView mImageView;

        public ViewHolder2(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.cv).findViewById(R.id.search_item_text);
            //mImageView = (ImageView) v.findViewById(R.id.search_item_img);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchAdapter(int numCards) {
        mNumCards = numCards;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card_special, parent, false);
                ViewHolder1 vh = new ViewHolder1(v);
                return vh;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
                ViewHolder2 vh2 = new ViewHolder2(v);
                return vh2;
        }

        // set the view's size, margins, paddings and layout parameters
        // ...
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (position == 0) {
            ((ViewHolder1)holder).mTextView.setText("special blah");
        } else {
            ((ViewHolder2)holder).mTextView.setText("blah_booooo!");
        }

        //holder.mImageView.setImageResource(mDatasetImg);

        // R.drawable.ic_media_play

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mNumCards;
    }

    @Override
    public int getItemViewType(int position) { return position; }
}
