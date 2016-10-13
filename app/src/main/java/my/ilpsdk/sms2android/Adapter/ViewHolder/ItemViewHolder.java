package my.ilpsdk.sms2android.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ilpsdk.sms2android.R;

import my.ilpsdk.sms2android.Model.ItemModel;
import my.ilpsdk.sms2android.Util.WordsCapitalizer;


public class ItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView tv_name, tv_kuantiti, tv_no_kad;

    public ItemViewHolder(View itemView) {
        super(itemView);

        tv_name = (TextView) itemView.findViewById(R.id.name);
        tv_kuantiti = (TextView) itemView.findViewById(R.id.kuantiti);
        tv_no_kad = (TextView) itemView.findViewById(R.id.pegawaistor_no_kad);
        //tv_no_kod = (TextView) itemView.findViewById(R.id.no_kod);
    }

    public void bind(ItemModel model) {
        //String textWord = WordsCapitalizer.capitalizeEveryWord(model.get_nama_jabatan());
        //tvHidden_id.setText(model.get_listid());
        tv_name.setText(WordsCapitalizer.capitalizeEveryWord(model.get_name()));
        tv_kuantiti.setText("Kuantiti : " + model.get_kuantiti() + " "+model.get_unit_pengukuran());
        tv_no_kad.setText(""+model.get_no_kad());
        //tv_no_kod.setText("No Kod : "+model.get_no_kod());
    }
}
