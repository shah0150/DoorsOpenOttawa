package com.shah0150algonquinlive.doorsopenottawa;

/**
 * Created by adeshshah on 2016-11-08.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.shah0150algonquinlive.doorsopenottawa.model.Building;
import com.shah0150algonquinlive.doorsopenottawa.MainActivity;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.shah0150algonquinlive.doorsopenottawa.MainActivity.REST_URI;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.MyViewHolder> implements View.OnClickListener, Filterable {

    private Context context;
    private List<Building> buildingList;

    protected LruCache<Integer, Bitmap> imageCache;

    private Building mSelectedBuilding;

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(context, DetailsActivity.class);

        Bundle b=new Bundle();
        b.putString("id",""+mSelectedBuilding.getBuildingId());
        b.putString("name", mSelectedBuilding.getName());
        b.putString("description", mSelectedBuilding.getDescription());
        b.putString("address",mSelectedBuilding.getAddress());
        intent.putExtras(b);
        context.startActivity(intent);



    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView buildingName,buildingDate;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);

            buildingName = (TextView) view.findViewById(R.id.itemname);
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
/* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
//    public void clear() {
//        items.clear();
//        notifyDataSetChanged();
//    }
//
//    // Add a list of items
//    public void addAll(List<Building> buildingList) {
//        items.addAll(buildingList);
//        notifyDataSetChanged();
//    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Building building = buildingList.get(position);


        String date = "";
        for (int i = 0 ; i<building.getOpenHours().size();i++){
            date+=building.getOpenHours().get(i)+"\n";
        }
        holder.buildingDate.setText(date);
        holder.buildingName.setText(building.getName());
        //holder.buildingDescription.setText(building.getDescription());
        holder.itemView.setOnClickListener(this);

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
                mSelectedBuilding=building;
                PopupMenu popup = new PopupMenu(context,view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_building, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
                popup.show();
                Snackbar snackbar =  Snackbar.make(view, "Building added to favourites!"  , Snackbar.LENGTH_LONG)
                        .setAction("Favourites", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, favourites.class);
                                intent.putExtra("title",building.getName());
                                intent.putExtra("image",building.getImage());
                                intent.putExtra("address",building.getAddress());

                                context.startActivity(intent);
                            }
                        });
//
//                snackbar.show();
//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, "Message is deleted", Snackbar.LENGTH_LONG)
//                        .setAction("UNDO", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
//                                snackbar1.show();
//                            }
//                        });
//
//                snackbar.show();


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///Snackbar.make(view, "Click on the card for more Information."  , Snackbar.LENGTH_LONG).setAction("Action", null).show();
// view.getContext().startActivity(new Intent(view.getContext(),DetailsActivity.class));
                Intent intent=new Intent(view.getContext(), DetailsActivity.class);
                Bundle b=new Bundle();
                b.putString("title",building.getName());
                b.putString("image",building.getImage());
                b.putString("address",building.getAddress());
                b.putString("description",building.getDescription());
                ArrayList<String> opnhours= (ArrayList<String>) building.getOpenHours();
                b.putStringArrayList("date",opnhours);
                intent.putExtras(b);
                view.getContext().startActivity(intent);
//
            }
        });


    }

//    Snackbar snackbar = Snackbar
    //                        .make(view, "Message is deleted", Snackbar.LENGTH_LONG)
//                        .setAction("UNDO", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Snackbar snackbar1 = Snackbar.make(view, "Message is restored!", Snackbar.LENGTH_SHORT);
//                                snackbar1.show();
//                            }
//                        });
//
//                snackbar.show();
    public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {



        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(, "Add to favourite", Toast.LENGTH_SHORT).show();
                    Log.d("Action_Fav", "Added to favourite");
                    return true;

                case R.id.action_delete:
                    delete();

                    break;
                case R.id.action_edit:
                    edit();
                    break;
                default:
            }
            return false;
        }
    }

    private void edit() {
        Intent intent=new Intent(context, EditBuilding.class);

        Bundle b=new Bundle();
        b.putInt("id",mSelectedBuilding.getBuildingId());
        b.putString("name", mSelectedBuilding.getName());
        b.putString("description", mSelectedBuilding.getDescription());
        b.putString("address",mSelectedBuilding.getAddress());
        intent.putExtras(b);
        context.startActivity(intent);

    }

    private void delete() {

        openDialog();
    }
    public void openDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete Data");
        alertDialogBuilder.setMessage("Are you sure to delete the record?");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            RequestPackage pkg = new RequestPackage();
            pkg.setMethod(HttpMethod.DELETE);
            pkg.setUri(REST_URI + "buildings/"+ mSelectedBuilding.getBuildingId());
            DeleteTask delTask=new DeleteTask();
            delTask.execute(pkg);



        }
       });
       alertDialogBuilder.setNegativeButton("Cancel",
       new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class DeleteTask extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(RequestPackage... params) {


            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                Toast.makeText(context, "Failed To delete a building", Toast.LENGTH_LONG).show();
                return;
            }else{
                Toast.makeText(context, "Building Succsessfully Deleted", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(context,view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_building, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


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
        if(result.view!=null) {
            ImageView image = (ImageView) result.view.findViewById(R.id.itemImage);
            image.setImageBitmap(result.bitmap);
            result.building.setBitmap(result.bitmap);
            imageCache.put(result.building.getBuildingId(), result.bitmap);
        }
    }
}


    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                buildingList = (List<Building>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new Filter.FilterResults();
                ArrayList<Building> FilteredArrayNames = new ArrayList<>();

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < buildingList.size(); i++) {
                    String dataNames = buildingList.get(i).getName();
                    if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArrayNames.add(buildingList.get(i));
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }

}