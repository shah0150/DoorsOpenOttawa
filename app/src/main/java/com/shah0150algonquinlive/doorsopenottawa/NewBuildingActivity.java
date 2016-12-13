package com.shah0150algonquinlive.doorsopenottawa;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shah0150algonquinlive.doorsopenottawa.model.Building;

import static com.shah0150algonquinlive.doorsopenottawa.MainActivity.REST_URI;

public class NewBuildingActivity extends FragmentActivity {
    private String buildingName;
    private String buildingAdress;
    private String buildingdescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_building);
        Button btn = (Button) findViewById(R.id.postbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                // Perform action on click

                postBuilding();

            }
        });

        Button cbtn = (Button) findViewById(R.id.cancelButton);
        cbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
//               NavUtils.navigateUpFromSameTask(getParent());
            }
        });
    }


    private void postBuilding() {
        final EditText bname = (EditText) findViewById(R.id.postname);
        buildingName = bname.getText().toString();

        final EditText baddress = (EditText) findViewById(R.id.postaddress);
        buildingAdress = baddress.getText().toString();

        final EditText bdescription = (EditText) findViewById(R.id.postdescription);
        buildingdescription = bdescription.getText().toString();

        createBuilding(REST_URI);
    }

    private void createBuilding(String uri) {

        Building building = new Building();
        building.setName(buildingName);
        building.setAddress(buildingAdress);
        building.setDescription(buildingdescription);
        building.setImage("xxx");


        RequestPackage pkg = new RequestPackage();
        pkg.setMethod(HttpMethod.POST);
        pkg.setUri(uri + "buildings");
        pkg.setParam("name", building.getName());
        pkg.setParam("address", building.getAddress());
        pkg.setParam("description", building.getDescription());
        pkg.setParam("image", building.getImage());

        NewBuildingActivity.DoTask postTask = new DoTask();
        postTask.execute(pkg);


    }

    private class DoTask extends AsyncTask<RequestPackage, String, String> {

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
                Toast.makeText(NewBuildingActivity.this, "Failed To add a building", Toast.LENGTH_LONG).show();
                return;
            }else{
                Toast.makeText(NewBuildingActivity.this, "Building Succsessfully Added", Toast.LENGTH_LONG).show();
            }
        }
    }

}


