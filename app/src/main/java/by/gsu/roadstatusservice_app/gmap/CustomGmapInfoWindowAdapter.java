package by.gsu.roadstatusservice_app.gmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import by.gsu.RoadStatusService.models.Picture;
import by.gsu.roadstatusservice_app.R;

public class CustomGmapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;

    CustomGmapInfoWindowAdapter(LayoutInflater layoutInflater) {
        myContentsView = layoutInflater.inflate(R.layout.custom_gmap_info_window, null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        Picture picture = (Picture) marker.getTag();
        TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
        tvTitle.setText("id:" + picture.getId() + "; name: " + marker.getTitle()+"description: "+picture.getDescription());

        TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
        tvSnippet.setText(marker.getSnippet());

        TextView description = ((TextView) myContentsView.findViewById(R.id.description));
        description.setText("description: "+picture.getDescription());

        ImageView image = ((ImageView) myContentsView.findViewById(R.id.image));
        if (picture.getData() != null) {
            byte[] decodedString = Base64.decode(picture.getData(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            image.setImageBitmap(decodedByte);
        }
        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

}