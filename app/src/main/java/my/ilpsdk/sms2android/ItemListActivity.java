package my.ilpsdk.sms2android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ilpsdk.sms2android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.ilpsdk.sms2android.Adapter.ItemAdapter;
import my.ilpsdk.sms2android.Interface.ServerGetJsonArray;
import my.ilpsdk.sms2android.Model.ItemModel;
import my.ilpsdk.sms2android.Model.VolleyRequest;
import my.ilpsdk.sms2android.Util.Config;
import my.ilpsdk.sms2android.Util.Const;
import my.ilpsdk.sms2android.Util.DividerItemDecoration;
import my.ilpsdk.sms2android.Util.MyDialog;

/**
 * Created by Nurzaly on 18/08/2016.
 */
public class ItemListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<ItemModel> itemModels, filterlist;//, ori_itemModels;
    private TextView nofound_item;
    private View view;
    private static String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = ItemListActivity.class.getSimpleName();
    private int id_bahagian = 0;
    private String kod_bahagian;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String KEY_ID_BAHAGIAN = "key_id_bahagian";
    private String KEY_KOD_BAHAGIAN = "key_kod_bahagian";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "test oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        Intent intent = getIntent();

        String nama_bahagian = intent.getStringExtra("nama_bahagian");

        id_bahagian = Const.id_bahagian_item_list;
        kod_bahagian = Const.kod_bahagian_item_list;

        mToolbar.setTitle(kod_bahagian + ": Senarai ITEM/STOK");

        //mToolbar.setTitle("STOR : " + nama_bahagian);
        view = this.findViewById(android.R.id.content).getRootView();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        nofound_item = (TextView) findViewById(R.id.nofound_item);
        nofound_item.setVisibility(view.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volleyrequest();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "test onStart");

        if(Const.item_list != null){
            nofound_item.setVisibility(view.GONE);
            create_item_list(Const.item_list);
        }
        else {
            volleyrequest();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"test onDestroy");
        //Const.item_list = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "test onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "test onPause");
    }
    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "test onRestoreInstanceState");
        id_bahagian = savedInstanceState.getInt(KEY_ID_BAHAGIAN);
        kod_bahagian = savedInstanceState.getString(KEY_KOD_BAHAGIAN);

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "test onresume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt(KEY_ID_BAHAGIAN,id_bahagian);
//        outState.putString(KEY_KOD_BAHAGIAN,kod_bahagian);
        Log.i(TAG, "test onsaveinstatnacestate");
    }

    public void volleyrequest() {
        Config config = new Config(getApplicationContext());
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        VolleyRequest.makeJsonArryReq(config.getdomain() + Const.URL_GET_ITEM + id_bahagian + "/" + config.getshowzero(), null, new ServerGetJsonArray() {
            @Override
            public void onFinish(JSONArray data) {
                if(data.length() > 0){
                    nofound_item.setVisibility(view.GONE);
                    Const.item_list = data;
                }
                else{
                    Const.item_list = null;
                    nofound_item.setVisibility(view.VISIBLE);
                }
                create_item_list(data);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(String data) {
                swipeRefreshLayout.setRefreshing(false);
                MyDialog.msgbox(ItemListActivity.this, "ERROR", "Terdapat masalah pada rangkaian dan internet. Sila cuba lagi", null);
            }
        });
    }

    private void create_item_list(JSONArray data) {
        JSONObject dataSet;

        itemModels = new ArrayList<>();
        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                try {
                    dataSet = data.getJSONObject(i);
                    itemModels.add(new ItemModel(dataSet.getInt("id_stor"), dataSet.getString("perihal_stor"), dataSet.getInt("kuantiti"), dataSet.getString("unit_pengukuran"), dataSet.getInt("no_kad"), ""));
                } catch (JSONException e) {
                }
            }
        } else {
        }

        filterlist = itemModels;
        //ori_itemModels = itemModels;

        itemAdapter = new ItemAdapter(this, filterlist);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ItemModel item = filterlist.get(position);
                Intent singleView = new Intent(getApplicationContext(), ShowItemActivity.class);
                singleView.putExtra("item_id", item.get_id());
                singleView.putExtra("item_name", item.get_name());
                singleView.putExtra("item_kuantiti", item.get_kuantiti());
                singleView.putExtra("item_unit", item.get_unit_pengukuran());
                singleView.putExtra("item_no_kad", item.get_no_kad());
                singleView.putExtra("item_id_bahagian", id_bahagian);

                startActivity(singleView);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflater.inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;

    }

   /* private void show_nonzero() {
        final List<ItemModel> filteredModelList;
        filteredModelList = shownonzero(ori_itemModels);
        ifzerolist(filteredModelList.size());
        itemModels = filteredModelList;
        itemAdapter.animateTo(filteredModelList);
        recyclerView.scrollToPosition(0);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*final List<ItemModel> filteredModelList;
        switch (item.getItemId()) {
            case R.id.show_zero:
                filteredModelList = showzero(ori_itemModels);
                ifzerolist(filteredModelList.size());
                itemModels = filteredModelList;
                itemAdapter.animateTo(filteredModelList);
                recyclerView.scrollToPosition(0);
                return true;
            case R.id.show_nonzero:
                show_nonzero();
                return true;
            case R.id.show_all:
                itemModels = ori_itemModels;
                itemAdapter.animateTo(ori_itemModels);
                recyclerView.scrollToPosition(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return true;
    }

    private void ifzerolist(int itemsize) {
        if (itemsize > 0) {
            view.findViewById(R.id.nofound_item).setVisibility(view.GONE);
        } else {
            view.findViewById(R.id.nofound_item).setVisibility(view.VISIBLE);
        }

    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }*/

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<ItemModel> filteredModelList = filter(itemModels, query);
        ifzerolist(filteredModelList.size());
        itemAdapter.animateTo(filteredModelList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    private List<ItemModel> filter(List<ItemModel> models, String query) {
        query = query.toLowerCase();

        final List<ItemModel> filteredModelList = new ArrayList<>();
        for (ItemModel model : models) {
            final String text = model.get_name().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        filterlist = filteredModelList;

        return filteredModelList;
    }

    private List<ItemModel> showzero(List<ItemModel> models) {
        final List<ItemModel> filteredModelList = new ArrayList<>();
        for (ItemModel model : models) {
            final int kuantiti = model.get_kuantiti();
            if (kuantiti == 0) {
                filteredModelList.add(model);
            }
        }
        filterlist = filteredModelList;

        return filteredModelList;
    }

    private List<ItemModel> shownonzero(List<ItemModel> models) {
        final List<ItemModel> filteredModelList = new ArrayList<>();
        for (ItemModel model : models) {
            final int kuantiti = model.get_kuantiti();
            if (kuantiti != 0) {
                filteredModelList.add(model);
            }
        }
        filterlist = filteredModelList;

        return filteredModelList;
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
