package com.example.bubblewash.adapters;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.bubblewash.R;
import com.example.bubblewash.model.MoreOption;

import java.util.List;

public class MoreOptionAdapter extends BaseAdapter {

    List<MoreOption> adapterMoreOptionsList;

    public MoreOptionAdapter(List<MoreOption> adapterMoreOptionsList) {
        this.adapterMoreOptionsList = adapterMoreOptionsList;
    }

    @Override
    public int getCount() {
        return adapterMoreOptionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterMoreOptionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_more_option_item, parent, false);
        }

        TextView txtViewMoreOptionItem = convertView.findViewById(R.id.txtViewMoreOptionItem);
        txtViewMoreOptionItem.setText(adapterMoreOptionsList.get(position).getOptionName());
        txtViewMoreOptionItem.setGravity(Gravity.CENTER_VERTICAL);
        // Drawable img = ContextCompat.getDrawable(parent.getContext(), adapterMoreOptionsList.get(position).getOptionIcon());
        // img.setBounds(0,0,80,80);

        ImageView imageViewMoreOptionItem = convertView.findViewById(R.id.imageViewMoreOptionItem);
        imageViewMoreOptionItem.setImageResource(adapterMoreOptionsList.get(position).getOptionIcon());

        return convertView;
    }
}
