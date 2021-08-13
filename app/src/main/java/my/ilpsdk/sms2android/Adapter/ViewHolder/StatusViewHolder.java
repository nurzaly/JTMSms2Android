package my.ilpsdk.sms2android.Adapter.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ilpsdk.sms2android.R;

import my.ilpsdk.sms2android.Model.KeluaranModel;
import my.ilpsdk.sms2android.Util.WordsCapitalizer;


public class StatusViewHolder extends RecyclerView.ViewHolder {

    private final TextView tv_name, tv_status_details, tv_status;
    private String TAG = StatusViewHolder.class.getSimpleName();
    private String status,status2,tarikh,year,month,date;
    private View view;

    public StatusViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;

        tv_name = (TextView) itemView.findViewById(R.id.status_item_name);
        tv_status_details = (TextView) itemView.findViewById(R.id.status_details);
        tv_status = (TextView) itemView.findViewById(R.id.status);
    }

    public void bind(KeluaranModel model) {
        //String textWord = WordsCapitalizer.capitalizeEveryWord(model.get_nama_jabatan());
        //tvHidden_id.setText(model.get_listid());
        year = model.get_tarikh_pohon().split("-")[0];
        month = model.get_tarikh_pohon().split("-")[1];
        date = model.get_tarikh_pohon().split("-")[2];
        tarikh = date+"-"+month+"-"+year;


        status = model.get_status();
        if(status.equals("1")){
            status2 = "L";
            view.findViewById(R.id.status).setBackgroundResource(R.drawable.lulus_style);
        }
        else if (status.equals("3")){
            status2 = "T";
            view.findViewById(R.id.status).setBackgroundResource(R.drawable.tolak_style);
        }
        else{
            status2 = "P";
            view.findViewById(R.id.status).setBackgroundResource(R.drawable.proses_style);
        }

        //status2 = new String("Belum Lulus");

        tv_name.setText(WordsCapitalizer.capitalizeEveryWord(model.get_nama_item()));
        tv_status.setText(status2);
        tv_status_details.setText("Tarikh: " + tarikh + ", Mohon: "+model.get_kuantiti_pohon()+", "+" Lulus:"+model.get_kuantiti_lulus());


        //tv_no_kad.setText("No Kad : "+model.get_no_kad());
        //tv_no_kod.setText("No Kod : "+model.get_no_kod());
    }
}
