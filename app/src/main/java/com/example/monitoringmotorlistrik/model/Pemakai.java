package com.example.monitoringmotorlistrik.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pemakai {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("kode_pemakai")
    @Expose
    private String kodePemakai;
    @SerializedName("keterangan_pemakai")
    @Expose
    private String keteranganPemakai;
    @SerializedName("tgl_dibuat")
    @Expose
    private String tglDibuat;
    @SerializedName("tgl_diupdate")
    @Expose
    private String tglDiupdate;
    @SerializedName("id_user")
    @Expose
    private String idUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodePemakai() {
        return kodePemakai;
    }

    public void setKodePemakai(String kodePemakai) {
        this.kodePemakai = kodePemakai;
    }

    public String getKeteranganPemakai() {
        return keteranganPemakai;
    }

    public void setKeteranganPemakai(String keteranganPemakai) {
        this.keteranganPemakai = keteranganPemakai;
    }

    public String getTglDibuat() {
        return tglDibuat;
    }

    public void setTglDibuat(String tglDibuat) {
        this.tglDibuat = tglDibuat;
    }

    public String getTglDiupdate() {
        return tglDiupdate;
    }

    public void setTglDiupdate(String tglDiupdate) {
        this.tglDiupdate = tglDiupdate;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

}
