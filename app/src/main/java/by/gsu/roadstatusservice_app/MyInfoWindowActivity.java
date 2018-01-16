package by.gsu.roadstatusservice_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.gsu.RoadStatusService.models.Picture;
import by.gsu.RoadStatusService.models.Point;
import by.gsu.client.Client;
import by.gsu.client.IRoadStatusClient;
import by.gsu.roadstatusservice_app.lazylist.ListActivity;

public class MyInfoWindowActivity extends FragmentActivity implements OnMapReadyCallback, LocationSource, LocationListener ,GoogleMap.OnInfoWindowClickListener {

    private final int configTiltValue = 45;
    private int configBearingValue = 0;
    private int configZoomValue = 5;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;


    final int RQS_GooglePlayServices = 1;
    private GoogleMap myMap;
    TextView tvLocInfo;

    LocationManager myLocationManager = null;
    OnLocationChangedListener myLocationListener = null;
    Criteria myCriteria;


    private IRoadStatusClient client = new Client();

    private Intent intent;

    private TextView tvInfo;
    private ProgressBar progressBarForMap;


    private List<Picture> store = new ArrayList<Picture>();

    @Override
    public void onInfoWindowClick(Marker marker) {
        intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            Picture picture = (Picture) marker.getTag();
            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            tvTitle.setText(picture.getId() + "+" + marker.getId() + "-vva-" + marker.getTitle());
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());

            ImageView icon = ((ImageView) myContentsView.findViewById(R.id.icon));
            if (picture.getData() != null) {
                byte[] decodedString = Base64.decode(picture.getData(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                icon.setImageBitmap(decodedByte);
            }
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_window);


        intent = getIntent();
        tvInfo = (TextView) findViewById(R.id.tvInfoMap);
        progressBarForMap = (ProgressBar) findViewById(R.id.progressBarForMap);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // mapFragment.getMapAsync(this);


        myCriteria = new Criteria();
        myCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        new AsyncTask<SupportMapFragment, Integer, List<Picture>>() {
            @SuppressLint("WrongConstant")
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarForMap.setVisibility(View.VISIBLE);
                tvInfo.setVisibility(View.VISIBLE);
                tvInfo.setText("Please wait...");
            }

            @Override
            protected List<Picture> doInBackground(SupportMapFragment... arg) {
                Log.e("start async load data", "");
                List<Picture> list = new ArrayList();
                try {
                    list = client.getListPictures();
                    store = list;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("error client", "" + e.getMessage());
                }
                store = list;
                return list;
            }

            @Override
            protected void onPostExecute(List<Picture> result) {
                super.onPostExecute(result);

                loadMap();
                progressBarForMap.setVisibility(View.INVISIBLE);
                tvInfo.setVisibility(View.INVISIBLE);
                tvInfo.setText("");

                Log.i("end async load data", "");
            }
        }.execute(mapFragment);
    }

    public void loadMap() {
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
        //Log.i("33333333333333****", "");
        // mMap = googleMap;
        googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        // myMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(-50, 50);
        for (Picture picture : store) {
            Point point = picture.getPoint();
            latLng = new LatLng(point.getX(), point.getY());
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(picture.getName()).snippet("Longitude:" + picture.getPoint().getX() + "; Latitude:" + picture.getPoint().getY())).setTag(picture);
        }
        CameraPosition liberty = CameraPosition.builder().target(latLng).zoom(configZoomValue).bearing(configBearingValue).tilt(configTiltValue).build();
    }


    @Override
    public void onLocationChanged(Location location) {
        if (myLocationListener != null) {
            myLocationListener.onLocationChanged(location);
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            tvLocInfo.setText(
                    "lat: " + lat + "\n" +
                            "lon: " + lon);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        myLocationListener = listener;
    }

    @Override
    public void deactivate() {
        myLocationListener = null;
    }
}
