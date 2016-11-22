package com.shah0150algonquinlive.doorsopenottawa;

/**
 * Created by adeshshah on 2016-11-08.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shah0150algonquinlive.doorsopenottawa.model.Building;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static com.shah0150algonquinlive.doorsopenottawa.R.id.itemname;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.MyViewHolder> {

    private Context context;
    private List<Building> buildingList;

    protected LruCache<Integer, Bitmap> imageCache;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView buildingName,buildingDate;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);

            buildingName = (TextView) view.findViewById(itemname);
            buildingDate = (TextView) view.findViewById(R.id.ItemDate);
            thumbnail = (ImageView) view.findViewById(R.id.itemImage);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }

    public BuildingAdapter(Context context, List<Building> building) {
        this.context = context;
        this.buildingList = building;
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;
        imageCache = new LruCache<>(cacheSize);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_building, parent, false);

        //Display planet name in the TextView widget
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Building building = buildingList.get(position);


        String date = "";
        for (int i = 0 ; i<building.getOpenHours().size();i++){
            date+=building.getOpenHours().get(i)+"\n";
        }
        holder.buildingDate.setText(date);
        holder.buildingName.setText(building.getName());

        Bitmap bitmap = imageCache.get(building.getBuildingId());
        if (bitmap != null) {

            holder.thumbnail.setImageBitmap(building.getBitmap());
        }
        else {
            BuildingAndView container = new BuildingAndView();
            container.building = building;
            container.view = holder.itemView;
            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
//                PopupMenu popup = new PopupMenu(context,view);
//                MenuInflater inflater = popup.getMenuInflater();
//                inflater.inflate(R.menu.menu_building, popup.getMenu());
//                popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//                popup.show();
                Snackbar.make(view, "Click on the card for more Information."  , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

    }
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.action_add_favourite:
//                    Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(context, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }
//    private void showPopupMenu(View view) {
//        PopupMenu popup = new PopupMenu(context,view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_building, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//    }


    @Override
    public int getItemCount() {
        return buildingList.size();
    }
    //    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater =
//                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.item_building, parent, false);
//
//        //Display planet name in the TextView widget
//        Building building = buildingList.get(position);
//
//        buildingName = (TextView) view.findViewById(R.id.itemname);
//        buildingDate = (TextView) view.findViewById(R.id.ItemDate);
//        String date = "";
//        for (int i = 0 ; i<building.getOpenHours().size();i++){
//            date+=building.getOpenHours().get(i)+"\n";
//        }
//        buildingDate.setText(date);
//        buildingName.setText(building.getName());
//
//        Bitmap bitmap = imageCache.get(building.getBuildingId());
//        if (bitmap != null) {
//            ImageView image = (ImageView) view.findViewById(R.id.itemImage);
//            image.setImageBitmap(building.getBitmap());
//        }
//        else {
//            BuildingAndView container = new BuildingAndView();
//            container.building = building;
//            container.view = view;
//            ImageLoader loader = new ImageLoader();
//            loader.execute(container);
//        }
//
//        return view;
//    }




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