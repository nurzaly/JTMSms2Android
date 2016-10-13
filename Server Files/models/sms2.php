<?php
defined('BASEPATH') or exit('No direct script access allowed');

class sms2 extends CI_Model
{

    public function select_data($table, $orderby = null, $select = null, $w = null, $limit = null)
    {

        if ($select) {
            $this->db->select($select);
        }
        if ($w) {
            $this->db->where($w);
        }

        if ($orderby) {
            $this->db->order_by($orderby);
        }

        if ($limit) {
            $this->db->limit($limit);
        }

        //$this->db->order_by("gred desc, nama asc");
        //$this->db->limit(100);
        $q = $this->db->get($table);
        return $q->result();
    }

    public function select_items($id_bahagian = 0, $showzero = null)
    {
        $this->db->select(array("id_stor", "perihal_stor", "(kuantiti_1 + kuantiti_2 + kuantiti_3 + kuantiti_4) as kuantiti", "unit_pengukuran", "no_kad"));
        $this->db->order_by("perihal_stor asc");
        
        if ($showzero == "false") {
        	$this->db->where(array("bahagian" => $id_bahagian, "(kuantiti_1 + kuantiti_2 + kuantiti_3 + kuantiti_4) >" => 0));
        }
        else{
        	$this->db->where(array("bahagian" => $id_bahagian));
        }

        $r = $this->db->get("stor");
        return $r->result();

    }

    public function get_num($table, $orderby = null, $select = null, $w = null, $limit = null)
    {

        if ($select) {
            $this->db->select($select);
        }
        if ($w) {
            $this->db->where($w);
        }

        if ($orderby) {
            $this->db->order_by($orderby);
        }

        if ($limit) {
            $this->db->limit($limit);
        }

        //$this->db->order_by("gred desc, nama asc");
        //$this->db->limit(100);
        $q = $this->db->get($table);
        return $q->num_rows();
    }

    public function select_stor()
    {
        $this->db->select('bahagian.id_bahagian,bahagian.nama_bahagian,bahagian.kod_bahagian,jabatan.nama_jabatan');
        $this->db->from('bahagian');
        $this->db->join('jabatan', 'bahagian.id_jabatan = jabatan.id_jabatan', 'inner');
        $this->db->order_by('jabatan.id_jabatan asc, bahagian.nama_bahagian, bahagian.id_bahagian asc');
        //$q = $this->db->query("select bahagian.*,jabatan.nama_jabatan from bahagian inner join jabatan on bahagian.id_jabatan = jabatan.id_jabatan");
        $q = $this->db->get();
        return $q->result();
    }
    public function select_status($id_pengguna)
    {
        $this->db->select('stor.perihal_stor,stor.no_kad, stor.unit_pengukuran, keluaran_unit.tarikh_pohon, keluaran_unit.kuantiti_pohon, keluaran_unit.kuantiti_lulus, keluaran_unit.status_keluar');
        $this->db->from('keluaran_unit');
        $this->db->join('stor', 'stor.id_stor = keluaran_unit.id_stor', 'inner');
        $this->db->where('id_pengguna_pohon = ' . $id_pengguna);
        $this->db->order_by('keluaran_unit.tarikh_pohon desc, keluaran_unit.id_keluar_unit desc');
        //$q = $this->db->query("select bahagian.*,jabatan.nama_jabatan from bahagian inner join jabatan on bahagian.id_jabatan = jabatan.id_jabatan");
        $q = $this->db->get();
        return $q->result();
    }

    public function select_permohonan($id_bahagian)
    {
        $this->db->select('keluaran_unit.id_keluar_unit,keluaran_unit.id_stor,keluaran_unit.id_pengguna_pohon,pengguna.nama,stor.perihal_stor,stor.no_kad,stor.unit_pengukuran,keluaran_unit.tarikh_pohon,keluaran_unit.kuantiti_pohon,(stor.kuantiti_1 + stor.kuantiti_2 + stor.kuantiti_3 + stor.kuantiti_4) as kuantiti_1,keluaran_unit.tujuan');
        $this->db->from('keluaran_unit');
        $this->db->join('stor', 'keluaran_unit.id_stor = stor.id_stor', 'inner');
        $this->db->join('pengguna', 'keluaran_unit.id_pengguna_pohon = pengguna.id_pengguna', 'inner');
        $this->db->where(array("keluaran_unit.bahagian" => $id_bahagian, "keluaran_unit.status_keluar" => 0));
        $this->db->order_by('keluaran_unit.tarikh_pohon desc, keluaran_unit.id_keluar_unit desc');
        //$q = $this->db->query("select bahagian.*,jabatan.nama_jabatan from bahagian inner join jabatan on bahagian.id_jabatan = jabatan.id_jabatan");
        $q = $this->db->get();
        return $q->result();
    }

    public function select_data2($table, $select = null, $w = null)
    {
        if ($select) {
            $this->db->select($select);
        }
        if ($w) {
            $this->db->where($w);
        }

        $q = $this->db->get('direktori');
        return $q->result();
    }

    public function update_data($table, $update, $w = null)
    {
        $this->db->where($w);
        $q = $this->db->update($table, $update);
        return $this->db->affected_rows();
    }

    public function insert_data($table, $v)
    {
        $q = $this->db->insert($table, $v);
        return $q;
    }
}
