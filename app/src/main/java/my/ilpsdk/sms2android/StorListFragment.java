package my.ilpsdk.sms2android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ilpsdk.sms2android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.ilpsdk.sms2android.Adapter.StorAdapter;
import my.ilpsdk.sms2android.Interface.ServerGetJsonArray;
import my.ilpsdk.sms2android.Model.StorModel;
import my.ilpsdk.sms2android.Model.VolleyRequest;
import my.ilpsdk.sms2android.Util.Config;
import my.ilpsdk.sms2android.Util.Const;
import my.ilpsdk.sms2android.Util.DividerItemDecoration;

/**
 * Created by Ratan on 7/29/2015.
 */



public class StorListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<StorModel> storModels;
    private StorAdapter storAdapter;
    private Parcelable mListState;
    private LinearLayoutManager linearLayoutManager;
    private String TAG = StorListFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_stor_list,null);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //final VolleyRequest volleyServerRequest = new VolleyRequest(view,TAG);
        VolleyRequest.makeJsonArryReq(new Config(getContext()).getdomain()+Const.URL_GET_STOR, null, new ServerGetJsonArray() {
            @Override
            public void onFinish(JSONArray data) {
                //Log.i(TAG,"test done load stor list");
                create_stor_list(data);
                //volleyServerRequest.hideProgressDialog();
            }

            @Override
            public void onError(String data) {
            }
        });
    }

    private void create_stor_list(JSONArray data) {
        JSONObject dataSet = null;

        storModels = new ArrayList<>();
        for (int i=0;i < data.length();i++) {
            try{
                dataSet = data.getJSONObject(i);
                //Log.d("TEST",String.valueOf(dataSet.getInt("id_bahagian")));
                storModels.add(new StorModel(dataSet.getInt("id_bahagian"), dataSet.getString("nama_jabatan"),dataSet.getString("nama_bahagian"),dataSet.getString("kod_bahagian")));
            }
            catch (JSONException e){}
        }

        storAdapter = new StorAdapter(getActivity(),storModels);
        recyclerView.setAdapter(storAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                StorModel stor = storModels.get(position);
                //Toast.makeText(getContext(), stor.get_nama_bahagian() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent singleView = new Intent(getContext(), ItemListActivity.class);
                //singleView.putExtra("id_bahagian", stor.get_id_bahagian());
                singleView.putExtra("nama_bahagian", stor.get_nama_bahagian());
                //singleView.putExtra("kod_bahagian", stor.get_kod_bahagian());
                Const.id_bahagian_item_list = stor.get_id_bahagian();
                Const.kod_bahagian_item_list = stor.get_kod_bahagian();

                //Bundle bundle  = new Bundle();
                //bundle.putParcelable("stor",stor);
                //singleView.putExtras(bundle);
                startActivity(singleView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private StorListFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final StorListFragment.ClickListener clickListener) {
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

    @Override
    public void onStart() {
        super.onStart();
        Const.item_list = null;
    }
}
