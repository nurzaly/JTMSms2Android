package my.ilpsdk.sms2android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ilpsdk.sms2android.R;

import my.ilpsdk.sms2android.Interface.Mycallback;
import my.ilpsdk.sms2android.Model.Staf;
import my.ilpsdk.sms2android.Util.Const;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    public Toolbar mToolbar;
    String TAG = MainActivity.class.getSimpleName();
    SharedPreferences preferences;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        view = this.findViewById(android.R.id.content).getRootView();
        PreferenceManager.setDefaultValues(this, R.xml.my_pref, false);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);

        load_first_fragment();
        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_item_sent) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new SentFragment()).commit();

                }

                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

                /*android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
                ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

                mDrawerLayout.setDrawerListener(mDrawerToggle);

                mDrawerToggle.syncState();*/


        //get_data();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflater.inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this,SettingActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void load_first_fragment() {
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        SharedPreferences myconfig = getSharedPreferences(Const.MYCONFIG, 0);
        if (Const.noic.length() <= 0) {
            if (myconfig.contains("noic")) {
                String noic = myconfig.getString("noic", "default");
                if (noic != "default" && noic.length() > 0) {
                    Staf.setconfig(noic, MainActivity.this, new Mycallback() {
                        @Override
                        public void custom_function() {
                            show_tab_fragment();
                        }
                    });
                } else {
                    show_login_fragment();
                }
            } else {
                show_login_fragment();
            }
        } else {
            Staf.setconfig(Const.noic, MainActivity.this, new Mycallback() {
                @Override
                public void custom_function() {
                    show_tab_fragment();
                }
            });
        }


    }

    public void show_tab_fragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
    }

    public void show_login_fragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new StafLoginFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "test onDestroy");
        //Const.status_list = null;
        Const.item_list = null;
    }
}