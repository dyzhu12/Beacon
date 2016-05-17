package com.example.davidzhu.beacon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by David Zhu on 5/17/2016.
 */
public class FieldViewHolder extends RecyclerView.ViewHolder {

    private ImageView iconView;
    private TextView headerText;
    private TextView contentText;



    public FieldViewHolder(View itemView) {
        super(itemView);

        iconView = (ImageView) itemView.findViewById(R.id.view_beacon_icon);
        headerText = (TextView) itemView.findViewById(R.id.view_beacon_field_header);
        contentText = (TextView) itemView.findViewById(R.id.view_beacon_field_content);
    }

    public ImageView getIconView() {
        return iconView;
    }
    public void setIconView(ImageView label) {
        iconView = label;
    }

    public TextView getHeaderText() {
        return headerText;
    }

    public void setHeaderText(TextView label) {
        headerText = label;
    }

    public TextView getContentText() {
        return contentText;
    }

    public void setContentText(TextView label) {
        contentText = label;
    }
}
