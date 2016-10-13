package my.ilpsdk.sms2android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilpsdk.sms2android.R;

import my.ilpsdk.sms2android.Util.Const;

/**
 * Created by Ratan on 7/27/2015.
 */
public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 0;
    private String TAG = TabFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

        int_items = (Const.group_pengguna <= 4 && Const.group_pengguna != 0) ? 3 : 2;
            View x =  inflater.inflate(R.layout.tab_layout,null);
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                   }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0 : ((MainActivity)getActivity()).mToolbar.setSubtitle("SMS2 Status Pemohonan");break;
                    case 1 : ((MainActivity)getActivity()).mToolbar.setSubtitle("SMS2 Senarai Stor");break;
                    case 2 : ((MainActivity)getActivity()).mToolbar.setSubtitle("SMS2 Senarai Pemohonan");break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return x;

    }


    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
          switch (position){
              case 0 :{return new StatusListFragment();}
              case 1 :{return new StorListFragment();}
              case 2 :{return new PegawaiStorFragment();}

          }
        return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }


        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Status Pemohonan";
                case 1 :
                    return "Pemohonan Baru";
                case 2 :
                    return "Pegawai Stor";
                case 3 :
                    return "Mekanikal";
            }
                return null;
        }
    }

}
