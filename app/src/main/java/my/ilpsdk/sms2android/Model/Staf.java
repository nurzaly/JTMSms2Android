package my.ilpsdk.sms2android.Model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import my.ilpsdk.sms2android.Interface.MyDialogOkClick;
import my.ilpsdk.sms2android.Interface.Mycallback;
import my.ilpsdk.sms2android.Interface.ServerGetJsonObject;
import my.ilpsdk.sms2android.MainActivity;
import my.ilpsdk.sms2android.Util.Config;
import my.ilpsdk.sms2android.Util.Const;
import my.ilpsdk.sms2android.Util.MyDialog;
import my.ilpsdk.sms2android.Util.MyProgressbar;

/**
 * Created by Nurzaly on 29/07/2016.
 */
public class Staf {
    private static String TAG = Staf.class.getSimpleName();
    public Staf() {
    }

    public static void setconfig(final String noic, final Context context, final Mycallback callback){
        View view  = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        JSONObject params = new JSONObject();
        try {
            params.put("uname", noic);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        final MyProgressbar myProgressbar = new MyProgressbar(view,"Pengesahan pengguna");
        VolleyRequest.makeJsonObjReq(new Config(context).getdomain()+Const.URL_GET_STAF_DATA, params, new ServerGetJsonObject() {
            @Override
            public void onFinish(JSONObject data) {
                Log.i(TAG,"test" + data.toString());

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
                        group = jsonObject.getInt("group");
                        nama_pengguna = jsonObject.getString("nama");

                        if(block_pengguna != 1){
                            MyDialog.msgbox(context,"ALERT","Akaun anda dihalang daripada menggunakan SMS2",null);
                            ((MainActivity)context).show_login_fragment();
                        }
                        else{

                            Const.id_bahagian = id_bahagian;
                            Const.id_pengguna = id_pengguna;
                            Const.id_jabatan = id_jabatan;
                            Const.group_pengguna = group;

                            SharedPreferences myconfig = context.getSharedPreferences(Const.MYCONFIG,0);
                            SharedPreferences.Editor editor = myconfig.edit();
                            editor.putString("noic",noic);

                            // Commit the edits!
                            editor.commit();

                            callback.custom_function();
                        }
                    }
                    else{
                        MyDialog.msgbox(context,"ERROR","Kesalahan pada Sms2 Staf Config",null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myProgressbar.hideProgressDialog();
            }

            @Override
            public void onError(String data) {
                myProgressbar.hideProgressDialog();
                MyDialog.msgbox(context, "ERROR", "Terdapat masalah pada rangkaian dan internet. Sila cuba lagi", new MyDialogOkClick() {
                    @Override
                    public void onOkClick() {
                        ((MainActivity) context).show_login_fragment();
                    }
                });
            }
        });
    }
}
