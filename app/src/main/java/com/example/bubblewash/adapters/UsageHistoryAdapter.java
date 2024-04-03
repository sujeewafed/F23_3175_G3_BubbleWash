package com.example.bubblewash.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bubblewash.R;
import com.example.bubblewash.model.MoreOption;
import com.example.bubblewash.model.UsageHistory;

import java.util.List;

public class UsageHistoryAdapter extends BaseAdapter {
    List<UsageHistory> adapterHistoryList;

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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_item, parent, false);
        }

        TextView txtViewHistoryDate = convertView.findViewById(R.id.txtViewHistoryDate);
        TextView txtViewHistoryCost = convertView.findViewById(R.id.txtViewHistoryCost);
        txtViewHistoryDate.setText((CharSequence) adapterHistoryList.get(position).getDate());
        txtViewHistoryCost.setText((int) adapterHistoryList.get(position).getCost());
        txtViewHistoryDate.setGravity(Gravity.CENTER_VERTICAL);
        txtViewHistoryCost.setGravity(Gravity.CENTER_VERTICAL);

        return convertView;
    }
}
