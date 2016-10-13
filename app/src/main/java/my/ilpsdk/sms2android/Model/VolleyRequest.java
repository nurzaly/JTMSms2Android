package my.ilpsdk.sms2android.Model;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import my.ilpsdk.sms2android.Interface.ServerGetImage;
import my.ilpsdk.sms2android.Interface.ServerGetJsonArray;
import my.ilpsdk.sms2android.Interface.ServerGetJsonObject;
import my.ilpsdk.sms2android.App.AppController;

/**
 * Created by Nurzaly on 18/08/2016.
 */

public class VolleyRequest {


    private static String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private static String TAG = VolleyRequest.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;


    public static void makeJsonArryReq(final String url, final JSONObject params, final ServerGetJsonArray callback){
        //showProgressDialog();
        Log.i(TAG,"GET JSON ARRAY REQUEST " + url);
        JsonArrayRequest req = new JsonArrayRequest(url,params,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG,response.toString());
                        //makelist(response);
                        //hideProgressDialog();
                        callback.onFinish(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"ERROR JSON ARRAY REQUEST" + url + " -> "+ params);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                callback.onError(tag_json_arry);
                //hideProgressDialog();
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        // Cancelling request
        //ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }

    public static void makeJsonObjReq(final String url, final JSONObject params, final ServerGetJsonObject callback) {
        //showProgressDialog();
        Log.i(TAG,"GET JSON OBJECT REQUEST " + url + " " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onFinish(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"ERROR JSON OBJECT REQUEST " + url + " -> "+ params);
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                callback.onError(tag_json_obj);
                //hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);




        // Cancelling request
        //AppController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    public static void makeImageRequest(final String url, final ServerGetImage callback) {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        // If you are using NetworkImageView
        //imgNetWorkView.setImageUrl(Const.URL_IMAGE, imageLoader);



        // If you are using normal ImageView
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage() + url);
                //Toast.makeText(getApplicationContext(),"Error load image. Image not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    callback.onfinish(response);
                    Log.d(TAG, "Image Load Error: " + url);
                }
            }
        });

        // Loading image with placeholder and error image
        /*imageLoader.get(Const.URL_IMAGE, ImageLoader.getImageListener(
                imageView, R.drawable.ico_loading, R.drawable.ico_error));

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Const.URL_IMAGE);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            // cached response doesn't exists. Make a network call here
        }*/

    }
}
