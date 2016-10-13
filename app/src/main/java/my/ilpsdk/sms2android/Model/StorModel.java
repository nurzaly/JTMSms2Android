package my.ilpsdk.sms2android.Model;

/**
 * Created by Nurzaly on 29/07/2016.
 */
public class StorModel {
    private int _id_bahagian;
    private String _nama_jabatan,_nama_bahagian,_kod_bahagian;

    public StorModel(int id_bahagian, String nama_jabatan, String nama_bahagian, String kod_bahagian) {
        this._id_bahagian = id_bahagian;
        this._nama_jabatan = nama_jabatan;
        this._nama_bahagian = nama_bahagian;
        this._kod_bahagian = kod_bahagian;
    }

    public int get_id_bahagian() {
        return this._id_bahagian;
    }

    public String get_nama_jabatan() {
        return this._nama_jabatan;
    }

    public String get_nama_bahagian(){return  this._nama_bahagian;}

    public String get_kod_bahagian(){return  this._kod_bahagian;}

}
