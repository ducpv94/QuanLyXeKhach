package vn.itplus.quanlyxekhach.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.activity.BaseActivity;
import vn.itplus.quanlyxekhach.asynctask.GetDirectionAsyncTask;
import vn.itplus.quanlyxekhach.util.Constant;
import vn.itplus.quanlyxekhach.util.GMapV2Direction;

/**
 * Created by AnhlaMrDuc on 16-May-16.
 */
public class MySupportLocationMapFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "MySupportLocationMapFragment";
    public static final LatLng CAM_PHA_CAR_STATION = new LatLng(21.0057695, 107.2883979);

    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity mActivity;
    private LocationRequest mLocationRequest;
    private Location mLocation;

    private Marker mMarker;
    private volatile boolean needViewAll = true;
    private boolean hasPolyLine = false;

    public MySupportLocationMapFragment(FragmentActivity mActivity, SupportMapFragment supportMapFragment) {
        this.mActivity = mActivity;

        supportMapFragment.getMapAsync(this);

        MapsInitializer.initialize(mActivity.getApplicationContext());

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createLocationRequest();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, ": onMapReady");
        this.googleMap = googleMap;

        if (needViewAll) {
            viewAll();
        }
        //set Map TYPE
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //enable Current location Button
        googleMap.setMyLocationEnabled(false);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, ": Google Api connected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        addMyMarkerToMap(location);
        zoomToLocation(location);
        LatLng toLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (!hasPolyLine)
            findDirectionAndDrawPolyLine(toLocation, CAM_PHA_CAR_STATION, GMapV2Direction.MODE_DRIVING);
        else if (location.getSpeed() >= Constant.MIN_SPEED)
            findDirectionAndDrawPolyLine(toLocation, CAM_PHA_CAR_STATION, GMapV2Direction.MODE_DRIVING);

        addMarkerToMap(CAM_PHA_CAR_STATION, R.drawable.ic_store_marker_not,
                mActivity.getString(R.string.cam_pha_car_station));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Request Location 10s per time, the fastest is 5s
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * connect GoogleApiClient
     */
    public void connect() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * disconnect GoogleApiClient
     */
    public void disconnect() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isGoogleApiClientConnected() {
        return mGoogleApiClient.isConnected();
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    public LatLng getCurrentLatLng() {
        if (mLocation != null) {
            return new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        }
        return null;
    }

    /**
     * Add Marker to currently my location
     *
     * @param location currently
     */
    private void addMyMarkerToMap(Location location) {
        if (mMarker != null) {
            mMarker.remove();
        }

        mMarker = addMarkerToMap(location,
                R.drawable.ic_marker_trucker, mActivity.getString(R.string.your_locate));
    }

    private Marker addMarkerToMap(Location location, int bitmapID, String title) {
        return addMarkerToMap(new LatLng(location.getLatitude(), location.getLongitude()), bitmapID, title);
    }

    /**
     * @param latLng   My location
     * @param bitmapID Image ID
     * @param title    Title of marker
     * @return
     */
    @SuppressLint("LongLogTag")
    private Marker addMarkerToMap(LatLng latLng, int bitmapID, String title) {
        if (googleMap == null) {
            Log.e(TAG, " map not ready");
            return null;
        }
        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(bitmapID)));
    }

    public void goToLocation() {
        zoomToLocation(mLocation);
    }

    @SuppressLint("LongLogTag")
    private void zoomToLocation(Location location) {
        float zoomLever = 10;

        if (googleMap == null) {
            Log.e(TAG, "map not ready");
            return;
        } else if (mLocation == null) {
            ((BaseActivity) mActivity).showLongToast(R.string.notif_wait_for_your_location);
            return;
        }

        googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), zoomLever));

        Log.e(TAG, ": zoom");

        googleMap.getUiSettings().setAllGesturesEnabled(true);
    }

    @SuppressLint("LongLogTag")
    public void viewAll() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if (mMarker != null) {
            builder.include(mMarker.getPosition());
        }
        if (googleMap == null) {
            needViewAll = true;
        }
        try {
//        int padding = 200; // offset from edges of the map in pixels
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), (int) mActivity.getResources().getDimension(R.dimen.map_padding)));
            needViewAll = false;
        } catch (Exception ex) {
            Log.e(TAG, "Map size can't be 0. Most likely, layout has not yet occured for the map view.  " +
                    "Either wait until layout has occurred or use newLatLngBounds(LatLngBounds, int, int, int) " +
                    "which allows you to specify the map's dimensions.");
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    viewAll();
                }
            });
        }
    }

    private void findDirectionAndDrawPolyLine(LatLng fromPostion, LatLng toPostion, String mode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionAsyncTask.USER_CURRENT_LAT,
                String.valueOf(fromPostion.latitude));
        map.put(GetDirectionAsyncTask.USER_CURRENT_LONG,
                String.valueOf(fromPostion.longitude));

        map.put(GetDirectionAsyncTask.DESTINATION_LAT,
                String.valueOf(toPostion.latitude));
        map.put(GetDirectionAsyncTask.DESTINATION_LONG,
                String.valueOf(toPostion.longitude));

        map.put(GetDirectionAsyncTask.DIRECTIONS_MODE, mode);


        GetDirectionAsyncTask getDirectionAsyncTask = new GetDirectionAsyncTask() {
            @Override
            protected void onPostExecute(ArrayList directionPoints) {
                Log.e("MyGoogleMapActivity", "deriection size ="
                        + directionPoints.size());
                PolylineOptions polylineOptions = new PolylineOptions()
                        .width(7).color(Color.RED);
                for (int i = 0; i < directionPoints.size(); i++) {
                    polylineOptions.add((LatLng) directionPoints.get(i));
                }
                googleMap.addPolyline(polylineOptions);
                hasPolyLine = true;
            }
        };
        getDirectionAsyncTask.execute(map);
    }
}
