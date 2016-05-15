package vn.itplus.quanlyxekhach.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpStatus;

import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.exception.ExceptionConstant;
import vn.itplus.quanlyxekhach.util.Constant;

/**
 * Created by AnhlaMrDuc on 14-May-16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private TextView txtHeaderTitle;
    private ImageView imgHeaderBack;
    private Toast toast;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, ": onCreate");

        setContentView(layoutID());
        initViews(savedInstanceState);
        initVariables(savedInstanceState);

        setupSharedPrefrences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, ": onDestroy");
    }

    /**
     *
     * @return layout ID of Activity. Example: R.layout.main_activity
     */
    protected abstract int layoutID();

    /**
     * Initialize views for activity. Example; findViewByID, ButterKnife.bind
     * @param savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * Initialize variables for activity
     * @param savedInstanceState
     */
    protected abstract void  initVariables(Bundle savedInstanceState);

    /**
     * Initialize Header for activity
     */
    protected void initHeader(String title) {
        txtHeaderTitle = (TextView) findViewById(R.id.txt_header_title);
        txtHeaderTitle.setText(title);

        imgHeaderBack = (ImageView) findViewById(R.id.img_back);
        imgHeaderBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLefActionHeaderClick();
            }
        });
    }

    private void onLefActionHeaderClick() {
        finish();
    }

    /**
     * Set up SharedPreferences to save user data
     */
    private void setupSharedPrefrences() {
        if (sharedPreferences == null) {

            sharedPreferences = getSharedPreferences(Constant.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    /**
     * Handle Error when connect to server and show a notification for user
     * @param errorCode
     * @param errorMessage
     * @param showToast
     */
    public void handleError(int errorCode, String errorMessage, boolean showToast) {
        Log.e(TAG, errorCode + " " + errorMessage);

        if (errorCode == HttpStatus.SC_UNAUTHORIZED) {
            logout();
           // LoginActivity.startActivity(this);
            if (showToast) {
                showLongToast(R.string.notif_an_other_user_login_to_this_account);
            }
            this.finish();
            return;
        } else if (errorCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            logout();
            if (showToast) {
                showLongToast(R.string.notif_need_re_login);
            }

           // LoginActivity.startActivity(this);
            this.finish();
            return;
        } else if (errorCode == HttpStatus.SC_NOT_FOUND) {
            if (showToast) {
                showLongToast(R.string.notif_server_not_found);
            }
            return;
        } else if (errorCode == HttpStatus.SC_NOT_FOUND) {
            if (showToast) {
                showLongToast(R.string.notif_server_not_found);
            }
            return;
        } else if (errorCode == ExceptionConstant.NO_NETWORK) {
            if (showToast) {
                showLongToast(R.string.notif_no_network);
            }
            return;
        }

        if (showToast) {
            showLongToast(errorMessage);
        }
    }

    private void logout() {
        editor.clear();
        editor.commit();
    }

    public void showLongToast(int stringResourceID) {
        if (toast == null) {
            toast = Toast.makeText(this, getString(stringResourceID), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void showLongToast(String message) {

        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void showShortToast(int stringResourceID) {

        if (toast == null) {
            toast = Toast.makeText(this, getString(stringResourceID), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
