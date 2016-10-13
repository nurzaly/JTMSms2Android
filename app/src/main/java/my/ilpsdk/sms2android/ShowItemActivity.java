package my.ilpsdk.sms2android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ilpsdk.sms2android.R;

import org.json.JSONException;
import org.json.JSONObject;

import my.ilpsdk.sms2android.App.AppController;
import my.ilpsdk.sms2android.Interface.MyDialogOkClick;
import my.ilpsdk.sms2android.Interface.ServerGetJsonObject;
import my.ilpsdk.sms2android.Model.VolleyRequest;
import my.ilpsdk.sms2android.Util.Config;
import my.ilpsdk.sms2android.Util.Const;
import my.ilpsdk.sms2android.Util.MyDialog;
import my.ilpsdk.sms2android.Util.MyProgressbar;

/**
 * Created by Nurzaly on 02/08/2016.
 */
public class ShowItemActivity extends AppCompatActivity {

    private String TAG = ShowItemActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private ImageView imageView;
    private NetworkImageView imageNetworkView;
    private String url;
    private EditText ed_kuantiti,ed_tujuan;
    private Button btn_hantar;
    private ProgressBar progressBar;
    private View view;
    private TextView tv_toolbar;
    private int item_id,item_kuantiti,item_id_bahagian = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tv_item_kuantiti = (TextView)findViewById(R.id.item_kuantiti);
        TextView tv_item_no_kad = (TextView)findViewById(R.id.item_no_kad);
        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);

        ed_kuantiti = (EditText)findViewById(R.id.ed_kuantiti);
        ed_tujuan = (EditText)findViewById(R.id.ed_tujuan);
        btn_hantar = (Button)findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

//        ed_kuantiti.setText("1");
//        ed_tujuan.setText("development test");
        //TextView tv_item_no_kod = (TextView)findViewById(R.id.item_no_kod);




        imageView = (ImageView) findViewById(R.id.imageView);
        imageNetworkView = (NetworkImageView) findViewById(R.id.imgNetwork);

        Intent intent = getIntent();
        item_id = intent.getIntExtra("item_id",0);
        item_kuantiti = intent.getIntExtra("item_kuantiti",0);
        item_id_bahagian = intent.getIntExtra("item_id_bahagian",0);

        String item_name = intent.getStringExtra("item_name");
        String item_unit = intent.getStringExtra("item_unit");

        //getSupportActionBar().setTitle(item_name);

        if(item_kuantiti == 0){
            ed_kuantiti.setVisibility(view.GONE);
            ed_tujuan.setVisibility(view.GONE);
            ed_tujuan.setVisibility(view.GONE);
            btn_hantar.setVisibility(view.GONE);
        }
        //String item_no_kod = intent.getStringExtra("item_no_kod");



        tv_toolbar.setText(item_name);
        tv_item_kuantiti.setText(item_kuantiti + " " + item_unit);
        //tv_item_no_kad.setText(String.valueOf(item_no_kad));
        //tv_item_no_kod.setText(item_no_kod);

        url = Const.URL_IMAGE + String.valueOf(item_id) + ".jpg";
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageNetworkView.setImageUrl(url, imageLoader);
        progressBar.setVisibility(view.GONE);

        /*VolleyRequest server = new VolleyRequest(view);
        server.makeImageRequest(url, new ServerGetImage() {
            @Override
            public void onfinish(ImageLoader.ImageContainer response) {
                imageView.setImageBitmap(response.getBitmap());
            }
        });*/
        //makeImageRequest(item_id);

    }
    public void hantar(View v){

        SharedPreferences myconfig = getSharedPreferences("myconfig",0);
        //int id_bahagian = myconfig.getInt("id_bahagian")

        String kuantiti = ed_kuantiti.getText().toString();
        String tujuan = ed_tujuan.getText().toString();

        if(kuantiti.trim().length() <= 0 || kuantiti == "0" || tujuan.trim().length() <= 0){
            MyDialog.msgbox(ShowItemActivity.this,"ERROR","Kuantiti dan tujuan pemohonan mesti diisi",null);
        }else{

            MyDialog.msgbox2(ShowItemActivity.this, "CONFIRMATION", "Adakah anda ingin menghantar pemohonan ini", new MyDialogOkClick() {
                @Override
                public void onOkClick() {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("item_id",String.valueOf(item_id));
                        params.put("id_bahagian",item_id_bahagian);
                        params.put("id_pengguna",String.valueOf(Const.id_pengguna));
                        params.put("kuantiti", ed_kuantiti.getText().toString().trim());
                        params.put("tujuan", ed_tujuan.getText().toString().trim());

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    save_data(new Config(getApplicationContext()).getdomain()+Const.URL_SEND_PEMOHONAN, params);
                }
            });


        }


    }

    private void save_data(String url, JSONObject params){
        final MyProgressbar myProgressbar = new MyProgressbar(findViewById(android.R.id.content).getRootView(),"Hantar Pemohonan...");
        VolleyRequest.makeJsonObjReq(url, params, new ServerGetJsonObject() {
            @Override
            public void onFinish(JSONObject data) {
                Log.i(TAG,"test " + data.toString());
                try {
                    if(data.getInt("success") == 1){
                        MyDialog.msgbox(ShowItemActivity.this, "INFO", "Pemohonan anda telah berjaya dihantar", new MyDialogOkClick() {
                            @Override
                            public void onOkClick() {
                                ed_tujuan.setText("");
                                ed_kuantiti.setText("");
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myProgressbar.hideProgressDialog();
            }

            @Override
            public void onError(String data) {
                myProgressbar.hideProgressDialog();
                MyDialog.msgbox(ShowItemActivity.this, "ERROR", "Terdapat masalah pada rangkaian dan internet. Sila cuba lagi", null);
            }
        });
    }

    public void kembali(View view){
        finish();
    }


}
