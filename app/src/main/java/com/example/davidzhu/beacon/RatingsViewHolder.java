package com.example.davidzhu.beacon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by David Zhu on 5/17/2016.
 */
public class RatingsViewHolder extends RecyclerView.ViewHolder {

    private TextView ratingsCount;

    public RatingsViewHolder(View itemView) {
        super(itemView);

        ratingsCount = (TextView) itemView.findViewById(R.id.ratings_count);
    }

    public TextView getRatingsLabel() {
        return ratingsCount;
    }
    public void setRatingsLabel(TextView label) {
        ratingsCount = label;
    }
}
