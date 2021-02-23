package com.example.monitoringmotorlistrik.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kerusakanmotor implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tgl_masuk")
    @Expose
    private String tglMasuk;
    @SerializedName("tgl_selesai")
    @Expose
    private String tglSelesai;
    @SerializedName("tgl_keluar")
    @Expose
    private String tglKeluar;
    @SerializedName("kode_alat_wo")
    @Expose
    private String kodeAlatWo;
    @SerializedName("kode_pemakai")
    @Expose
    private String kodePemakai;
    @SerializedName("material_bearing")
    @Expose
    private String materialBearing;
    @SerializedName("material_kawat_kg")
    @Expose
    private String materialKawatKg;
    @SerializedName("nomor_wo")
    @Expose
    private String nomorWo;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("dll")
    @Expose
    private String dll;
    @SerializedName("foto_sebelum")
    @Expose
    private String foto_sebelum;
    @SerializedName("foto_sesudah")
    @Expose
    private String foto_sesudah;
    @SerializedName("tgl_dibuat")
    @Expose
    private String tglDibuat;
    @SerializedName("tgl_diupdate")
    @Expose
    private String tglDiupdate;
    @SerializedName("id_user")
    @Expose
    private String idUser;

    protected Kerusakanmotor(Parcel in) {
        id = in.readString();
        tglMasuk = in.readString();
        tglSelesai = in.readString();
        tglKeluar = in.readString();
        kodeAlatWo = in.readString();
        kodePemakai = in.readString();
        materialBearing = in.readString();
        materialKawatKg = in.readString();
        nomorWo = in.readString();
        keterangan = in.readString();
        dll = in.readString();
        foto_sebelum = in.readString();
        foto_sesudah = in.readString();
        tglDibuat = in.readString();
        tglDiupdate = in.readString();
        idUser = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tglMasuk);
        dest.writeString(tglSelesai);
        dest.writeString(tglKeluar);
        dest.writeString(kodeAlatWo);
        dest.writeString(kodePemakai);
        dest.writeString(materialBearing);
        dest.writeString(materialKawatKg);
        dest.writeString(nomorWo);
        dest.writeString(keterangan);
        dest.writeString(dll);
        dest.writeString(foto_sebelum);
        dest.writeString(foto_sesudah);
        dest.writeString(tglDibuat);
        dest.writeString(tglDiupdate);
        dest.writeString(idUser);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Kerusakanmotor> CREATOR = new Creator<Kerusakanmotor>() {
        @Override
        public Kerusakanmotor createFromParcel(Parcel in) {
            return new Kerusakanmotor(in);
        }

        @Override
        public Kerusakanmotor[] newArray(int size) {
            return new Kerusakanmotor[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTglMasuk() {
        return tglMasuk;
    }

    public void setTglMasuk(String tglMasuk) {
        this.tglMasuk = tglMasuk;
    }

    public String getTglSelesai() {
        return tglSelesai;
    }

    public void setTglSelesai(String tglSelesai) {
        this.tglSelesai = tglSelesai;
    }

    public String getTglKeluar() {
        return tglKeluar;
    }

    public void setTglKeluar(String tglKeluar) {
        this.tglKeluar = tglKeluar;
    }

    public String getKodeAlatWo() {
        return kodeAlatWo;
    }

    public void setKodeAlatWo(String kodeAlatWo) {
        this.kodeAlatWo = kodeAlatWo;
    }

    public String getKodePemakai() {
        return kodePemakai;
    }

    public void setKodePemakai(String kodePemakai) {
        this.kodePemakai = kodePemakai;
    }

    public String getMaterialBearing() {
        return materialBearing;
    }

    public void setMaterialBearing(String materialBearing) {
        this.materialBearing = materialBearing;
    }

    public String getMaterialKawatKg() {
        return materialKawatKg;
    }

    public void setMaterialKawatKg(String materialKawatKg) {
        this.materialKawatKg = materialKawatKg;
    }

    public String getNomorWo() {
        return nomorWo;
    }

    public void setNomorWo(String nomorWo) {
        this.nomorWo = nomorWo;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getDll() {
        return dll;
    }

    public void setDll(String dll) {
        this.dll = dll;
    }

    public String getFoto_sebelum() {
        return foto_sebelum;
    }

    public void setFoto_sebelum(String foto_sebelum) {
        this.foto_sebelum = foto_sebelum;
    }

    public String getFoto_sesudah() {
        return foto_sesudah;
    }

    public void setFoto_sesudah(String foto_sesudah) {
        this.foto_sesudah = foto_sesudah;
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
