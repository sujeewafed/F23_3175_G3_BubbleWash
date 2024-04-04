package com.example.bubblewash.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bubblewash.R;
import com.example.bubblewash.databinding.LayoutHistoryItemBinding;
import com.example.bubblewash.databinding.LayoutMoreOptionItemBinding;
import com.example.bubblewash.model.MoreOption;
import com.example.bubblewash.model.UsageHistory;

import java.util.List;

public class UsageHistoryAdapter extends BaseAdapter {
    List<UsageHistory> adapterHistoryList;

    LayoutHistoryItemBinding itemBinding;

    public UsageHistoryAdapter(List<UsageHistory> adapterHistoryList) {
        this.adapterHistoryList = adapterHistoryList;
    }

    @Override
    public int getCount() {
        return adapterHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            itemBinding = LayoutHistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        }

        itemBinding.txtViewHistoryDate.setText(adapterHistoryList.get(position).getDate());
        itemBinding.txtViewHistoryCost.setText( Float.toString(adapterHistoryList.get(position).getCost()) );
        itemBinding.txtViewHistoryDate.setGravity(Gravity.CENTER_VERTICAL);
        itemBinding.txtViewHistoryCost.setGravity(Gravity.CENTER_VERTICAL);

        return itemBinding.getRoot();

    }
}
