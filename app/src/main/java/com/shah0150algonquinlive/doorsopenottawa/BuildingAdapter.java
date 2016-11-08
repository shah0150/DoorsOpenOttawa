package com.shah0150algonquinlive.doorsopenottawa;

/**
 * Created by adeshshah on 2016-11-08.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shah0150algonquinlive.doorsopenottawa.model.Building;

import java.util.List;

public class BuildingAdapter extends ArrayAdapter<Building> {

    private Context context;
    private List<Building> buildingList;
    private TextView buildingId, buildingName, buildingAddress;


    public BuildingAdapter(Context context, int resource, List<Building> building) {
        super(context, resource, building);
        this.context = context;
        this.buildingList = building;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_building, parent, false);

        //Display planet name in the TextView widget
        Building building = buildingList.get(position);

        buildingName = (TextView) view.findViewById(R.id.itemname);
        buildingName.setText(building.getName());

        return view;
    }
}