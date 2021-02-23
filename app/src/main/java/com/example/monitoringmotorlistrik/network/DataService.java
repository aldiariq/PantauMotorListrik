package com.example.monitoringmotorlistrik.network;

import com.example.monitoringmotorlistrik.model.Alatwo;
import com.example.monitoringmotorlistrik.model.Kerusakanmotor;
import com.example.monitoringmotorlistrik.model.Pemakai;
import com.example.monitoringmotorlistrik.model.User;
import com.example.monitoringmotorlistrik.response.ResponseAlatwo;
import com.example.monitoringmotorlistrik.response.ResponseCetak;
import com.example.monitoringmotorlistrik.response.ResponseInfouser;
import com.example.monitoringmotorlistrik.response.ResponseKerusakanmotor;
import com.example.monitoringmotorlistrik.response.ResponseMasuk;
import com.example.monitoringmotorlistrik.response.ResponsePemakai;
import com.example.monitoringmotorlistrik.response.ResponseUmum;
import com.example.monitoringmotorlistrik.response.ResponseUser;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DataService {

    @Multipart
    @POST(Server.API_DAFTAR)
    Call<ResponseUmum> apiDaftar(
            @Part("nip") RequestBody nip,
            @Part("password") RequestBody password,
            @Part("nama") RequestBody nama,
            @Part("alamat") RequestBody alamat,
            @Part("nohp") RequestBody nohp,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part foto
    );

    @FormUrlEncoded
    @POST(Server.API_MASUK)
    Call<ResponseMasuk> apiMasuk(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET(Server.API_LIHATDATAUSER + "{id}")
    Call<ResponseInfouser> apiGetinfouser(
            @Header("Authorization") String Token,
            @Path("id") String id
    );

    @FormUrlEncoded
    @POST(Server.API_GANTIPASSWORDUSER)
    Call<ResponseUmum> apiGantipassword(
            @Header("Authorization") String Token,
            @Field("id") String id,
            @Field("passwordlama") String passwordlama,
            @Field("passwordbaru") String passwordbaru
    );

    @FormUrlEncoded
    @POST(Server.API_RESETPASSWORDUSER)
    Call<ResponseUmum> apiResetpassword(
            @Field("email") String email
    );

    @Multipart
    @POST(Server.API_UBAHPROFIL)
    Call<ResponseUmum> apiUbahprofil(
            @Header("Authorization") String Token,
            @Part("id") RequestBody id,
            @Part("nip") RequestBody nip,
            @Part("nama") RequestBody nama,
            @Part("alamat") RequestBody alamat,
            @Part("nohp") RequestBody nohp,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part foto
    );

    @Multipart
    @POST(Server.API_TAMBAHKERUSAKANMOTOR)
    Call<ResponseUmum> apiTambahkerusakanmotor(
            @Header("Authorization") String Token,
            @Part("tgl_masuk") RequestBody tglmasuk,
            @Part("tgl_selesai") RequestBody tglselesai,
            @Part("tgl_keluar") RequestBody tglkeluar,
            @Part("kode_alat_wo") RequestBody kodealatwo,
            @Part("kode_pemakai") RequestBody kode_pemakai,
            @Part("material_bearing") RequestBody material_bearing,
            @Part("material_kawat_kg") RequestBody material_kawat_kg,
            @Part("nomor_wo") RequestBody nomor_wo,
            @Part("keterangan") RequestBody keterangan,
            @Part("dll") RequestBody dll,
            @Part MultipartBody.Part foto_sebelum,
            @Part("id_user") RequestBody id_user
    );

    @GET(Server.API_LIHATKERUSAKANMOTORBELUM)
    Call<ResponseKerusakanmotor<List<Kerusakanmotor>>> apiLihatkerusakanmotorbelum(
            @Header("Authorization") String Token
    );

    @GET(Server.API_LIHATKERUSAKANMOTORSUDAH)
    Call<ResponseKerusakanmotor<List<Kerusakanmotor>>> apiLihatkerusakanmotorsudah(
            @Header("Authorization") String Token
    );

    @Multipart
    @POST(Server.API_UBAHKERUSAKANMOTOR + "{id}")
    Call<ResponseUmum> apiUbahkerusakanmotor(
            @Header("Authorization") String Token,
            @Path("id") String id,
            @Part("tgl_masuk") RequestBody tglmasuk,
            @Part("tgl_selesai") RequestBody tglselesai,
            @Part("tgl_keluar") RequestBody tglkeluar,
            @Part("kode_alat_wo") RequestBody kodealatwo,
            @Part("kode_pemakai") RequestBody kode_pemakai,
            @Part("material_bearing") RequestBody material_bearing,
            @Part("material_kawat_kg") RequestBody material_kawat_kg,
            @Part("nomor_wo") RequestBody nomor_wo,
            @Part("keterangan") RequestBody keterangan,
            @Part("dll") RequestBody dll,
            @Part MultipartBody.Part foto_selesai,
            @Part("id_user") RequestBody id_user
    );

    @FormUrlEncoded
    @POST(Server.API_KONFIRMASIPENGAMBILANMOTOR + "{id}")
    Call<ResponseUmum> apiKonfirmasipengambilan(
            @Header("Authorization") String Token,
            @Path("id") String id,
            @Field("tgl_keluar") String tglkeluar,
            @Field("id_user") String iduser
    );

    @GET(Server.API_HAPUSKERUSAKANMOTOR + "{id}")
    Call<ResponseUmum> apiHapuskerusakanmotor(
            @Header("Authorization") String Token,
            @Path("id") String id
    );

    @FormUrlEncoded
    @POST(Server.API_TAMBAHALATWO)
    Call<ResponseUmum> apiTambahalatwo(
            @Header("Authorization") String Token,
            @Field("kode_alat_wo") String kodealatwo,
            @Field("nama_alat_wo") String namaalatwo,
            @Field("keterangan_alat_wo") String keteranganalatwo,
            @Field("tgl_beli_alat_wo") String tglbeli,
            @Field("usia_maksimum_alat_wo") String usiamaksimumalatwo,
            @Field("usia_service_rekomendasi") String usiaservicerekomendasi,
            @Field("id_user") String iduser
    );

    @GET(Server.API_LIHATALATWO)
    Call<ResponseAlatwo<List<Alatwo>>> apiLihatalatwo(
            @Header("Authorization") String Token
    );

    @GET(Server.API_LIHATDAFTARUSER)
    Call<ResponseUser<List<User>>> apiLihatdaftaruser(
            @Header("Authorization") String Token
    );

    @FormUrlEncoded
    @POST(Server.API_UBAHSTATUSUSER + "{id}")
    Call<ResponseUmum> apiUbahstatususer(
            @Header("Authorization") String Token,
            @Path("id") String id,
            @Field("statusaktivasi") String statusaktivasi
    );

    @GET(Server.API_HAPUSUSER + "{id}")
    Call<ResponseUmum> apiHapususer(
            @Header("Authorization") String Token,
            @Path("id") String id
    );

    @FormUrlEncoded
    @POST(Server.API_UBAHALATWO + "{id}")
    Call<ResponseUmum> apiUbahalatwo(
            @Header("Authorization") String Token,
            @Path("id") String id,
            @Field("kode_alat_wo") String kodealatwo,
            @Field("nama_alat_wo") String namaalatwo,
            @Field("keterangan_alat_wo") String keteranganalatwo,
            @Field("tgl_beli_alat_wo") String tglbeli,
            @Field("usia_maksimum_alat_wo") String usiamaksimumalatwo,
            @Field("usia_service_rekomendasi") String usiaservicerekomendasi,
            @Field("id_user") String iduser
    );

    @GET(Server.API_HAPUSALATWO + "{id}")
    Call<ResponseUmum> apiHapusalatwo(
            @Header("Authorization") String Token,
            @Path("id") String id
    );

    @FormUrlEncoded
    @POST(Server.API_TAMBAHPEMAKAI)
    Call<ResponseUmum> apiTambahpemakai(
            @Header("Authorization") String Token,
            @Field("kode_pemakai") String kodepemakai,
            @Field("keterangan_pemakai") String keteranganpemakai,
            @Field("id_user") String iduser
    );

    @GET(Server.API_LIHATPEMAKAI)
    Call<ResponsePemakai<List<Pemakai>>> apiLihatpemakai(
            @Header("Authorization") String Token
    );

    @FormUrlEncoded
    @POST(Server.API_UBAHPEMAKAI + "{id}")
    Call<ResponseUmum> apiUbahpemakai(
            @Header("Authorization") String Token,
            @Path("id") String id,
            @Field("kode_pemakai") String kodepemakai,
            @Field("keterangan_pemakai") String keteranganpemakai,
            @Field("id_user") String iduser
    );

    @GET(Server.API_HAPUSPEMAKAI + "{id}")
    Call<ResponseUmum> apiHapuspemakai(
            @Header("Authorization") String Token,
            @Path("id") String id
    );

    @FormUrlEncoded
    @POST(Server.API_CETAKLAPORAN)
    Call<ResponseCetak> apiCetaklaporan(
            @Header("Authorization") String Token,
            @Field("tanggal_mulai") String tanggalmulai,
            @Field("tanggal_selesai") String tanggalselesai,
            @Field("jenis_dokumen") String jenisdokumen,
            @Field("opsi_cetak") int opsicetak
    );
}
