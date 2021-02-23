package com.example.monitoringmotorlistrik.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alatwo implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("kode_alat_wo")
    @Expose
    private String kodeAlatWo;
    @SerializedName("nama_alat_wo")
    @Expose
    private String namaAlatWo;
    @SerializedName("keterangan_alat_wo")
    @Expose
    private String keteranganAlatWo;
    @SerializedName("tgl_beli_alat_wo")
    @Expose
    private String tglBeliAlatWo;
    @SerializedName("usia_maksimum_alat_wo")
    @Expose
    private String usiaMaksimumAlatWo;
    @SerializedName("usia_service_rekomendasi")
    @Expose
    private String usiaServiceRekomendasi;
    @SerializedName("tgl_dibuat")
    @Expose
    private String tglDibuat;
    @SerializedName("tgl_diupdate")
    @Expose
    private String tglDiupdate;
    @SerializedName("id_user")
    @Expose
    private String idUser;

    protected Alatwo(Parcel in) {
        id = in.readString();
        kodeAlatWo = in.readString();
        namaAlatWo = in.readString();
        keteranganAlatWo = in.readString();
        tglBeliAlatWo = in.readString();
        usiaMaksimumAlatWo = in.readString();
        usiaServiceRekomendasi = in.readString();
        tglDibuat = in.readString();
        tglDiupdate = in.readString();
        idUser = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(kodeAlatWo);
        dest.writeString(namaAlatWo);
        dest.writeString(keteranganAlatWo);
        dest.writeString(tglBeliAlatWo);
        dest.writeString(usiaMaksimumAlatWo);
        dest.writeString(usiaServiceRekomendasi);
        dest.writeString(tglDibuat);
        dest.writeString(tglDiupdate);
        dest.writeString(idUser);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Alatwo> CREATOR = new Creator<Alatwo>() {
        @Override
        public Alatwo createFromParcel(Parcel in) {
            return new Alatwo(in);
        }

        @Override
        public Alatwo[] newArray(int size) {
            return new Alatwo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodeAlatWo() {
        return kodeAlatWo;
    }

    public void setKodeAlatWo(String kodeAlatWo) {
        this.kodeAlatWo = kodeAlatWo;
    }

    public String getNamaAlatWo() {
        return namaAlatWo;
    }

    public void setNamaAlatWo(String namaAlatWo) {
        this.namaAlatWo = namaAlatWo;
    }

    public String getKeteranganAlatWo() {
        return keteranganAlatWo;
    }

    public void setKeteranganAlatWo(String keteranganAlatWo) {
        this.keteranganAlatWo = keteranganAlatWo;
    }

    public String getTglBeliAlatWo() {
        return tglBeliAlatWo;
    }

    public void setTglBeliAlatWo(String tglBeliAlatWo) {
        this.tglBeliAlatWo = tglBeliAlatWo;
    }

    public String getUsiaMaksimumAlatWo() {
        return usiaMaksimumAlatWo;
    }

    public void setUsiaMaksimumAlatWo(String usiaMaksimumAlatWo) {
        this.usiaMaksimumAlatWo = usiaMaksimumAlatWo;
    }

    public String getUsiaServiceRekomendasi() {
        return usiaServiceRekomendasi;
    }

    public void setUsiaServiceRekomendasi(String usiaServiceRekomendasi) {
        this.usiaServiceRekomendasi = usiaServiceRekomendasi;
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