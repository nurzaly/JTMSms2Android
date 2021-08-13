package my.ilpsdk.sms2android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ilpsdk.sms2android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import my.ilpsdk.sms2android.Interface.MyDialogOkClick;
import my.ilpsdk.sms2android.Interface.ServerGetJsonObject;
import my.ilpsdk.sms2android.Model.VolleyRequest;
import my.ilpsdk.sms2android.Util.Config;
import my.ilpsdk.sms2android.Util.Const;
import my.ilpsdk.sms2android.Util.MyDialog;
import my.ilpsdk.sms2android.Util.MyProgressbar;

/**
 * Created by Ravi on 29/07/15.
 */
public class StafLoginFragment extends Fragment implements View.OnClickListener{

    private String TAG = StafLoginFragment.class.getSimpleName();
    private EditText edUname, edPword;
    private String uname, pword;
    public Button btnLogin;


    public StafLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_staf_login, container, false);

        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edUname = (EditText) view.findViewById(R.id.etUsername);
        edPword = (EditText) view.findViewById(R.id.etPassword);
        /*edUname.setText("841017125843");
        edPword.setText("123456");*/

        ((MainActivity)getActivity()).mToolbar.setTitle("ILPSDK SMS2 Login");

    }

    @Override
    public void onClick(View v) {
        uname = edUname.getText().toString().trim();
        pword = edPword.getText().toString().trim();



        if(uname.trim().length() == 0 || pword.trim().length() == 0){
            Snackbar.make(v,"Username And Password is empty", Snackbar.LENGTH_SHORT).show();
        }
        else{

            JSONObject params = new JSONObject();
            try {
                params.put("uname", uname);
                params.put("pword", pword);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            login(Const.URL_LOGIN,params);
        }


    }

    private void login(String url, JSONObject params){
        final MyProgressbar myProgressbar = new MyProgressbar(getView(),"Pengesahan MY STAF ED");
        VolleyRequest.makeJsonObjReq(url, params, new ServerGetJsonObject() {
            @Override
            public void onFinish(JSONObject data) {
                Log.i(TAG,data.toString());
                try {
                    if(data.getInt("success") == 1){
                        /*Staf.setconfig(uname, getContext(), new Mycallback() {
                            @Override
                            public void custom_function() {
                                ((MainActivity)getActivity()).show_tab_fragment();
                            }
                        });*/
                        myProgressbar.hideProgressDialog();
                        get_staf_config();
                    }
                    else{
                        myProgressbar.hideProgressDialog();
                        MyDialog.msgbox(getContext(),"ALERT","Kesalahan pada ID atau Password",null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String data) {
                myProgressbar.hideProgressDialog();
                MyDialog.msgbox(getContext(), "ERROR", "Terdapat masalah pada rangkaian dan internet. Sila cuba lagi", null);
            }
        });
    }

    private void get_staf_config() {
        JSONObject params = new JSONObject();
        try {
            params.put("uname", uname);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        final MyProgressbar myProgressbar = new MyProgressbar(getView(),"Pengesahan Sms2...");
        VolleyRequest.makeJsonObjReq(new Config(getContext()).getdomain()+Const.URL_GET_STAF_DATA, params, new ServerGetJsonObject() {
            @Override
            public void onFinish(JSONObject data) {
                Log.i(TAG,data.toString());

                int id_pengguna,id_bahagian,block_pengguna,id_jabatan,group = 0;
                String nama_pengguna;

                try {
                    if (data.getInt("success") == 1){
                        JSONArray jsonArray = data.getJSONArray("data");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        id_pengguna = jsonObject.getInt("id_pengguna");
                        id_bahagian = jsonObject.getInt("bahagian");
                        id_jabatan = jsonObject.getInt("jabatan");
                        block_pengguna = jsonObject.getInt("block_pengguna");
                        nama_pengguna = jsonObject.getString("nama");
                        group = jsonObject.getInt("group");

                        if(block_pengguna != 1){
                            MyDialog.msgbox(getContext(),"ALERT","Akaun anda dihalang daripada menggunakan SMS2",null);
                        }
                        else{

                            Const.id_bahagian = id_bahagian;
                            Const.id_pengguna = id_pengguna;
                            Const.id_jabatan = id_jabatan;
                            Const.noic = uname;
                            Const.group_pengguna = group;

                            SharedPreferences myconfig = getActivity().getSharedPreferences(Const.MYCONFIG,0);
                            SharedPreferences.Editor editor = myconfig.edit();
                            editor.putInt("id_pengguna", id_pengguna);
                            editor.putInt("id_bahagian", id_bahagian);
                            editor.putString("noic",uname);

                            // Commit the edits!
                            editor.commit();
                            MyDialog.msgbox(getContext(), "MESSAGE", "Selamat Datang \n" + nama_pengguna, new MyDialogOkClick() {
                                @Override
                                public void onOkClick() {
                                    ((MainActivity)getActivity()).show_tab_fragment();
                                }
                            });
                        }
                    }
                    else{
                        MyDialog.msgbox(getContext(),"ERROR","Kesalahan pada Sms2 Staf Config",null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
