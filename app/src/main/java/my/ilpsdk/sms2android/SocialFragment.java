package my.ilpsdk.sms2android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilpsdk.sms2android.R;

import java.util.List;

import my.ilpsdk.sms2android.Model.StorModel;

/**
 * Created by Ratan on 7/29/2015.
 */
public class SocialFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    //private ExampleAdapter mAdapter;
    private List<StorModel> mModels,clicklist;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.social_layout,null);

        pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        //mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        return view;
    }


}
