package com.example.davidzhu.beacon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchRecycleViewAdapter extends RecyclerView.Adapter<SearchRecycleViewAdapter.SearchPlaceViewHolder> {


    // default lists
    int mSize;
    ArrayList<String> mDefaultTags;
    ExpandableListAdapter mELA;
    // user lists
    ArrayList<String> mUserSavedTags;
    SearchIconLoader iconLoader = new SearchIconLoader();

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
                    if (!mSavedTags.contains("Food")) {
                        mSavedTags.add(itemView.getResources().getString(R.string.food));
                        mELA.mCount++;
                        mELA.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    if (!mSavedTags.contains("Free")) {
                        mSavedTags.add(itemView.getResources().getString(R.string.free));
                        mELA.mCount++;
                        mELA.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    if (!mSavedTags.contains("Fun")) {
                        mSavedTags.add(itemView.getResources().getString(R.string.fun));
                        mELA.mCount++;
                        mELA.notifyDataSetChanged();
                    }
                    break;
                case 3:
                    break;
            }

        }

    }

    SearchRecycleViewAdapter(ArrayList<String> defaultItems, ArrayList<String> userSavedItems, ExpandableListAdapter adapter){
        mDefaultTags = defaultItems;
        mSize = mDefaultTags.size();
        mUserSavedTags = userSavedItems;
        mELA = adapter;
    }

    @Override
    public int getItemCount() {
        return mDefaultTags.size();
    }

    @Override
    public SearchPlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);
        return new SearchPlaceViewHolder(v,mUserSavedTags,mELA);
    }

    @Override
    public void onBindViewHolder(SearchPlaceViewHolder defaultItemViewHolder, int i) {
        defaultItemViewHolder.itemName.setText(mDefaultTags.get(i));
        if (i != 3) {defaultItemViewHolder.image.setImageResource(iconLoader.getIcon(mDefaultTags.get(i)));}
    }
}
