package vn.itplus.quanlyxekhach.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AnhlaMrDuc on 18-May-16.
 * Check internet connection
 */
public class ConnectionDetector {
    private Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    /**
     *
     * @return true if has internet or return false
     */
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if (infos != null) {
                for (int i = 0; i < infos.length; i++) {
                    if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
