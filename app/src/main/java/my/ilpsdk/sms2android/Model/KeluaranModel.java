package my.ilpsdk.sms2android.Model;

/**
 * Created by Nurzaly on 18/08/2016.
 */
public class KeluaranModel {
    private String _nama_item,_no_kad,_tarikh_pohon,_status,_unit_pengukuran, _nama_pemohon,_tujuan;
    private int _id,_id_item,_kuantiti_pohon,_kuantiti_lulus,_kuantiti, _id_pengguna_pohon;
    private String TAG = KeluaranModel.class.getSimpleName();

    public KeluaranModel(String name){
        this._nama_item = name;
    }

    //Status lilst
    public KeluaranModel(String name, String no_kad, String unit_pengukuran, String tarikh_pohon, int kuantiti_pohon, String kuantiti_lulus, String status) {
        this._no_kad = no_kad;
        this._nama_item = name;
        this._unit_pengukuran = unit_pengukuran;
        this._kuantiti_pohon = kuantiti_pohon;
        this._kuantiti_lulus = (kuantiti_lulus == "null")?Integer.parseInt("0"):Integer.parseInt(kuantiti_lulus.toString());
        this._tarikh_pohon = tarikh_pohon;
        this._status = status;
    }

    //Pegawaistor list
    public KeluaranModel(int id, int id_item, int id_pengguna_pohon, String nama_pemohon, String nama_item, String no_kad, String unit_pengukuran, String tarikh_pohon, int kuantiti_pohon, int kuantiti, String tujuan) {
        this._id = id;
        this._id_item = id_item;
        this._id_pengguna_pohon = id_pengguna_pohon;
        this._nama_pemohon = nama_pemohon;
        this._no_kad = no_kad;
        this._nama_item = nama_item;
        this._unit_pengukuran = unit_pengukuran;
        this._kuantiti_pohon = kuantiti_pohon;
        this._tarikh_pohon = tarikh_pohon;
        this._kuantiti = kuantiti;
        this._tujuan = tujuan;
    }

    public int get_id() {
        return this._id;
    }
    public int get_id_item() {
        return this._id_item;
    }
    public int get_id_pengguna_pohon() {
        return this._id_pengguna_pohon;
    }

    public int get_kuantiti() {
        return this._kuantiti;
    }

    public String get_nama_item() {
        return this._nama_item;
    }

    public String get_nama_pemohon() {
        return this._nama_pemohon;
    }
    public String get_no_kad() {
        return this._no_kad;
    }

    public String get_unit_pengukuran() {
        return this._unit_pengukuran;
    }

    public int get_kuantiti_pohon(){return  this._kuantiti_pohon;}

    public int get_kuantiti_lulus(){return  this._kuantiti_lulus;}

    public String get_tarikh_pohon(){return this._tarikh_pohon;}

    public String get_status(){return this._status;}

    public String get_tujuan(){return this._tujuan;}

}
