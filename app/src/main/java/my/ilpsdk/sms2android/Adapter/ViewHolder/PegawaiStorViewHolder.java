package my.ilpsdk.sms2android.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ilpsdk.sms2android.R;

import my.ilpsdk.sms2android.Model.KeluaranModel;
import my.ilpsdk.sms2android.Util.WordsCapitalizer;


public class PegawaiStorViewHolder extends RecyclerView.ViewHolder {

    private final TextView tv_nama_item, tv_nama_pemohon, tv_details, tv_no_kad, tv_tujuan;
    private String TAG = PegawaiStorViewHolder.class.getSimpleName();
    private String status,status2;

    public PegawaiStorViewHolder(View itemView) {
        super(itemView);

        tv_nama_item = (TextView) itemView.findViewById(R.id.pegawaistor_nama_item);
        tv_nama_pemohon = (TextView) itemView.findViewById(R.id.pegawaistor_nama_pemohon);
        tv_details = (TextView) itemView.findViewById(R.id.details);
        tv_tujuan = (TextView) itemView.findViewById(R.id.tujuan);
        tv_no_kad = (TextView) itemView.findViewById(R.id.pegawaistor_no_kad);
        //tv_kuantiti_pohon = (TextView) itemView.findViewById(R.id.kuantiti_pohon);
        //tv_kuantiti_lulus = (TextView) itemView.findViewById(R.id.kuantiti_lulus);
        //tv_status = (TextView) itemView.findViewById(R.id.status);
    }

    public void bind(KeluaranModel model) {
        String tarikh,year,month,date;

        year = model.get_tarikh_pohon().split("-")[0];
        month = model.get_tarikh_pohon().split("-")[1];
        date = model.get_tarikh_pohon().split("-")[2];
        tarikh = date+"-"+month+"-"+year;

        tv_nama_item.setText(WordsCapitalizer.capitalizeEveryWord(model.get_nama_item()));
        tv_nama_pemohon.setText(WordsCapitalizer.capitalizeEveryWord(model.get_nama_pemohon()));
        tv_details.setText("Tarikh : "+tarikh+", Kuantiti : " + model.get_kuantiti_pohon() + " / "+model.get_kuantiti()+" "+model.get_unit_pengukuran());
        tv_no_kad.setText("" + model.get_no_kad());
        tv_tujuan.setText("Tujuan : " + model.get_tujuan());
        //tv_nama_pemohon.setText("[ Kuantiti Pohon: " + model.get_kuantiti_pohon()+" "+model.get_unit_pengukuran()+" ]");
        //tv_status.setText("[ Status: " + status+" ]");



        //tv_no_kad.setText("No Kad : "+model.get_no_kad());
        //tv_no_kod.setText("No Kod : "+model.get_no_kod());
    }
}
