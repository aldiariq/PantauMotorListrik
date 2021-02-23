package com.example.monitoringmotorlistrik.response;

import com.example.monitoringmotorlistrik.model.Alatwo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseAlatwo<T> {

    @SerializedName("berhasil")
    @Expose
    private Boolean berhasil;
    @SerializedName("pesan")
    @Expose
    private String pesan;
    @SerializedName("alatwo")
    @Expose
    private List<Alatwo> alatwo;

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

    public List<Alatwo> getAlatwo() {
        return alatwo;
    }

    public void setAlatwo(List<Alatwo> alatwo) {
        this.alatwo = alatwo;
    }

}