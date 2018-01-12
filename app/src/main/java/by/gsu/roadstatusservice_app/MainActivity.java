package by.gsu.roadstatusservice_app;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import by.gsu.RoadStatusService.models.Picture;
import by.gsu.client.Client;
import by.gsu.client.IRoadStatusClient;
import by.gsu.roadstatusservice_app.exceptions.LocationException;
import by.gsu.roadstatusservice_app.utils.LocationUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    enum Action {
        PHOTO;
    }

    private LocationUtils locationUtils = new LocationUtils(this);
    private Location location;
    private String locationInfo = "";

    private IRoadStatusClient client = new Client();

    private ImageView iv;
    private TextView helloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            location = locationUtils.getLocation();
            locationInfo = ("Latitude:" + location.getLatitude() + "; Latitude:" + location.getLatitude() + ";");
        } catch (LocationException e) {
            locationInfo = (e.getMessage());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, locationInfo, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        iv = (ImageView) findViewById(R.id.imageView2);
        helloTextView = (TextView) findViewById(R.id.helloTextView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            // startActivity(intent);
            startActivityForResult(intent, Action.PHOTO.ordinal());

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            int ii = 2;
            new AsyncTask<Integer, Integer, String>() {
                @Override
                protected String doInBackground(Integer... arg) {
                    try {
                        newPicture = client.methodGetPicture(arg[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "";
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //  new PhotoActivity.AsyncRequest().execute("123", "/ajax", "foo=bar");


                    helloTextView.setText(newPicture.toString());

                }
            }.execute(ii);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Action action = Action.values()[requestCode];
        switch (action) {
            case PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                    intent.putExtras(data);
                    intent.putExtra(Location.class.getCanonicalName(), location);
                    startActivity(intent);
                }
        }
    }

    private Picture newPicture;
}
