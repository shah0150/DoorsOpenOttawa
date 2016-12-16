package com.shah0150algonquinlive.doorsopenottawa;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shah0150algonquinlive.doorsopenottawa.model.Building;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.shah0150algonquinlive.doorsopenottawa.MainActivity.REST_URI;

/**
 * Created by adeshshah on 2016-12-14.
 */

public class NewBuildingActivity extends Activity implements View.OnClickListener {

    private static final int IMAGE_CAPTUE = 1;
    private EditText postName,postAddress,postDescription;
    private Button btnAdd,btnCancel,btnTakePhoto;
    private String pName,pAddress,pDescription,imgUrl;
    private Building building;
    private ImageView addImage;
    private Uri imageUri;
    private Bitmap bitmapImg;
    private File realImage;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_building);
        building=new Building();
        postName=(EditText)findViewById(R.id.postname);
        postAddress=(EditText)findViewById(R.id.postaddress);
        postDescription=(EditText)findViewById(R.id.postdescription);
        btnAdd=(Button)findViewById(R.id.postbutton);
        btnAdd.setOnClickListener(this);
        addImage=(ImageView)findViewById(R.id.addImage);
        btnTakePhoto=(Button)findViewById(R.id.takePhoto);
        btnTakePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.takePhoto)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CAPTUE);
        }
        else {
            pName = postName.getText().toString();
            pAddress = postAddress.getText().toString();
            pDescription = postDescription.getText().toString();
            building.setName(pName);
            building.setAddress(pAddress);
            building.setDescription(pDescription);
            RequestPackage rpkg = new RequestPackage();
            rpkg.setMethod(HttpMethod.POST);
            rpkg.setUri(REST_URI + "buildings");
            rpkg.setParam("name", pName);
            rpkg.setParam("address", pAddress);
            rpkg.setParam("description", pDescription);
            rpkg.setParam("image", "Picture.png");
            PostTask pt = new PostTask();
            pt.execute(rpkg);


            NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify = new Notification.Builder
                    (getApplicationContext()).setContentTitle(pName).setContentText(pAddress).
                    setContentTitle(pName).setSmallIcon(R.drawable.ic_stat_name).build();

            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(0, notify);
        }

    }

    private void getData() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_CAPTUE:
                if (resultCode == Activity.RESULT_OK) {

                    if (data != null) {
                        try {
                            bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                            addImage.setImageBitmap(bitmapImg);
                            Uri tempUri = getImageUri(this, bitmapImg);
                            realImage = new File(getRealPathFromURI(tempUri));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        return cursor.getString(column_index);
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
            RequestPackage pkg = new RequestPackage();
            pkg.setMethod(HttpMethod.PUT);
            pkg.setUri(REST_URI + "buildings/" + building.getBuildingId() + "/image");
            pkg.setParam("id", ""+building.getBuildingId());
            pkg.setImageParams("image", realImage);
            PostTask ct = new PostTask();
            ct.execute(pkg);

        }
    }


}
