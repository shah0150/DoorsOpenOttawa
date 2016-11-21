package com.shah0150algonquinlive.doorsopenottawa;

/**
 * Created by adeshshah on 2016-11-08.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import com.shah0150algonquinlive.doorsopenottawa.model.Building;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class BuildingAdapter extends ArrayAdapter<Building> {

    private Context context;
    private List<Building> buildingList;
    private TextView buildingId, buildingName, buildingAddress, buildingDate;
    protected LruCache<Integer, Bitmap> imageCache;


    public BuildingAdapter(Context context, int resource, List<Building> building) {
        super(context, resource, building);
        this.context = context;
        this.buildingList = building;
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;
        imageCache = new LruCache<>(cacheSize);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_building, parent, false);

        //Display planet name in the TextView widget
        Building building = buildingList.get(position);

        buildingName = (TextView) view.findViewById(R.id.itemname);
        buildingDate = (TextView) view.findViewById(R.id.ItemDate);
        String date = "";
        for (int i = 0 ; i<building.getOpenHours().size();i++){
            date+=building.getOpenHours().get(i)+"\n";
        }
        buildingDate.setText(date);
        buildingName.setText(building.getName());

        Bitmap bitmap = imageCache.get(building.getBuildingId());
        if (bitmap != null) {
            ImageView image = (ImageView) view.findViewById(R.id.itemImage);
            image.setImageBitmap(building.getBitmap());
        }
        else {
            BuildingAndView container = new BuildingAndView();
            container.building = building;
            container.view = view;
            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }

        return view;
    }




protected class BuildingAndView {
    public Building building;
    public View view;
    public Bitmap bitmap;
}

private class ImageLoader extends AsyncTask<BuildingAndView, Void, BuildingAndView> {

    @Override
    protected BuildingAndView doInBackground(BuildingAndView... params) {
        BuildingAndView container = params[0];
        Building building = container.building;

        try {
            String imageUrl =  MainActivity.REST_URI.concat(building.getImage());
            InputStream in = (InputStream) new URL(imageUrl).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            building.setBitmap(bitmap);
            in.close();
            container.bitmap = bitmap;
            return container;
        }
        catch (Exception e) {
            Log.i("ImageLoader", e.getMessage());
            e.printStackTrace();
        }

        return new BuildingAndView();
    }

    @Override
    protected void onPostExecute(BuildingAndView result) {
        ImageView image = (ImageView) result.view.findViewById(R.id.itemImage);
        image.setImageBitmap(result.bitmap);
        result.building.setBitmap(result.bitmap);
        imageCache.put(result.building.getBuildingId(), result.bitmap);
    }
}
}