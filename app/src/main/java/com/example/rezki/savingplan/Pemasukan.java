package com.example.rezki.savingplan;

/**
 * Created by Rezki on 6/3/2017.
 */

public class Pemasukan {
    private String tgl_pemasukan;
    private String kategori;

    public String getTgl_pemasukan() {
        return tgl_pemasukan;
    }

    public void setTgl_pemasukan(String tgl_pemasukan) {
        this.tgl_pemasukan = tgl_pemasukan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    private String jumlah;
    private String detail;
}
