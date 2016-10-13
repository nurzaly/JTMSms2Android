package my.ilpsdk.sms2android.Interface;

import org.json.JSONArray;

/**
 * Created by Nurzaly on 19/08/2016.
 */
public interface ServerGetJsonArray {
    public void onFinish(JSONArray data);
    public void onError(String data);
}
