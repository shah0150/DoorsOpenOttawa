package com.shah0150algonquinlive.doorsopenottawa;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shah0150algonquinlive.doorsopenottawa.parsers.BuildingJSONParser;
import com.shah0150algonquinlive.doorsopenottawa.model.Building;
import com.shah0150algonquinlive.doorsopenottawa.HttpMethod;
import com.shah0150algonquinlive.doorsopenottawa.RequestPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private AboutDialogFragment mAboutDialog;
    public static final String REST_URI = "https://doors-open-ottawa-hurdleg.mybluemix.net/";
    private ProgressBar pb;
    private List<MyTask> tasks;


    private List<Building> buildingList;
    private BuildingAdapter ba;
    private RecyclerView recyclerView;
    BuildingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAboutDialog = new AboutDialogFragment();
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();
        requestData(REST_URI + "buildings");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        //Swipe down to refresh List
        final SwipeRefreshLayout mySwipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.activity_main);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
//                        mySwipeRefreshLayout.setProgressViewOffset(true,1,5);
//                        mySwipeRefreshLayout.setColorSchemeColors(3443);
//                        requestData(REST_URI);
                        mySwipeRefreshLayout.setRefreshing(false);
                        updateDisplay();
                    }
                }

        );


    }

    public void requestData(String uri) {
        RequestPackage getPackage = new RequestPackage();
        getPackage.setMethod( HttpMethod.GET );
        getPackage.setUri( uri );
        MyTask task = new MyTask();
        task.execute( getPackage );
    }


    public void updateDisplay() {
        adapter = new BuildingAdapter(this, buildingList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class MyTask extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            String content =HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

            if (result == null) {
                Toast.makeText(MainActivity.this, "Web service not available", Toast.LENGTH_LONG).show();
                return;
            }

            buildingList = BuildingJSONParser.parseFeed(result);
            updateDisplay();
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);


        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    ((adapter)).getFilter().filter(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    ((adapter)).getFilter().filter(s);
                    return false;
                }


            });
        return true;
        }






    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.isCheckable()) {
            // leave if the list is null
            if (buildingList == null) {
                return true;
            }
        }
            Log.i("Buildings", "Sorting");

            switch (item.getItemId()) {
            case R.id.action_about:

                mAboutDialog.show(getFragmentManager(), "About_Dialog");
                return true;

                case R.id.action_user:
                    Intent newBuilding=new Intent(this,NewBuildingActivity.class);
                    startActivity(newBuilding);
                    return true;

            case R.id.action_add_favourite:
                Intent intent = new Intent(this, favourites.class);
                this.startActivity(intent);
                break;

                // which sort menu item did the user pick?

                    case R.id.action_sort_name_asc:
                        Collections.sort(buildingList, new Comparator<Building>() {
                            @Override
                            public int compare(Building lhs, Building rhs) {
                                Log.i("Buildings", "Sorting Buildings by name (a-z)");
                                return lhs.getName().compareTo(rhs.getName());
                            }
                        });
                        break;

                    case R.id.action_sort_name_dsc:
                        Collections.sort(buildingList,Collections.reverseOrder(new Comparator<Building>() {
                            @Override
                            public int compare(Building building, Building t1) {
                                Log.i("Buildings", "Sorting Buildings by name (z-a)");
                                return building.getName().compareTo(t1.getName());
                            };
                        }));
                        break;


                // remember which sort option the user picked

            }
            item.setChecked(true);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            // re-fresh the list to show the sort order
            adapter.notifyDataSetChanged();


        return super.onOptionsItemSelected(item);

        }







    protected class BuildingAndView {
        public Building building; }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
