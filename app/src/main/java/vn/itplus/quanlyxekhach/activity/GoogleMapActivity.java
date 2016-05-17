package vn.itplus.quanlyxekhach.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;

import com.google.android.gms.maps.SupportMapFragment;

import butterknife.Bind;
import butterknife.OnClick;
import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.view.MySupportLocationMapFragment;

/**
 * Created by AnhlaMrDuc on 16-May-16.
 */
public class GoogleMapActivity extends BaseActivity {

    private MySupportLocationMapFragment mSupportLocationMapFragment;
    private AlertDialog dialog;

    @Override
    protected int layoutID() {
        return R.layout.activity_google_map;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mSupportLocationMapFragment = new MySupportLocationMapFragment(this, supportMapFragment);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (checkLocationProviderAndOpenSettingIfNot()) {
            mSupportLocationMapFragment.connect();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSupportLocationMapFragment.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSupportLocationMapFragment.isGoogleApiClientConnected()) {
            mSupportLocationMapFragment.startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSupportLocationMapFragment.stopLocationUpdates();
    }

    @OnClick(R.id.btnLocation)
    public void onClick() {
        mSupportLocationMapFragment.goToLocation();
    }

    /**
     * Check GPS of devide. If GPS off show a Confirm Dialog
     *
     * @return
     */
    public boolean checkLocationProviderAndOpenSettingIfNot() {
        boolean isProviderEnabled = ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isProviderEnabled) {
            // Show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(R.string.message_app_need_location_access)
                    .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(viewIntent);
                        }
                    });
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }
        return isProviderEnabled;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GoogleMapActivity.class);
        context.startActivity(intent);
    }

    @Bind(R.id.btnLocation)
    ImageView btnLocation;
}
