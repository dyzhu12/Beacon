package com.example.davidzhu.beacon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchRecycleViewAdapter extends RecyclerView.Adapter<SearchRecycleViewAdapter.SearchPlaceViewHolder> {

    int mSize;
    ArrayList<Integer> mImages;
    ArrayList<String> mNames;
    ArrayList<String> mSavedTags;
    ExpandableListAdapter mELA;

    public static class SearchPlaceViewHolder extends RecyclerView.ViewHolder
                                                implements View.OnClickListener{

        TextView itemName;
        ImageView image;
        ArrayList<String> mSavedTags;
        ExpandableListAdapter mELA;

        SearchPlaceViewHolder(View itemView, ArrayList<String> savedTags, ExpandableListAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView)itemView.findViewById(R.id.search_item_img);
            itemName = (TextView)itemView.findViewById(R.id.search_item_text);
            mSavedTags = savedTags;
            mELA = adapter;
        }


        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();

            switch (pos) {
                case 0:
                    mSavedTags.add(itemView.getResources().getString(R.string.food));
                    mELA.mCount++;
                    mELA.notifyDataSetChanged();
                    break;
                case 1:
                    mSavedTags.add(itemView.getResources().getString(R.string.free));
                    mELA.mCount++;
                    mELA.notifyDataSetChanged();
                    break;
                case 2:
                    mSavedTags.add(itemView.getResources().getString(R.string.fun));
                    mELA.mCount++;
                    mELA.notifyDataSetChanged();
                    break;
                case 3:
                    break;
            }

        }

    }

    SearchRecycleViewAdapter(ArrayList<String> itemNames, ArrayList<Integer> itemImages,
                             int size, ArrayList<String> savedTags, ExpandableListAdapter adapter){
        mNames = itemNames;
        mImages = itemImages;
        mSize = size;
        mSavedTags = savedTags;
        mELA = adapter;
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    @Override
    public SearchPlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);
        return new SearchPlaceViewHolder(v,mSavedTags,mELA);
    }

    @Override
    public void onBindViewHolder(SearchPlaceViewHolder personViewHolder, int i) {
        personViewHolder.itemName.setText(mNames.get(i));
        if (i != 3) {personViewHolder.image.setImageResource(mImages.get(i));}
    }
}
