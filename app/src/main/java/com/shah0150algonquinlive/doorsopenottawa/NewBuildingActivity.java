package com.shah0150algonquinlive.doorsopenottawa;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.shah0150algonquinlive.doorsopenottawa.MainActivity.REST_URI;

/**
 * Created by adeshshah on 2016-12-14.
 */

public class NewBuildingActivity extends Activity implements View.OnClickListener {

    private EditText postName,postAddress,postDescription;
    private Button btnAdd,btnCancel;
    private String pName,pAddress,pDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_building);

        postName=(EditText)findViewById(R.id.postname);
        postAddress=(EditText)findViewById(R.id.postaddress);
        postDescription=(EditText)findViewById(R.id.postdescription);
        btnAdd=(Button)findViewById(R.id.postbutton);
        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        pName=postName.getText().toString();
        pAddress=postAddress.getText().toString();
        pDescription=postDescription.getText().toString();
        RequestPackage rpkg=new RequestPackage();
        rpkg.setMethod(HttpMethod.POST);
        rpkg.setUri(REST_URI + "buildings");
        rpkg.setParam("name",pName);
        rpkg.setParam("address",pAddress);
        rpkg.setParam("description",pDescription);
        rpkg.setParam("image","Picture.png");
        PostTask pt=new PostTask();
        pt.execute(rpkg);


        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext()).setContentTitle(pName).setContentText(pAddress).
                setContentTitle(pName).setSmallIcon(R.drawable.ic_stat_name).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);

    }

    private void getData() {

    }

    public class PostTask extends AsyncTask<RequestPackage, String,String>
    {
        @Override
        protected String doInBackground(RequestPackage... requestPackages) {
           String content=HttpManager.getData(requestPackages[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(getApplicationContext(),"Record added successfully",Toast.LENGTH_SHORT).show();
        }
    }


}
