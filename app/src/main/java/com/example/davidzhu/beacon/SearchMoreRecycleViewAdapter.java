package com.example.davidzhu.beacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchMoreRecycleViewAdapter extends RecyclerView.Adapter<SearchMoreRecycleViewAdapter.SearchPlaceViewHolder> {

    Activity mSearchMoreActivity;
    // default lists
    int mSize;
    ArrayList<String> mDefaultTags;
    // user lists
    ArrayList<String> mUserSavedTags;
    SearchIconLoader iconLoader = new SearchIconLoader();

    public static class SearchPlaceViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        TextView itemName;
        ImageView image;
        Activity mActivity;
        ArrayList<String> mSavedTags;

        SearchPlaceViewHolder(View itemView, ArrayList<String> savedTags, Activity activity) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView)itemView.findViewById(R.id.search_item_img);
            itemName = (TextView)itemView.findViewById(R.id.search_item_text);
            mSavedTags = savedTags;
            mActivity = activity;
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            String selected;
            switch (pos) {
                case 0:
                    selected = itemView.getResources().getString(R.string.food);
                    if (!mSavedTags.contains(selected)) {
                        mSavedTags.add(selected);
                        //selected.concat(itemView.getResources().getString(R.string.is_added));
                        Toast.makeText(mActivity.getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    selected = itemView.getResources().getString(R.string.free);
                    if (!mSavedTags.contains(selected)) {
                        mSavedTags.add(selected);
                        //selected.concat(itemView.getResources().getString(R.string.is_added));
                        Toast.makeText(mActivity.getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    selected = itemView.getResources().getString(R.string.fun);
                    if (!mSavedTags.contains(selected)) {
                        mSavedTags.add(selected);
                        //selected.concat(itemView.getResources().getString(R.string.is_added));
                        Toast.makeText(mActivity.getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    selected = itemView.getResources().getString(R.string.arts);
                    if (!mSavedTags.contains(selected)) {
                        mSavedTags.add(selected);
                        //selected.concat(itemView.getResources().getString(R.string.is_added));
                        Toast.makeText(mActivity.getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    selected = itemView.getResources().getString(R.string.sports);
                    if (!mSavedTags.contains(selected)) {
                        mSavedTags.add(selected);
                        //selected.concat(itemView.getResources().getString(R.string.is_added));
                        Toast.makeText(mActivity.getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    selected = itemView.getResources().getString(R.string.serve);
                    if (!mSavedTags.contains(selected)) {
                        mSavedTags.add(selected);
                        //selected.concat(itemView.getResources().getString(R.string.is_added));
                        Toast.makeText(mActivity.getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    selected = itemView.getResources().getString(R.string.fabulous);
                    if (!mSavedTags.contains(selected)) {
                        mSavedTags.add(selected);
                        //selected.concat(itemView.getResources().getString(R.string.is_added));
                        Toast.makeText(mActivity.getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 7:
                    selected = itemView.getResources().getString(R.string.fire);
                    if (!mSavedTags.contains(selected)) {
                        mSavedTags.add(selected);
                        //selected.concat(itemView.getResources().getString(R.string.is_added));
                        Toast.makeText(mActivity.getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }

    }

    SearchMoreRecycleViewAdapter(ArrayList<String> defaultItems, ArrayList<String> userSavedItems, Activity activity){
        mDefaultTags = defaultItems;
        mSize = mDefaultTags.size();
        mUserSavedTags = userSavedItems;
        mSearchMoreActivity = activity;
    }

    @Override
    public int getItemCount() {
        return mDefaultTags.size();
    }

    @Override
    public SearchPlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);
        return new SearchPlaceViewHolder(v,mUserSavedTags,mSearchMoreActivity);
    }

    @Override
    public void onBindViewHolder(SearchPlaceViewHolder defaultItemViewHolder, int i) {
        defaultItemViewHolder.itemName.setText(mDefaultTags.get(i));
        defaultItemViewHolder.image.setImageResource(SearchIconLoader.getIcon(mDefaultTags.get(i)));
    }
}
