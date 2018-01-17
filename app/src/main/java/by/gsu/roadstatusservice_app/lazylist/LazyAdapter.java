package by.gsu.roadstatusservice_app.lazylist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.gsu.RoadStatusService.models.Picture;
import by.gsu.roadstatusservice_app.ImageActivity;
import by.gsu.roadstatusservice_app.R;


public class LazyAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<Picture> data = new ArrayList<Picture>();
    private ImageLoader imageLoader;

    public LazyAdapter(Activity a, List<Picture> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.custom_list_item, null);

        TextView text = (TextView) vi.findViewById(R.id.text);

        ImageView image = (ImageView) vi.findViewById(R.id.image);
        Picture picture = data.get(position);
        text.setText("Name: " + picture.getName());

        imageLoader.DisplayImage(picture.getData(), image);
        final Picture picture1 = picture;

        vi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ImageActivity.class);
                intent.putExtras(activity.getIntent());
                intent.putExtra("picture", picture1);
                activity.startActivity(intent);
            }
        });


        return vi;
    }
}