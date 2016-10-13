package my.ilpsdk.sms2android;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ilpsdk.sms2android.R;

/**
 * Created by Nurzaly on 26/08/2016.
 */
public class SettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.my_pref);
    }
}
