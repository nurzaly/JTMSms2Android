package my.ilpsdk.sms2android.Adapter.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ilpsdk.sms2android.R;

import my.ilpsdk.sms2android.Model.StorModel;
import my.ilpsdk.sms2android.Util.WordsCapitalizer;


public class StorViewHolder extends RecyclerView.ViewHolder {

    private final TextView tv_jabatan, tv_bahagian, tv_kod_bahagian;

    public StorViewHolder(View itemView) {
        super(itemView);

        tv_jabatan = (TextView) itemView.findViewById(R.id.jabatan);
        tv_bahagian = (TextView) itemView.findViewById(R.id.bahagian);
        tv_kod_bahagian = (TextView) itemView.findViewById(R.id.kod_bahagian);
    }

    public void bind(StorModel model) {
        //String textWord = WordsCapitalizer.capitalizeEveryWord(model.get_nama_jabatan());
        //tvHidden_id.setText(model.get_listid());
        tv_jabatan.setText(model.get_nama_jabatan().replace("ILPSDK/","").toUpperCase());
        tv_bahagian.setText(WordsCapitalizer.capitalizeEveryWord(model.get_nama_bahagian()));
        tv_kod_bahagian.setText(model.get_kod_bahagian().toUpperCase());
    }
}
