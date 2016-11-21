package com.shah0150algonquinlive.doorsopenottawa.parsers;

import com.shah0150algonquinlive.doorsopenottawa.model.Building;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by adeshshah on 2016-11-08.
 */

public class BuildingJSONParser {
    public static List<Building> parseFeed(String content) {
        try {
            JSONObject jsonResponse = new JSONObject(content);
            JSONArray buildingArray = jsonResponse.getJSONArray("buildings");
            List<String> openHours;

            List<Building> listbuilding = new ArrayList<>();
            for (int i = 0; i < buildingArray.length(); i++) {
                JSONObject obj = buildingArray.getJSONObject(i);
                Building building = new Building();
                building.setName(obj.getString("name"));
                JSONArray calender = obj.getJSONArray("open_hours");
                openHours = new ArrayList<>();
                for (int j=0; j< calender.length();j++){

                    openHours.add(calender.getJSONObject(j).getString("date"));

                }
                building.setOpenHours(openHours);
                building.setAddress(obj.getString("address"));
                building.setBuildingId(obj.getInt("buildingId"));
                building.setImage(obj.getString("image"));
                listbuilding.add(building);
            }
            return listbuilding;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
