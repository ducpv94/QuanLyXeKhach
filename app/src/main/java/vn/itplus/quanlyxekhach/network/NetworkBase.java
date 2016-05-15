package vn.itplus.quanlyxekhach.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.exception.ExceptionConstant;
import vn.itplus.quanlyxekhach.exception.VolleyErrorHelper;
import vn.itplus.quanlyxekhach.util.Constant;
import vn.itplus.quanlyxekhach.util.MySingleton;
import vn.itplus.quanlyxekhach.util.Utilities;

/**
 * Created by AnhlaMrDuc on 14-May-16.
 */
public abstract class NetworkBase<T extends Object> {

    private static final int NETWORK_TIME_OUT_DEFAULT = 30000;
    private static final int NETWORK_TIME_OUT_SMALL = 2000;
    private int networkTimeout;
    private static final String CODE = "code";
    private static final String MESSAGE = "message";
    private Request<T> mRequest;
    private Context mContext;
    private String tokenAccess;
    private RequestQueue mRequestQueue;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private JSONObject jsonObjectBody;

    private boolean needToCheckUpdateInHistory = true;

    protected NetworkBase(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(Constant.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        tokenAccess = sharedPreferences.getString(Constant.ACCESS_TOKEN_DRIVER, "");
        this.networkTimeout = NETWORK_TIME_OUT_DEFAULT;
    }

    public final void request(final Response.Listener<T> listener, final ErrorListener errorListener) {
        jsonObjectBody = null;
        try {
            jsonObjectBody = genBodyParam();
        } catch (JSONException e) {
            e.printStackTrace();
            VolleyErrorHelper volleyErrorHelper = new VolleyErrorHelper(e.getMessage(), ExceptionConstant.ERROR_CODE_PARSE_JSON);
            errorListener.onErrorListener(volleyErrorHelper.getErrorCode(), volleyErrorHelper.getMessageError());
            return;
        }

        mRequest = new Request<T>(genMethod(), genURL(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 * if volleyError is a instance of VolleyErrorHelper ->error from sever
                 * else create new VolleyErrorHelper to handle error from Volley
                 */
                VolleyErrorHelper volleyErrorHelper;
                if (volleyError instanceof VolleyErrorHelper) {
                    volleyErrorHelper = (VolleyErrorHelper) volleyError;
                } else {
                    volleyErrorHelper = new VolleyErrorHelper(volleyError);
                }
                errorListener.onErrorListener(volleyErrorHelper.getErrorCode(), volleyErrorHelper.getMessageError());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("mobile-session-id", tokenAccess);
                headers.put("os", "" + 2);

                Log.e("NetworkBase", "date : " + getTodayDate());
                headers.put("vn-date", getTodayDate());

                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                if (null == jsonObjectBody) {
                    Log.e("NetworkBase", "request with no parameter");
                    return "".getBytes();
                } else {
                    Log.e("NetworkBase", "json = " + jsonObjectBody.toString());
                    byte[] json = jsonObjectBody.toString().getBytes();
                    return json;
                }
            }


            @SuppressLint("LongLogTag")
            @Override
            protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
                String data = new String(networkResponse.data);
                Log.e("NetworkBase -> parse NetworkBase", "" + data);

                T result = null;
                VolleyErrorHelper volleyErrorHelper;
                try {
                    volleyErrorHelper = validData(data);
                    if (null == volleyErrorHelper) {
                        result = genDataFromJSON(data);
                        return Response.success(result, getCacheEntry());
                    } else {
                        return Response.error(volleyErrorHelper);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    volleyErrorHelper = new VolleyErrorHelper(" Error code parser json ", ExceptionConstant.ERROR_CODE_PARSE_JSON);
                    return Response.error(volleyErrorHelper);
                } catch (VolleyErrorHelper noOrder) {
                    noOrder.printStackTrace();
                    return Response.error(noOrder);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    volleyErrorHelper = new VolleyErrorHelper(e.getMessage(), ExceptionConstant.ERROR_WHEN_CLONE_OBJECT);
                    return Response.error(volleyErrorHelper);
                }
            }

            @Override
            protected void deliverResponse(T data) {
                listener.onResponse(data);
            }
        };
        mRequestQueue = MySingleton.getInstance(mContext).getRequestQueue();
        mRequest.setRetryPolicy(new DefaultRetryPolicy(networkTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mRequest);
    }

    protected abstract T genDataFromJSON(String data) throws JSONException, VolleyErrorHelper, CloneNotSupportedException;

    private VolleyErrorHelper validData(String json) {
        if (json.equals("OK")) {
            return null;
        }
        VolleyErrorHelper volleyErrorHelper = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(CODE)) {
                int code = jsonObject.getInt(CODE);
                if (code != 0) {
                    String message = jsonObject.getString(MESSAGE);
                    volleyErrorHelper = new VolleyErrorHelper(message, code);
                }
            } else {
                if (jsonObject.has(MESSAGE)) {
                    volleyErrorHelper = new VolleyErrorHelper(jsonObject.getString(MESSAGE), ExceptionConstant.ERROR_FROM_SERVER);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            volleyErrorHelper = new VolleyErrorHelper(e.getMessage(), ExceptionConstant.ERROR_CODE_PARSE_JSON);
        }
        return volleyErrorHelper;
    }


    public abstract JSONObject genBodyParam() throws JSONException;

    public abstract String genURL();

    public abstract int genMethod();

    public void cancelRequest() {
        if (null != mRequest) {
            mRequest.cancel();
        }
    }

    public interface ErrorListener {
        void onErrorListener(int errorCode, String errorMessage);
    }



    protected void changeToSmallNetworkTimeout() {
        this.networkTimeout = NETWORK_TIME_OUT_SMALL;
    }

    public void setNeedToCheckUpdateInHistory(boolean needToCheckUpdateInHistory) {
        this.needToCheckUpdateInHistory = needToCheckUpdateInHistory;
    }

    protected String getTodayDate() {
        Date currentDate = Calendar.getInstance().getTime();
        String dateString = sharedPreferences.getString(Constant.TODAY_DATE,
                Utilities.getDateString(mContext.getString(R.string.format_date_send_server), currentDate));
        return dateString;
    }

}
