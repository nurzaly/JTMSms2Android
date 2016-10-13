package my.ilpsdk.sms2android.Interface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nurzaly on 19/08/2016.
 */
public interface ServerGetJsonObject {
    public void onFinish(JSONObject data) throws JSONException;
    public void onError(String data);
}
