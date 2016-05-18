package com.example.davidzhu.beacon;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Created by David Zhu on 5/17/2016.
 */
public class BeaconRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;

    private final int RATING = 0;
    private final int FIELD = 1;

    private Context context;

    public BeaconRecyclerViewAdapter(List<Object> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case RATING:
                View v1 = inflater.inflate(R.layout.layout_viewholder_ratings, viewGroup, false);
                viewHolder = new RatingsViewHolder(v1);
                break;
            case FIELD:
                View v2 = inflater.inflate(R.layout.layout_viewholder_fields, viewGroup, false);
                viewHolder = new FieldViewHolder(v2);
                break;
            default:
                break;
        }

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case RATING:
                RatingsViewHolder vh1 = (RatingsViewHolder) viewHolder;
                configureRatingsViewHolder(vh1, position);
                break;
            case FIELD:
                FieldViewHolder vh2 = (FieldViewHolder) viewHolder;
                configureFieldViewHolder(vh2, position);
                break;
            default:
                break;
        }
    }

    private void configureFieldViewHolder(FieldViewHolder vh2, int position) {
        Pair pair = (Pair) items.get(position);
        String header = (String) pair.first;
        int resourceId = R.drawable.ic_public_24dp;
        switch (header) {
            case "Date":
                resourceId = R.drawable.ic_schedule_24dp;
                break;
            case "Address":
                resourceId = R.drawable.ic_place_24dp;
                break;
            case "Phone Number":
                resourceId = R.drawable.ic_call_24dp;
                break;
            case "Website":
                resourceId = R.drawable.ic_public_24dp;
                break;
            case "Tags":
                resourceId = R.drawable.ic_loyalty_24dp;
                break;

        }

        vh2.getIconView().setImageDrawable(ContextCompat.getDrawable(context, resourceId));
        vh2.getHeaderText().setText(header);
        vh2.getContentText().setText((String) pair.second);
    }

    private void configureRatingsViewHolder(RatingsViewHolder vh1, int position) {
        int rating = (int) items.get(position);
        vh1.getRatingsLabel().setText("Saved " + rating + "x");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Integer) {
            return RATING;
        } else if (items.get(position) instanceof Pair) {
            return FIELD;
        }
        return -1;
    }
}
