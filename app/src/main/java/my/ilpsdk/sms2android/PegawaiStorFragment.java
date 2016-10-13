package my.ilpsdk.sms2android;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilpsdk.sms2android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.ilpsdk.sms2android.Adapter.PegawaiStorAdapter;
import my.ilpsdk.sms2android.Interface.MyDialogOkClick;
import my.ilpsdk.sms2android.Interface.ServerGetJsonArray;
import my.ilpsdk.sms2android.Interface.ServerGetJsonObject;
import my.ilpsdk.sms2android.Model.KeluaranModel;
import my.ilpsdk.sms2android.Model.VolleyRequest;
import my.ilpsdk.sms2android.Util.Config;
import my.ilpsdk.sms2android.Util.Const;
import my.ilpsdk.sms2android.Util.DividerItemDecoration;
import my.ilpsdk.sms2android.Util.MyDialog;
import my.ilpsdk.sms2android.Util.MyProgressbar;

/**
 * Created by Ratan on 7/29/2015.
 */


public class PegawaiStorFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<KeluaranModel> keluaranModel;
    private KeluaranModel keluaran_click;
    private PegawaiStorAdapter pegawaiStorAdapter;
    private String TAG = PegawaiStorFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView nofound_pegawaistor;
    private Dialog setstatus;
    private EditText ed_kuantiti;
    private EditText ed_catatan;
    private TextView tv_set_status_nama_item;
    private Button btnLulus,btn_pegawaistor_retry;
    private Button btnTolak;
    private LinearLayout lay_nofound;
    private int cur_position;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pegawaistor, null);



        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_pegawaistor);
        lay_nofound = (LinearLayout) view.findViewById(R.id.lay_nofound_pegawaistor);
        lay_nofound.setVisibility(getView().GONE);

        btn_pegawaistor_retry = (Button) view.findViewById(R.id.pegawaistor_retry);

        btn_pegawaistor_retry.setOnClickListener(new View.OnClickListener() {
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
            params.put("id_bahagian", Const.id_bahagian);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        VolleyRequest.makeJsonArryReq(new Config(getContext()).getdomain()+Const.URL_GET_PEGAWAISTOR_LIST, params, new ServerGetJsonArray() {
            @Override
            public void onFinish(JSONArray data) {
                //Log.i(TAG,data.toString());
                if(data.length() > 0){
                    lay_nofound.setVisibility(getView().GONE);
                    Const.pegawaistor_list = data;
                    create_list(data);
                }
                else{
                    Const.pegawaistor_list = null;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(Const.pegawaistor_list == null){
            load_server_database();
        }
        else {
            lay_nofound.setVisibility(getView().GONE);
            create_list(Const.pegawaistor_list);
        }
        create_set_status_dialog();
    }


    private void create_list(JSONArray data) {
        JSONObject dataSet = null;


        keluaranModel = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            try {
                dataSet = data.getJSONObject(i);
                keluaranModel.add(new KeluaranModel(dataSet.getInt("id_keluar_unit"), dataSet.getInt("id_stor"),dataSet.getInt("id_pengguna_pohon"),dataSet.getString("nama"), dataSet.getString("perihal_stor"), dataSet.getString("no_kad"), dataSet.getString("unit_pengukuran"), dataSet.getString("tarikh_pohon"), dataSet.getInt("kuantiti_pohon"), dataSet.getInt("kuantiti_1"),dataSet.getString("tujuan")));
                //keluaranModel.add(new KeluaranModel(dataSet.getString("perihal_stor"),dataSet.getString("no_kad")));
            } catch (JSONException e) {
            }
        }
        /*recyclerView.setAdapter(new PegawaiStorAdapter(getActivity(),keluaranModel));
        recyclerView.invalidate();*/
        pegawaiStorAdapter = new PegawaiStorAdapter(getActivity(), keluaranModel);
        recyclerView.setAdapter(pegawaiStorAdapter);

        //pegawaiStorAdapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                keluaran_click = keluaranModel.get(position);
                cur_position = position;
                setstatus.show();
                ed_kuantiti.setText(""+keluaran_click.get_kuantiti_pohon());
                tv_set_status_nama_item.setText(keluaran_click.get_nama_item());

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void create_set_status_dialog(){

        // Create Object of Dialog class
        setstatus = new Dialog(getActivity());
        // Set GUI of login screen
        setstatus.setContentView(R.layout.dialog_set_status);
        //login.setCancelable(false);
        setstatus.setTitle("Set Status Pemohonan");

        ed_kuantiti = (EditText)setstatus.findViewById(R.id.lulus_kuantiti);
        ed_catatan = (EditText)setstatus.findViewById(R.id.catatan);
        btnLulus = (Button) setstatus.findViewById(R.id.btnLulus);
        btnTolak = (Button) setstatus.findViewById(R.id.btnTolak);
        tv_set_status_nama_item = (TextView) setstatus.findViewById(R.id.set_status_nama_item);

        // Init button of login GUI


        //ed_kuantiti.setText(""+keluaran_click.get_kuantiti_pohon());
        ed_catatan.setText("Catatan");

        // Attached listener for login GUI button
        btnLulus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kuantiti_lulus = ed_kuantiti.getText().toString().trim();
                String catatan = ed_catatan.getText().toString().trim();
                catatan = (catatan.length() <= 0)?"-":catatan;

                JSONObject params = new JSONObject();
                try {
                    params.put("id_keluar_unit", keluaran_click.get_id());
                    params.put("id_stor", keluaran_click.get_id_item());
                    params.put("kuantiti_lulus", kuantiti_lulus);
                    params.put("status_keluar", 1);
                    params.put("catatan", catatan);
                    params.put("id_pengguna_pohon", keluaran_click.get_id_pengguna_pohon());
                    params.put("id_pengguna", Const.id_pengguna);
                    params.put("nama_item", keluaran_click.get_nama_item());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final MyProgressbar myProgressbar = new MyProgressbar(getView(),"Set status pemohonan...");
                VolleyRequest.makeJsonObjReq(new Config(getContext()).getdomain()+Const.URL_SET_STATUS_PERMOHONAN, params, new ServerGetJsonObject() {
                    @Override
                    public void onFinish(JSONObject data) throws JSONException {
                        if(data.getInt("success") == 1){
                            MyDialog.msgbox(getContext(), "INFO", data.getString("msg"), new MyDialogOkClick() {
                                @Override
                                public void onOkClick() {
                                    setstatus.dismiss();
                                    Const.pegawaistor_list = removeJSONArray(Const.pegawaistor_list,cur_position);
                                    create_list(Const.pegawaistor_list);
                                }
                            });
                        }
                        else{
                            MyDialog.msgbox(getContext(), "ERROR", data.getString("msg"), new MyDialogOkClick() {
                                @Override
                                public void onOkClick() {
                                    setstatus.dismiss();

                                }
                            });
                        }
                        myProgressbar.hideProgressDialog();
                    }

                    @Override
                    public void onError(String data) {
                        myProgressbar.hideProgressDialog();
                        MyDialog.msgbox(getContext(), "ERROR", "Terdapat masalah pada rangkaian dan internet. Sila cuba lagi", null);
                    }
                });
            }
        });
        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setstatus.dismiss();

                String kuantiti_lulus = ed_kuantiti.getText().toString().trim();
                String catatan = ed_catatan.getText().toString().trim();
                catatan = (catatan.length() <= 0)?"-":catatan;
                JSONObject params = new JSONObject();
                try {
                    params.put("id_keluar_unit", keluaran_click.get_id());
                    params.put("id_stor", keluaran_click.get_id_item());
                    params.put("kuantiti_lulus", kuantiti_lulus);
                    params.put("status_keluar", 3);
                    params.put("catatan", catatan);
                    params.put("id_pengguna_pohon", keluaran_click.get_id_pengguna_pohon());
                    params.put("id_pengguna", Const.id_pengguna);
                    params.put("nama_item", keluaran_click.get_nama_item());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final MyProgressbar myProgressbar = new MyProgressbar(getView(),"Set Status Pemohonan");
                VolleyRequest.makeJsonObjReq(new Config(getContext()).getdomain()+Const.URL_SET_STATUS_PERMOHONAN, params, new ServerGetJsonObject() {
                    @Override
                    public void onFinish(JSONObject data) throws JSONException {
                        if(data.getInt("success") == 1){
                            MyDialog.msgbox(getContext(), "INFO", data.getString("msg"), new MyDialogOkClick() {
                                @Override
                                public void onOkClick() {
                                    setstatus.dismiss();
                                    Const.pegawaistor_list = removeJSONArray(Const.pegawaistor_list,cur_position);
                                    create_list(Const.pegawaistor_list);
                                }
                            });
                        }
                        else{
                            MyDialog.msgbox(getContext(), "ERROR", data.getString("msg"), new MyDialogOkClick() {
                                @Override
                                public void onOkClick() {
                                    setstatus.dismiss();

                                }
                            });
                        }
                        myProgressbar.hideProgressDialog();
                    }

                    @Override
                    public void onError(String data) {
                        myProgressbar.hideProgressDialog();
                        MyDialog.msgbox(getContext(), "ERROR", "Terdapat masalah pada rangkaian dan internet. Sila cuba lagi", null);
                    }
                });
                //finish();
            }
        });

        // Make dialog box visible.
        //setstatus.show();
    }

    public static JSONArray removeJSONArray( JSONArray jarray,int pos) {

        JSONArray Njarray=new JSONArray();
        try{
            for(int i=0;i<jarray.length();i++){
                if(i!=pos)
                    Njarray.put(jarray.get(i));
            }
        }catch (Exception e){e.printStackTrace();}
        return Njarray;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private PegawaiStorFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final PegawaiStorFragment.ClickListener clickListener) {
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
