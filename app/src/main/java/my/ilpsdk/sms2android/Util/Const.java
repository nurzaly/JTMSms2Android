package my.ilpsdk.sms2android.Util;

import org.json.JSONArray;

public class Const {
	public static final String DOMAIN = "http://192.168.1.40/";
	public static final String DOMAIN2 = "http://www.ilpsdk.gov.my/";
	public static final String URL = "sms2apps/index.php/";
	public static final String CONTROLER = "welcome/";
	public static final String URL_SEND_PEMOHONAN = URL + CONTROLER + "hantar_permohonan_stok/";
	public static final String URL_GET_ITEM = URL + CONTROLER + "get_itmes/";
	public static final String URL_GET_STOR = URL + CONTROLER + "get_stors/";
	public static final String URL_GET_STATUS_LIST = URL + CONTROLER + "get_status_permohonan_stok/";
	public static final String URL_GET_PEGAWAISTOR_LIST = URL + CONTROLER + "get_permohonan_stok/";
	public static final String URL_SET_STATUS_PERMOHONAN = URL + CONTROLER + "set_status_permohonan_stok/";
	public static final String URL_GET_STAF_DATA = URL + CONTROLER + "get_user_data/";
	public static final String URL_IMAGE = DOMAIN + "sms2/gambar/";

	public static final String URL_LOGIN = DOMAIN2+"stafdirektori/index.php/apps_stafdirectory/verify2";

	public static final String MYCONFIG = "myconfig";
	public static int id_bahagian = 0;
	public static int id_pengguna = 0;
	public static int id_jabatan = 0;
	public static int group_pengguna = 0;
	public static String noic = "";

	public static JSONArray status_list = null;
	public static JSONArray pegawaistor_list = null;
	public static JSONArray item_list = null;

	//stor item list
	public static String kod_bahagian_item_list = null;
	public static int id_bahagian_item_list = 0;


}
