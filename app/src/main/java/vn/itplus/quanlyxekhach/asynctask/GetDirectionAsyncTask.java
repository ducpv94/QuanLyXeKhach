package vn.itplus.quanlyxekhach.asynctask;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Map;

import vn.itplus.quanlyxekhach.util.GMapV2Direction;

/**
 * Created by AnhlaMrDuc on 16-May-16.
 */
public class GetDirectionAsyncTask extends AsyncTask<Map<String, String>,Object, ArrayList<LatLng>> {
    public static final String USER_CURRENT_LAT = "user_current_lat";
    public static final String USER_CURRENT_LONG = "user_current_long";
    public static final String DESTINATION_LAT = "destination_lat";
    public static final String DESTINATION_LONG = "destination_long";
    public static final String DIRECTIONS_MODE = "directions_mode";

    @Override
    protected ArrayList<LatLng> doInBackground(Map<String, String>... params) {
        Map<String, String> paramMap = params[0];

        LatLng fromPosition = new
                LatLng(Double.valueOf(paramMap.get(USER_CURRENT_LAT)),
                Double.valueOf(paramMap.get(USER_CURRENT_LONG)));

        LatLng toPosition = new
                LatLng(Double.valueOf(paramMap.get(DESTINATION_LAT)),
                Double.valueOf(paramMap.get(DESTINATION_LONG)));

        GMapV2Direction gMapV2Direction = new GMapV2Direction();
        Document document = gMapV2Direction.getDocument(fromPosition, toPosition,
                paramMap.get(DIRECTIONS_MODE));

        ArrayList<LatLng> directionPoints = gMapV2Direction.getDirection(document);

        return directionPoints;
    }
}
