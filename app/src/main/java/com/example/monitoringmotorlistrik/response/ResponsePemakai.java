package com.example.monitoringmotorlistrik.response;

import com.example.monitoringmotorlistrik.model.Pemakai;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePemakai<T> {
    @SerializedName("berhasil")
    @Expose
    private Boolean berhasil;
    @SerializedName("pesan")
    @Expose
    private String pesan;
    @SerializedName("pemakai")
    @Expose
    private List<Pemakai> pemakai;

    public Boolean getBerhasil() {
        return berhasil;
    }

    public void setBerhasil(Boolean berhasil) {
        this.berhasil = berhasil;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<Pemakai> getPemakai() {
        return pemakai;
    }

    public void setPemakai(List<Pemakai> pemakai) {
        this.pemakai = pemakai;
    }
}
