package by.gsu.roadstatusservice_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import by.gsu.RoadStatusService.models.Picture;
import by.gsu.RoadStatusService.models.Point;
import by.gsu.client.Client;
import by.gsu.client.IRoadStatusClient;

public class PhotoActivity extends AppCompatActivity {

    private IRoadStatusClient client = new Client();
    private ImageView iv;
    private Button buttonSave;
    private Button buttonCancel;

    private EditText editFileName;
    private EditText editDescription;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, sb.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        location = (Location) getIntent().getParcelableExtra(Location.class.getCanonicalName());
        iv = (ImageView) findViewById(R.id.photoImageView);
        editFileName = (EditText) findViewById(R.id.editFileName);
        editDescription = (EditText) findViewById(R.id.editDescription);

        Intent data = getIntent();

        //String fName = intent.getStringExtra("fname");






        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        iv.setImageBitmap(bitmap);


        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();

                Bitmap bitmap1 = drawable.getBitmap();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                Point point = new Point(location.getLatitude(), location.getLongitude());

                String fname=editFileName.getText().equals("")?System.currentTimeMillis() + ".jpg":editFileName.getText() + ".jpg";
                String escription=""+ editDescription.getText();
                newPicture = new Picture(331, fname, escription, point, encoded);

                new AsyncTask<Integer, Integer, String>() {
                    @Override
                    protected String doInBackground(Integer... arg) {
                        client.methodPostPicture(newPicture);
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        sb.append("lol");
                    }
                }.execute();

            }
        });

     /*   buttonCancel = (Button) findViewById(R.id.buttonSave);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });*/
    }

    private Picture newPicture;
    private StringBuilder sb = new StringBuilder("Replace with your own action");


}



