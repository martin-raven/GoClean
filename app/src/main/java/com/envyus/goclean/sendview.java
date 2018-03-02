package com.envyus.goclean;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by seby on 3/2/2018.
 */


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by seby on 3/1/2018.
 */

public class sendview extends Fragment{
    ImageView imageView;
    private GoogleApiClient googleApiClient;
    Double latitude=null,longitude=null;
    Button bt,bt2;
    private static String jsonInString;
    public ProgressDialog mProgressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_sendview, container, false);
        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        imageView=view.findViewById(R.id.sendpreview);
        final Bitmap bitmap = BitmapFactory.decodeByteArray(getArguments().getByteArray("data"), 0, getArguments().getByteArray("data").length);
        imageView.setImageBitmap(bitmap);
        bt=view.findViewById(R.id.upload);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                ContentResolver cR = getContext().getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String type = "jpg";
                String filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                getCurrentLocation();
                profilepic pc=new profilepic();
                try {
                    pc.setUsername("seby");
                    pc.setExtension(type);
                    pc.setImageData(imageUtil.convert(bitmap));
                    pc.setDumbID(filename);
                    pc.setLatt(latitude);
                    pc.setLong(longitude);
                    com.google.gson.Gson gson = new GsonBuilder().create();
                    jsonInString = gson.toJson(pc);
                    Log.d("Json in string", jsonInString);
                    new Uploadpicture().execute("https://3oafpgzf9k.execute-api.ap-south-1.amazonaws.com/DEV/upload");
                }catch (Exception e){Log.d("Exception in send",String.valueOf(e));}
            }
        });
        bt2=view.findViewById(R.id.retake);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new cameraview();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container2, fragment, cameraview.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null)
                        .commit();
            }
        });




    }
    private void getCurrentLocation() {
        //Creating a location object
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        longitude = (double)sharedPreferences.getFloat("LONG", 0);
        latitude = (double)sharedPreferences.getFloat("LAT", 0);
    }
    public class profilepic
    {
        String Username;
        Double Latt,Long;
        public void setLatt(double latt) {
            Latt = latt;
        }
        public void setLong(double log) {
            Long = log;
        }
        public String getUsername() {
            return Username;
        }

        public void setUsername(String username) {
            Username = username;
        }

        public String getExtension() {
            return Extension;
        }

        public void setExtension(String extension) {
            Extension = extension;
        }

        public String getImageData() {
            return ImageData;
        }

        public void setImageData(String imageData) {
            ImageData = imageData;
        }

        String Extension;
        String ImageData;

        public String getDumbID() {
            return DumbID;
        }

        public void setDumbID(String dumbID) {
            DumbID = dumbID;
        }

        String DumbID;
    }

    public class Uploadpicture extends  AsyncTask<String,String,String>  {


        @Override
        protected String doInBackground(String... params) {

            String response = PostJsonUrl.postJsonWithUrl(params[0],jsonInString);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideProgressDialog();
            Fragment fragment=new cameraview();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container2, fragment, cameraview.class.getSimpleName())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null)
                    .commit();
        }
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Uploading");
            mProgressDialog.setIndeterminate(true);

        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}