package com.example.monitoringmotorlistrik.response;

import com.google.gson.annotations.SerializedName;

public class ResponseUmum<T> {
    @SerializedName("berhasil")
    private boolean berhasil;

    @SerializedName("pesan")
    private String pesan;

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
}
