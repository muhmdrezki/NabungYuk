package com.example.rezki.savingplan;

/**
 * Created by Rezki on 6/7/2017.
 */

public class Pengeluaran {
    private String tgl_pengeluaran;
    private String jumlah;
    private String kategori;
    private String detail;

    public String getTgl_pengeluaran() {
        return tgl_pengeluaran;
    }

    public void setTgl_pengeluaran(String tgl_pengeluaran) {
        this.tgl_pengeluaran = tgl_pengeluaran;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
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
}
