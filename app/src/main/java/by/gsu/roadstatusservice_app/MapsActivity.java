package by.gsu.roadstatusservice_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.gsu.RoadStatusService.models.Picture;
import by.gsu.RoadStatusService.models.Point;
import by.gsu.client.Client;
import by.gsu.client.IRoadStatusClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int configTiltValue = 45;
    private int configBearingValue = 0;
    private int configZoomValue = 16;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private IRoadStatusClient client = new Client();

    private List<Picture> store = new ArrayList<Picture>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
       // mapFragment.getMapAsync(this);



        new AsyncTask<SupportMapFragment, Integer, List<Picture>>() {
            @Override
            protected List<Picture> doInBackground(SupportMapFragment... arg) {
                List<Picture> list = new ArrayList();
                try {
                    list = client.getListPictures();
                    store =list;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                store =list;
                //Log.i("AAAAAAAA", ""+ store);
                return list;
            }

            @Override
            protected void onPostExecute(List<Picture> result) {
                super.onPostExecute(result);
                Log.i("111111111111111111***11", "");
                loadMap();
            }
        }.execute(mapFragment);
    }

    public void loadMap(){
        //Log.i("22222222222222*****22", "");
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("33333333333333****", "");
       // mMap = googleMap;
        LatLng latLng = new LatLng(-34, 151);
        for (Picture picture : store) {
            Log.i("CCCCCCCCC", ""+picture);
            // Add a marker in Sydney and move the camera
            Point point  = picture.getPoint();
            latLng = new LatLng(point.getX(), point.getY());

            // googleMap.addMarker(new MarkerOptions().position(latLng).title("lol").snippet("aaa"));
            googleMap.addMarker(new MarkerOptions().position(latLng).title(picture.getName()).snippet(picture.getData()));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }


        CameraPosition liberty = CameraPosition.builder().target(latLng).zoom(configZoomValue).bearing(configBearingValue).tilt(configTiltValue).build();

        // onMapReady( googleMap);

    }



}
