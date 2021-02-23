package com.example.monitoringmotorlistrik.response;

import com.google.gson.annotations.SerializedName;

public class ResponseKerusakanmotor<T> {

    @SerializedName("berhasil")
    private boolean berhasil;
    @SerializedName("pesan")
    private String pesan;

    @SerializedName("kerusakanmotor")
    private T kerusakanmotor;

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

    public T getKerusakanmotor() {
        return kerusakanmotor;
    }

    public void setKerusakanmotor(T kerusakanmotor) {
        this.kerusakanmotor = kerusakanmotor;
    }
}
