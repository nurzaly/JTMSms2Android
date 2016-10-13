package my.ilpsdk.sms2android.Model;

/**
 * Created by Nurzaly on 18/08/2016.
 */
public class ItemModel {
    private String _name,_unit_pengukuran,_no_kod;
    private int _id,_kuantiti,_no_kad;

    public ItemModel(int id, String name, int kuantiti, String unit_pengukuran, int no_kad, String no_kod) {
        this._id = id;
        this._name = name;
        this._kuantiti = kuantiti;
        this._unit_pengukuran = unit_pengukuran;
        this._no_kad = no_kad;
        this._no_kod = no_kod;
    }

    public ItemModel(String name){
        this._name = name;
    }

    public int get_id() {
        return this._id;
    }
    public void set_id(int id) {
        this._id = id;
    }

    public String get_name() {
        return this._name;
    }
    public void set_name(String name){this._name = name;}

    public int get_kuantiti(){return  this._kuantiti;}
    public void set_kuantiti(int kuantiti){this._kuantiti = kuantiti;}

    public String get_unit_pengukuran(){return  this._unit_pengukuran;}
    public void set_unit_pengukuran(String unit_pengukuran){this._unit_pengukuran = unit_pengukuran;}

    public int get_no_kad(){return this._no_kad;}

    public String get_no_kod(){return this._no_kod;}
}
