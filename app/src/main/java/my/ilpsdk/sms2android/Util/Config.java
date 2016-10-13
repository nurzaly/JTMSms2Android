package my.ilpsdk.sms2android.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Nurzaly on 26/08/2016.
 */
public class Config {
    //private Context context;
    private SharedPreferences sharedPreferences;
    public Config(Context context){
        //this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }
    public String getdomain(){
        return sharedPreferences.getString("ip_address",Const.DOMAIN);
    }
    public boolean getshowzero(){
        return sharedPreferences.getBoolean("zero_kuantiti",false);
    }
}
