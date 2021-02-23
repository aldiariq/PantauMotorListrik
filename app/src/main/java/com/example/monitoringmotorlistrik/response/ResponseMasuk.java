package com.example.monitoringmotorlistrik.response;

import com.example.monitoringmotorlistrik.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMasuk<T> {
    @SerializedName("berhasil")
    private boolean berhasil;
    @SerializedName("pesan")
    private String pesan;
    @SerializedName("token")
    private String token;
    @SerializedName("pengguna")
    private List<User> pengguna;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<User> getPengguna() {
        return pengguna;
    }

    public void setPengguna(List<User> pengguna) {
        this.pengguna = pengguna;
    }
}
