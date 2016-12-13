package com.shah0150algonquinlive.doorsopenottawa;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shah0150algonquinlive.doorsopenottawa.model.Building;

import java.util.ArrayList;
import java.util.List;

public class favourites extends RecyclerView.Adapter<BuildingAdapter.MyViewHolder> {
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private Bundle b;
    private Context context;
    private List<Building> buildingList;

    protected LruCache<Integer, Bitmap> imageCache;



    @Override
    public BuildingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BuildingAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
