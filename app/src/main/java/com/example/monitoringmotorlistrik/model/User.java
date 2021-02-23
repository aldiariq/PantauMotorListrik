package com.example.monitoringmotorlistrik.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable{
    //Atribut Model Pengguna

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nip")
    @Expose
    private String nip;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("nohp")
    @Expose
    private String nohp;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusaktivasi")
    @Expose
    private String statusaktivasi;
    @SerializedName("tgl_dibuat")
    @Expose
    private String tglDibuat;
    @SerializedName("tgl_diupdate")
    @Expose
    private String tglDiupdate;

    //Constructor Model Pengguna
    public User(Parcel in) {
        id = in.readString();
        nip = in.readString();
        nama = in.readString();
        alamat = in.readString();
        nohp = in.readString();
        email = in.readString();
        foto = in.readString();
        status = in.readString();
        statusaktivasi = in.readString();
        tglDibuat = in.readString();
        tglDiupdate = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //Proses Penulisan Atribut Model Pengguna
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nip);
        dest.writeString(nama);
        dest.writeString(alamat);
        dest.writeString(nohp);
        dest.writeString(email);
        dest.writeString(foto);
        dest.writeString(status);
        dest.writeString(statusaktivasi);
        dest.writeString(tglDibuat);
        dest.writeString(tglDiupdate);
    }

    //Setter & Getter Atribut Model Pengguna

    public String getId() {
        return id;
    }

    public String getNip() {
        return nip;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto() {
        return foto;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusaktivasi() {
        return statusaktivasi;
    }

    public String getTglDibuat() {
        return tglDibuat;
    }

    public String getTglDiupdate() {
        return tglDiupdate;
    }
}
