<?php
defined('BASEPATH') or exit('No direct script access allowed');

class welcome extends CI_Controller
{

    public function index()
    {
        echo "Android apps Sms2Apps Server";
    }

    public function get_itmes()
    {
        $bahagian = $this->uri->segment(3);
        $showzero = $this->uri->segment(4);
        $this->load->model('sms2');

        $data = $this->sms2->select_items($bahagian, $showzero);

        echo (json_encode($data));

    }

    public function get_stors()
    {
        $this->load->model('sms2');
        //$this->output->enable_profiler(TRUE);
        $data = $this->sms2->select_stor();

        echo (json_encode($data));
        //$this->load->view('getall',$data);
    }

    public function get_user_data()
    {
        $v    = json_decode(file_get_contents('php://input'));

        $noic = $v->uname;
        $this->load->model('sms2');

        $data = $this->sms2->select_data('pengguna', null, array("id_pengguna", "bahagian", "jabatan", "block_pengguna", "nama", "group"), array("no_ic" => $noic), 1);

        $response['success'] = (count($data) > 0) ? 1 : 0;
        //$response['message'] = "Get All item";
        $response['data']     = $data;
        $response['versions'] = 1;
        echo json_encode($response);
    }

    public function get_permohonan_stok()
    {
        $v           = json_decode(file_get_contents('php://input'));
        $id_bahagian = $v->id_bahagian;

        $this->load->model('sms2');
        $data = $this->sms2->select_permohonan($id_bahagian);

        echo json_encode($data);
        die();
    }

    public function hantar_permohonan_stok()
    {
        $v = json_decode(file_get_contents('php://input'));
        //$v = '{"item_id":"2116","id_bahagian":12,"id_pengguna":"2","kuantiti":"1","tujuan":"development test"}';
        //$v = json_decode($v);

        $kuantiti    = $v->kuantiti;
        $tujuan      = $v->tujuan;
        $id_pengguna = $v->id_pengguna;
        $id_bahagian = $v->id_bahagian;
        $item_id     = $v->item_id;

        $tarikh_pohon = date("Y-m-d");
        $tarikh_kod   = date("Ymd");

        $this->load->model('sms2');

        $bahagian = $this->sms2->select_data('bahagian', null,"kod_bahagian,id_jabatan", array("id_bahagian" => $id_bahagian), 1);
        //var_dump($bahagian);
        $kod_bahagian = $bahagian[0]->kod_bahagian;
        $id_jabatan = $bahagian[0]->id_jabatan;

        $no_pohon     = "$kod_bahagian/$tarikh_kod/$id_pengguna/(    )";

        $insert_data["no_pohon"]          = $no_pohon;
        $insert_data["id_pengguna_pohon"] = $id_pengguna;
        $insert_data["tarikh_pohon"]      = $tarikh_pohon;
        $insert_data["id_stor"]           = $item_id;
        $insert_data["kuantiti_pohon"]    = $kuantiti;
        $insert_data["jabatan"]           = $id_jabatan;
        $insert_data["bahagian"]          = $id_bahagian;
        $insert_data["status_keluar"]     = 0;
        $insert_data["tujuan"]            = $tujuan;
        //die(json_encode($insert_data));

        $data = $this->sms2->insert_data("keluaran_unit", $insert_data);

        //$data['data'] = $data;
        //var_dump($r[0]->nama);
        $response['success'] = (count($data) > 0) ? 1 : 0;
        //$response['message'] = "Get All item";
        $response['data'] = $data;

        echo json_encode($response);
    }

    public function get_status_permohonan_stok()
    {
        $v           = json_decode(file_get_contents('php://input'));
        $id_pengguna = $v->id_pengguna;
        //echo $v;

        $this->load->model('sms2');
        //$q1="select * from `keluaran_unit` WHERE `id_pengguna_pohon`='$id_pengguna' ORDER BY `tarikh_pohon` DESC LIMIT $start, $display";

        $data = $this->sms2->select_status($id_pengguna);
        $n    = 0;
        /*if (count($data) > 0) {
            foreach ($data as $key => $value) {
                if ($value->status_keluar == 1) {
                    $data[$n]->status_keluar = "Telah Diluluskan";
                } elseif ($value->status_keluar == 3) {
                    $data[$n]->status_keluar = "Tidak Diluluskan";
                } else {
                    $data[$n]->status_keluar = "Belum Lulus";
                }
                $n++;
            }
        }*/
        //$response["data"] = $data;
        echo json_encode($data);
        die();
        //var_dump($data);

    }

    public function set_status_permohonan_stok()
    {
        $v = json_decode(file_get_contents('php://input'));
        // $v                 = '{"id_keluar_unit":8699,"id_stor":2116,"kuantiti_lulus":"1","status_keluar":3,"catatan":"Catatan","id_pengguna_pohon":2,"id_pengguna":2,"nama_item":"Ballast\/Chocke 36W"}';

        // $v                 = json_decode($v);
        $id_keluar_unit    = $v->id_keluar_unit;
        $id_stor           = $v->id_stor;
        $kuantiti_lulus    = $v->kuantiti_lulus;
        $status_keluar     = $v->status_keluar;
        $catatan           = $v->catatan." from android";
        $tarikh_lulus      = date("Y-m-d");
        $id_pengguna_pohon = $v->id_pengguna_pohon;
        $id_pengguna       = $v->id_pengguna;
        $perihal_stor      = $v->nama_item;



        $response['success'] = 0;
        $response['msg']     = "";

        $this->load->model('sms2');

        //select_data($table, $orderby, $select, $where, $limit);
        $num = $this->sms2->get_num("keluaran_unit", null, null, array("id_keluar_unit" => $id_keluar_unit, "status_keluar !=" => 0));
        if ($num > 0) {
            $response['msg'] = "Sila hubungi administrator. Ralat status_keluar";
            echo(json_encode($response));
            die();
        }


        //select_data($table, $orderby, $select, $where, $limit);
        $r            = $this->sms2->select_data("stor", null, "kuantiti_1, kuantiti_2, kuantiti_3, kuantiti_4, harga_unit_1, harga_unit_2, harga_unit_3, harga_unit_4", "id_stor = $id_stor");
        $kuantiti_1   = $r[0]->kuantiti_1;
        $kuantiti_2   = $r[0]->kuantiti_2;
        $kuantiti_3   = $r[0]->kuantiti_3;
        $kuantiti_4   = $r[0]->kuantiti_4;
        $harga_unit_1 = $r[0]->harga_unit_1;
        $harga_unit_2 = $r[0]->harga_unit_2;
        $harga_unit_3 = $r[0]->harga_unit_3;
        $harga_unit_4 = $r[0]->harga_unit_4;

        if (empty($kuantiti_1)) {
            $kuantiti_1 = 0;
        }
        if (empty($kuantiti_2)) {
            $kuantiti_2 = 0;
        }
        if (empty($kuantiti_3)) {
            $kuantiti_3 = 0;
        }
        if (empty($kuantiti_4)) {
            $kuantiti_4 = 0;
        }

        $kuantiti_sediada = $kuantiti_1 + $kuantiti_2 + $kuantiti_3 + $kuantiti_4;

        if ($status_keluar == 1) {
            if (empty($kuantiti_sediada) || $kuantiti_sediada <= 0) {
                $response['msg'] = "Tiada Stok. Pengeluaran tidak boleh dibuat.";
                die(json_encode($response));
            }

            if ($kuantiti_sediada < $kuantiti_lulus) {
                $response['msg'] = "Stok tidak cukup. Pengeluaran tidak boleh dibuat.";
                die(json_encode($response));
            }
        }

        $kuantiti_12  = $kuantiti_1 + $kuantiti_2;
        $kuantiti_123 = $kuantiti_1 + $kuantiti_2 + $kuantiti_3;

        if ($kuantiti_lulus < $kuantiti_1) {

            $kuantiti_1_baru       = $kuantiti_1 - $kuantiti_lulus;
            $harga_keluar_unit     = $harga_unit_1;
            $jum_harga_keluar_unit = $kuantiti_lulus * $harga_unit_1;
            $baki                  = $kuantiti_1_baru + $kuantiti_2 + $kuantiti_3 + $kuantiti_4;
            $jum_harga_baki        = ($kuantiti_1_baru * $harga_unit_1) + ($kuantiti_2 * $harga_unit_2) + ($kuantiti_3 * $harga_unit_3) + ($kuantiti_4 * $harga_unit_4);

            $kuantiti_2_baru = $kuantiti_2;
            $kuantiti_3_baru = $kuantiti_3;
            $kuantiti_4_baru = $kuantiti_4;

        } else if ($kuantiti_lulus < $kuantiti_12) {

            $kuantiti_1_baru       = 0;
            $kuantiti_2_baru       = $kuantiti_1 + $kuantiti_2 - $kuantiti_lulus;
            $kuantiti_keluar_2     = $kuantiti_2 - $kuantiti_2_baru;
            $jum_harga_keluar_unit = ($kuantiti_1 * $harga_unit_1) + ($kuantiti_keluar_2 * $harga_unit_2);
            $harga_keluar_unit     = $jum_harga_keluar_unit / $kuantiti_lulus;
            $baki                  = $kuantiti_1_baru + $kuantiti_2_baru + $kuantiti_3 + $kuantiti_4;
            $jum_harga_baki        = ($kuantiti_1_baru * $harga_unit_1) + ($kuantiti_2_baru * $harga_unit_2) + ($kuantiti_3 * $harga_unit_3) + ($kuantiti_4 * $harga_unit_4);

            $kuantiti_3_baru = $kuantiti_3;
            $kuantiti_4_baru = $kuantiti_4;

        } else if ($kuantiti_lulus < $kuantiti_123) {

            $kuantiti_1_baru       = 0;
            $kuantiti_2_baru       = 0;
            $kuantiti_3_baru       = $kuantiti_1 + $kuantiti_2 + $kuantiti_3 - $kuantiti_lulus;
            $kuantiti_keluar_3     = $kuantiti_3 - $kuantiti_3_baru;
            $jum_harga_keluar_unit = ($kuantiti_1 * $harga_unit_1) + ($kuantiti_2 * $harga_unit_2) + ($kuantiti_keluar_3 * $harga_unit_3);
            $harga_keluar_unit     = $jum_harga_keluar_unit / $kuantiti_lulus;
            $baki                  = $kuantiti_1_baru + $kuantiti_2_baru + $kuantiti_3_baru + $kuantiti_4;
            $jum_harga_baki        = ($kuantiti_1_baru * $harga_unit_1) + ($kuantiti_2_baru * $harga_unit_2) + ($kuantiti_3_baru * $harga_unit_3) + ($kuantiti_4 * $harga_unit_4);

            $kuantiti_4_baru = $kuantiti_4;

        } else {

            $kuantiti_1_baru       = 0;
            $kuantiti_2_baru       = 0;
            $kuantiti_3_baru       = 0;
            $kuantiti_4_baru       = $kuantiti_1 + $kuantiti_2 + $kuantiti_3 + $kuantiti_4 - $kuantiti_lulus;
            $kuantiti_keluar_4     = $kuantiti_4 - $kuantiti_4_baru;
            $jum_harga_keluar_unit = ($kuantiti_1 * $harga_unit_1) + ($kuantiti_2 * $harga_unit_2) + ($kuantiti_3 * $harga_unit_3) + ($kuantiti_keluar_4 * $harga_unit_4);

            if ($status_keluar == 3) {
                /////ada ubah pada 1-6-2011
                $harga_keluar_unit == 0;
            } else {
                $harga_keluar_unit = $jum_harga_keluar_unit / $kuantiti_lulus;
            }

            $baki           = $kuantiti_1_baru + $kuantiti_2_baru + $kuantiti_3_baru + $kuantiti_4_baru;
            $jum_harga_baki = ($kuantiti_1_baru * $harga_unit_1) + ($kuantiti_2_baru * $harga_unit_2) + ($kuantiti_3_baru * $harga_unit_3) + ($kuantiti_4_baru * $harga_unit_4);

        }

        if ($status_keluar == 3) {

            $update["id_pengguna_lulus"]  = $id_pengguna;
            $update["tarikh_lulus"]       = $tarikh_lulus;
            $update["id_pengguna_rekod"]  = $id_pengguna;
            $update["tarikh_rekod"]       = $tarikh_lulus;
            $update["status_keluar"]      = $status_keluar;
            $update["catatan"]            = $catatan;
            $update["id_pengguna_terima"] = $id_pengguna_pohon;
            $update["tarikh_terima"]      = $tarikh_lulus;

            $r                   = $this->sms2->update_data("keluaran_unit", $update, "id_keluar_unit = $id_keluar_unit");
            $response['success'] = 1;
            $response['msg']     = "Pengeluaran stok tidak diluluskan untuk item $perihal_stor telah berjaya direkod.";
            //die(json_encode($response));
        } elseif ($status_keluar == 1) {
            $update["kuantiti_lulus"]        = $kuantiti_lulus;
            $update["baki"]                  = $baki;
            $update["harga_keluar_unit"]     = $harga_keluar_unit;
            $update["jum_harga_keluar_unit"] = $jum_harga_keluar_unit;
            $update["jum_harga_baki"]        = $jum_harga_baki;
            $update["id_pengguna_lulus"]     = $id_pengguna;
            $update["tarikh_lulus"]          = $tarikh_lulus;
            $update["id_pengguna_rekod"]     = $id_pengguna;
            $update["tarikh_rekod"]          = $tarikh_lulus;
            $update["status_keluar"]         = $status_keluar;
            $update["catatan"]               = $catatan;
            $update["id_pengguna_terima"]    = $id_pengguna_pohon;
            $update["tarikh_terima"]         = $tarikh_lulus;

            $r = $this->sms2->update_data("keluaran_unit", $update, "id_keluar_unit = $id_keluar_unit");

            $update_stor["kuantiti_1"] = $kuantiti_1_baru;
            $update_stor["kuantiti_2"] = $kuantiti_2_baru;
            $update_stor["kuantiti_3"] = $kuantiti_3_baru;
            $update_stor["kuantiti_4"] = $kuantiti_4_baru;
            $r                         = $this->sms2->update_data("stor", $update_stor, "id_stor = $id_stor");

            $response['success'] = 1;
            $response['msg']     = "Pengeluaran stok untuk item  $perihal_stor telah berjaya direkod.";
            //die(json_encode($response));
        } else {
            $response['success'] = 0;
            $response['msg']     = "Gagal. Sila hubungi administrator.";
            //die(json_encode($response));
        }

        //$response['query'] = $this->db->last_query();
        die(json_encode($response));
    }

    public function testjson()
    {
        $this->load->model('sms2');
        $data = $this->sms2->select_data('stor', "perihal_stor asc", array("id_stor", "perihal_stor"), array("bahagian" => 2), 2);

        echo json_encode($data);
    }

    public function test($value = null)
    {
        if (!isset($value)) {
            $value = file_get_contents('php://input');
        }

        $file = FCPATH . 'note.txt';

        file_put_contents($file, "\r\n" . date("H:i:s") . "\r\n$value", FILE_APPEND | LOCK_EX);
    }
}
