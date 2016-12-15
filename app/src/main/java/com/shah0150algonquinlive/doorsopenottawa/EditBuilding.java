package com.shah0150algonquinlive.doorsopenottawa;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.shah0150algonquinlive.doorsopenottawa.MainActivity.REST_URI;



/**
 *  To Create a doors open ottawa application for Doors Open calling an API afrom Gerry's Bluemix Server.
 *  CRUD operation was every exciting learned many things with this.
 *  Want to learn more about uploading a file/image to the server but the time limit won this time :(
 *  But I will be doing upload/file image over the break.
 *  Overall I really liked it and loved trying different things
 *  @author Adesh Shah (shah0150@algonquinlive.com)
 */


public class EditBuilding extends AppCompatActivity implements View.OnClickListener {

    private EditText pName,pAddress,pDescription;
    private Button btnEdit,btnCancel;
    private String buildingName,buildingDescription,buildingAddress;
    private Integer buildingId;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_building);
        b=getIntent().getExtras();
        pName=(EditText)findViewById(R.id.postname);
        pAddress=(EditText)findViewById(R.id.postaddress);
        pDescription=(EditText)findViewById(R.id.postdescription);
        btnEdit=(Button)findViewById(R.id.postbutton);
        btnEdit.setOnClickListener(this);

        if(b!=null)
        {
            buildingId=b.getInt("id");
            buildingName=b.getString("name");
            buildingDescription=b.getString("description");
            buildingAddress=b.getString("address");
            pName.setText(buildingName);
            pAddress.setText(buildingAddress);
            pDescription.setText(buildingDescription);
        }
    }


    @Override
    public void onClick(View view) {
        RequestPackage pkg=new RequestPackage();
        pkg.setMethod(HttpMethod.PUT);
        pkg.setUri(REST_URI + "buildings/" + buildingId);
        pkg.setParam("address",pAddress.getText().toString());
        pkg.setParam("description",pDescription.getText().toString());
        EditTask edtTask=new EditTask();
        edtTask.execute(pkg);

        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext()).setContentTitle("Edit").setContentText("Building Edited Succesful").
                setContentTitle("ok").setSmallIcon(R.drawable.ic_stat_name).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);


    }
    public class EditTask extends AsyncTask<RequestPackage, String,String>
    {
        @Override
        protected String doInBackground(RequestPackage... requestPackages) {
            String content=HttpManager.getData(requestPackages[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"Record updated successfully",Toast.LENGTH_SHORT).show();
        }
    }

}
