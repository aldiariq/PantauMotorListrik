package com.example.monitoringmotorlistrik.response;

import com.google.gson.annotations.SerializedName;

public class ResponseCetak<T> {
    @SerializedName("berhasil")
    private boolean berhasil;

    @SerializedName("pesan")
    private String pesan;

    @SerializedName("lokasifile")
    private String lokasifile;

    public boolean isBerhasil() {
        return berhasil;
    }

    public void setBerhasil(boolean berhasil) {
        this.berhasil = berhasil;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getLokasifile() {
        return lokasifile;
    }

    public void setLokasifile(String lokasifile) {
        this.lokasifile = lokasifile;
    }
}
