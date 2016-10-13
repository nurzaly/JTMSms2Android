package my.ilpsdk.sms2android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilpsdk.sms2android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.ilpsdk.sms2android.Adapter.StatusAdapter;
import my.ilpsdk.sms2android.Interface.ServerGetJsonArray;
import my.ilpsdk.sms2android.Model.KeluaranModel;
import my.ilpsdk.sms2android.Model.VolleyRequest;
import my.ilpsdk.sms2android.Util.Config;
import my.ilpsdk.sms2android.Util.Const;
import my.ilpsdk.sms2android.Util.DividerItemDecoration;
import my.ilpsdk.sms2android.Util.MyDialog;

/**
 * Created by Ratan on 7/29/2015.
 */


public class StatusListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<KeluaranModel> keluaranModel;
    private StatusAdapter statusAdapter;
    private String TAG = StatusListFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView nofound;
    private Button retry;
    private LinearLayout lay_nofound;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_status_list, null);
        Log.i(TAG,"onCreateView");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_status);
        lay_nofound = (LinearLayout) view.findViewById(R.id.lay_nofound);
        nofound = (TextView) view.findViewById(R.id.nofound);

        lay_nofound.setVisibility(getView().GONE);

        retry = (Button) view.findViewById(R.id.btn_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_server_database();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load_server_database();
            }
        });

        return view;
    }

    private void load_server_database() {

        //VolleyRequest server = new VolleyRequest(view);
        JSONObject params = new JSONObject();
        try {
            params.put("id_pengguna", Const.id_pengguna);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        VolleyRequest.makeJsonArryReq(new Config(getContext()).getdomain() + Const.URL_GET_STATUS_LIST, params, new ServerGetJsonArray() {
            @Override
            public void onFinish(JSONArray data) {
                //Log.i(TAG,data.toString());
                if(data.length() > 0){
                    lay_nofound.setVisibility(getView().GONE);
                    Const.status_list = data;
                    create_list(data);
                }
                else{
                    Const.status_list = null;
                    lay_nofound.setVisibility(getView().VISIBLE);
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(String data) {
                swipeRefreshLayout.setRefreshing(false);
                MyDialog.msgbox(getContext(), "ERROR", "Terdapat masalah pada rangkaian dan internet. Sila cuba lagi", null);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"test onSaveInstanceState");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"test onResume");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        if(Const.status_list == null) {
            load_server_database();
        }
        else{
            lay_nofound.setVisibility(getView().GONE);
            create_list(Const.status_list);
        }
    }


    private void create_list(JSONArray data) {
        JSONObject dataSet = null;


        keluaranModel = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            try {
                dataSet = data.getJSONObject(i);
                keluaranModel.add(new KeluaranModel(dataSet.getString("perihal_stor"), dataSet.getString("no_kad"), dataSet.getString("unit_pengukuran"), dataSet.getString("tarikh_pohon"), dataSet.getInt("kuantiti_pohon"), dataSet.getString("kuantiti_lulus"), dataSet.getString("status_keluar")));
                //keluaranModel.add(new KeluaranModel(dataSet.getString("perihal_stor"),dataSet.getString("no_kad")));
            } catch (JSONException e) {
            }
        }
        /*recyclerView.setAdapter(new StatusAdapter(getActivity(),keluaranModel));
        recyclerView.invalidate();*/
        recyclerView.removeAllViews();
        statusAdapter = new StatusAdapter(getActivity(), keluaranModel);
        recyclerView.setAdapter(statusAdapter);
        recyclerView.invalidate();


        //statusAdapter.notifyDataSetChanged();
        /*recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //KeluaranModel statusmodel = keluaranModel.get(position);
                //Toast.makeText(getContext(), stor.get_nama_bahagian() + " is selected!", Toast.LENGTH_SHORT).show();
                //Intent singleView = new Intent(getContext(), ItemListActivity.class);
                //singleView.putExtra("id_bahagian", stor.get_id_bahagian());
                //singleView.putExtra("nama_bahagian", stor.get_nama_bahagian());
                //Bundle bundle  = new Bundle();
                //bundle.putParcelable("stor",stor);
                //singleView.putExtras(bundle);
                //startActivity(singleView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private StatusListFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final StatusListFragment.ClickListener clickListener) {
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
