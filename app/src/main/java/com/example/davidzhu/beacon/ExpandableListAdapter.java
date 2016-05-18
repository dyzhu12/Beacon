package com.example.davidzhu.beacon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity mContext;
    private String mDataHeader; // header titles
    // child data in format of header title, child title
    private List<String> mListDataChild;
    public int mCount;

    public ExpandableListAdapter(Activity context, String listDataHeader, List<String> listChildData) {
        mContext = context;
        mDataHeader = listDataHeader;
        mListDataChild = listChildData;
        mCount =  mListDataChild.size();
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListDataChild.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return 1;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 1;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.search_exp_list_header, null);
        }

        ImageView v = (ImageView) convertView.findViewById(R.id.search_exp_hdr_arrow);
        if (isExpanded) {
            v.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        } else {
            v.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        }

        TextView cnt = (TextView) convertView.findViewById(R.id.search_exp_hdr_count);
        StringBuilder count = new StringBuilder();
        count.append("");
        count.append(mCount);
        cnt.setText(count.toString());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.search_exp_list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.search_exp_itm_name);

        ImageView delete = (ImageView) convertView.findViewById(R.id.search_exp_itm_del);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListDataChild.remove(childPosition);
                mCount--;
                notifyDataSetChanged();
            }
        });

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded (int groupPosition){

    }
}
