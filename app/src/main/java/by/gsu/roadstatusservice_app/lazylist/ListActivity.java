package by.gsu.roadstatusservice_app.lazylist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.gsu.RoadStatusService.models.Picture;
import by.gsu.RoadStatusService.models.Point;
import by.gsu.client.Client;
import by.gsu.client.IRoadStatusClient;
import by.gsu.roadstatusservice_app.MainActivity;
import by.gsu.roadstatusservice_app.R;


public class ListActivity extends Activity {
    private IRoadStatusClient client = new Client();
    private List<Picture> store = new ArrayList<Picture>();
    private ListView list;
    private LazyAdapter adapter;

    private Intent intent ;

    private TextView tvInfo;
    private ProgressBar progressBarForList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  setContentView(R.layout.custom_list);
        setContentView(R.layout.custom_list);
        intent = getIntent();
        tvInfo=(TextView) findViewById(R.id.tvInfo);
        progressBarForList=(ProgressBar) findViewById(R.id.progressBarForList);

        list=(ListView)findViewById(R.id.list);


        Button b=(Button)findViewById(R.id.button1);
        b.setOnClickListener(listener);

        new AsyncTask<Activity, Integer, List<Picture>>() {
            @SuppressLint("WrongConstant")
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarForList.setVisibility(View.VISIBLE);
                tvInfo.setVisibility(View.VISIBLE);
                tvInfo.setText("Please wait...");
            }

            @Override
            protected List<Picture> doInBackground(Activity... arg) {
                Log.e("start async load data", "");
                List<Picture> list = new ArrayList();

                try {
                    list = client.getListPictures();
                    store =list;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("error client", ""+e.getMessage());
                }
                store =list;
                return list;
            }

            @SuppressLint("WrongConstant")
            @Override
            protected void onPostExecute(List<Picture> result) {
                super.onPostExecute(result);

                loadMap();
                progressBarForList.setVisibility(View.INVISIBLE);
                tvInfo.setVisibility(View.INVISIBLE);
                tvInfo.setText("");

                Log.i("end async load data", "");
            }
        }.execute(this);
    }

    public void loadMap(){
        Log.i("22222222222222*****22", "");
        adapter=new LazyAdapter(this, store);
        list.setAdapter(adapter);
    }
    
    @Override
    public void onDestroy()
    {
        list.setAdapter(null);
        super.onDestroy();
    }


    public OnClickListener listener=new OnClickListener(){
        @Override
        public void onClick(View arg0) {
            intent = new Intent(ListActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };


  /*  private String[] mStrings={
            "https://pbs.twimg.com/profile_images/3092003750/9b72a46e957a52740c667f4c64fa5d10_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2508170683/m8jf0po4imu8t5eemjdd_normal.png",
            "https://pbs.twimg.com/profile_images/1701796334/TA-New-Logo_normal.jpg",
            "https://pbs.twimg.com/profile_images/913338263/AndroidPolice_logo_normal.png",
            "https://pbs.twimg.com/profile_images/1417650153/android-hug_normal.png",
            "https://pbs.twimg.com/profile_images/1517737798/aam-twitter-right-final_normal.png",
            "https://pbs.twimg.com/profile_images/3319660679/70e7025a05b674852b9f3cea0998259c_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2100693240/58534_150210305010136_148613708503129_315282_6481640_n_normal.jpg",
            "https://pbs.twimg.com/profile_images/1306095935/androidcoo_normal.png",
            "https://pbs.twimg.com/profile_images/2938108229/399ba333772228bfbb40134018fbe777_normal.jpeg",
            "https://pbs.twimg.com/profile_images/487047133392949248/sVTI9rGI_normal.png",
            "https://pbs.twimg.com/profile_images/3092003750/9b72a46e957a52740c667f4c64fa5d10_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2508170683/m8jf0po4imu8t5eemjdd_normal.png",
            "https://pbs.twimg.com/profile_images/1701796334/TA-New-Logo_normal.jpg",
            "https://pbs.twimg.com/profile_images/913338263/AndroidPolice_logo_normal.png",
            "https://pbs.twimg.com/profile_images/1417650153/android-hug_normal.png",
            "https://pbs.twimg.com/profile_images/1517737798/aam-twitter-right-final_normal.png",
            "https://pbs.twimg.com/profile_images/3319660679/70e7025a05b674852b9f3cea0998259c_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2100693240/58534_150210305010136_148613708503129_315282_6481640_n_normal.jpg",
            "https://pbs.twimg.com/profile_images/1306095935/androidcoo_normal.png",
            "https://pbs.twimg.com/profile_images/2938108229/399ba333772228bfbb40134018fbe777_normal.jpeg",
            "https://pbs.twimg.com/profile_images/487047133392949248/sVTI9rGI_normal.png","https://pbs.twimg.com/profile_images/3092003750/9b72a46e957a52740c667f4c64fa5d10_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2508170683/m8jf0po4imu8t5eemjdd_normal.png",
            "https://pbs.twimg.com/profile_images/1701796334/TA-New-Logo_normal.jpg",
            "https://pbs.twimg.com/profile_images/913338263/AndroidPolice_logo_normal.png",
            "https://pbs.twimg.com/profile_images/1417650153/android-hug_normal.png",
            "https://pbs.twimg.com/profile_images/1517737798/aam-twitter-right-final_normal.png",
            "https://pbs.twimg.com/profile_images/3319660679/70e7025a05b674852b9f3cea0998259c_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2100693240/58534_150210305010136_148613708503129_315282_6481640_n_normal.jpg",
            "https://pbs.twimg.com/profile_images/1306095935/androidcoo_normal.png",
            "https://pbs.twimg.com/profile_images/2938108229/399ba333772228bfbb40134018fbe777_normal.jpeg",
            "https://pbs.twimg.com/profile_images/487047133392949248/sVTI9rGI_normal.png","https://pbs.twimg.com/profile_images/3092003750/9b72a46e957a52740c667f4c64fa5d10_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2508170683/m8jf0po4imu8t5eemjdd_normal.png",
            "https://pbs.twimg.com/profile_images/1701796334/TA-New-Logo_normal.jpg",
            "https://pbs.twimg.com/profile_images/913338263/AndroidPolice_logo_normal.png",
            "https://pbs.twimg.com/profile_images/1417650153/android-hug_normal.png",
            "https://pbs.twimg.com/profile_images/1517737798/aam-twitter-right-final_normal.png",
            "https://pbs.twimg.com/profile_images/3319660679/70e7025a05b674852b9f3cea0998259c_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2100693240/58534_150210305010136_148613708503129_315282_6481640_n_normal.jpg",
            "https://pbs.twimg.com/profile_images/1306095935/androidcoo_normal.png",
            "https://pbs.twimg.com/profile_images/2938108229/399ba333772228bfbb40134018fbe777_normal.jpeg",
            "https://pbs.twimg.com/profile_images/487047133392949248/sVTI9rGI_normal.png","https://pbs.twimg.com/profile_images/3092003750/9b72a46e957a52740c667f4c64fa5d10_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2508170683/m8jf0po4imu8t5eemjdd_normal.png",
            "https://pbs.twimg.com/profile_images/1701796334/TA-New-Logo_normal.jpg",
            "https://pbs.twimg.com/profile_images/913338263/AndroidPolice_logo_normal.png",
            "https://pbs.twimg.com/profile_images/1417650153/android-hug_normal.png",
            "https://pbs.twimg.com/profile_images/1517737798/aam-twitter-right-final_normal.png",
            "https://pbs.twimg.com/profile_images/3319660679/70e7025a05b674852b9f3cea0998259c_normal.jpeg",
            "https://pbs.twimg.com/profile_images/2100693240/58534_150210305010136_148613708503129_315282_6481640_n_normal.jpg",
            "https://pbs.twimg.com/profile_images/1306095935/androidcoo_normal.png",
            "https://pbs.twimg.com/profile_images/2938108229/399ba333772228bfbb40134018fbe777_normal.jpeg",
            "https://pbs.twimg.com/profile_images/487047133392949248/sVTI9rGI_normal.png"
    };*/
}