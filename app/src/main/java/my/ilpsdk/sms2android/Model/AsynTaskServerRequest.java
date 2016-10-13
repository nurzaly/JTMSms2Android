package my.ilpsdk.sms2android.Model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.ilpsdk.sms2android.Interface.MyServerReqListener;
import my.ilpsdk.sms2android.Util.Const;
import my.ilpsdk.sms2android.Util.JSONParser;

/**
 * Created by Nurzaly on 23/08/2016.
 */
public class AsynTaskServerRequest {
    Context context;
    MyServerReqListener callback;
    String url;

    public AsynTaskServerRequest(Context context, String url, MyServerReqListener callback){
        this.context = context;
        this.callback = callback;
        this.url = url;
        new GetData().execute();
    }

    public class GetData extends AsyncTask<String, String, String> {
        JSONObject json;
        ProgressDialog pDialog;
        private String TAG = AsynTaskServerRequest.class.getSimpleName();


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading data from server. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            JSONParser jsonParser = new JSONParser();


        }

        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            JSONParser jsonParser = new JSONParser();
            try {
                int success = 0;
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                Log.i(TAG,"test get request from " + url);
                json = jsonParser.makeHttpRequest(url, "GET", params);
                onPostExecute(json);
                //Log.i(TAG,"test "+url+" "+json.toString());

                //DatabaseHandler db = new DatabaseHandler(context);


            } catch (Exception e) {
            }

            return null;
        }

        protected void onPostExecute(JSONObject request) {
            pDialog.dismiss();
            callback.onfinish(request);
        }
    }
}
