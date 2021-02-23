package com.example.monitoringmotorlistrik.response;

import com.example.monitoringmotorlistrik.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUser<T> {

    @SerializedName("berhasil")
    @Expose
    private Boolean berhasil;
    @SerializedName("pesan")
    @Expose
    private String pesan;
    @SerializedName("pengguna")
    @Expose
    private List<User> user;

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

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}