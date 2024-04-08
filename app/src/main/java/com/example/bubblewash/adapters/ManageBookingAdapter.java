package com.example.bubblewash.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.bubblewash.databinding.LayoutManagebookingItemBinding;
import com.example.bubblewash.model.ManageBookingAdminPanel;

import java.util.List;

public class ManageBookingAdapter extends BaseAdapter {

    List<ManageBookingAdminPanel> adapterManageBookingList;

    LayoutManagebookingItemBinding itemBinding;

    public ManageBookingAdapter(List<ManageBookingAdminPanel> adapterManageBookingList) {
        this.adapterManageBookingList = adapterManageBookingList;
    }

    @Override
    public int getCount() {
        return adapterManageBookingList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterManageBookingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            itemBinding = LayoutManagebookingItemBinding
                            .inflate(LayoutInflater
                                    .from(parent.getContext()), parent, false);
        }

        itemBinding.txtViewItemDate.setText(adapterManageBookingList.get(position).getDate());
        itemBinding.txtViewItemMonth.setText(adapterManageBookingList.get(position).getMonth());
        itemBinding.txtViewItemPickDate.setText(adapterManageBookingList.get(position).getPickDate());
        itemBinding.txtViewItemPickTime.setText(adapterManageBookingList.get(position).getPickTime());
        itemBinding.txtViewItemStatus.setText(adapterManageBookingList.get(position).getStatus().toString());

        /*itemBinding.txtViewItemDate.setGravity(Gravity.CENTER_VERTICAL);
        itemBinding.txtViewItemMonth.setGravity(Gravity.CENTER_VERTICAL);
        itemBinding.txtViewItemPickDate.setGravity(Gravity.CENTER_VERTICAL);
        itemBinding.txtViewItemPickTime.setGravity(Gravity.CENTER_VERTICAL);
        itemBinding.txtViewItemStatus.setGravity(Gravity.CENTER_VERTICAL);*/

        return itemBinding.getRoot();
    }
}
