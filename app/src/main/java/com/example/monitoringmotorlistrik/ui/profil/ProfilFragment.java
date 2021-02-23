package com.example.monitoringmotorlistrik.ui.profil;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.monitoringmotorlistrik.LoginActivity;
import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.Server;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;
import com.example.monitoringmotorlistrik.response.ResponseUmum;
import com.example.monitoringmotorlistrik.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilFragment extends Fragment {

    private View view;

    private SharedPreferences sharedPreferences;

    private ImageView ivFoto;
    private TextView tvID, tvNip, tvNama, tvAlamat, tvNohp, tvEmail;
    private Button btnUbahprofil, btnUbahpassword, btnLogout;

    private SharedPreferences.Editor sharedPreferenceseditor;

    private DataService dataService;

    private String lokasifoto = "";

    private ImageView ivpengguna;
    private EditText nippengguna;
    private EditText namapengguna;
    private EditText alamatpengguna;
    private EditText nohppengguna;
    private EditText emailpengguna;

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    public ProfilFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null){
            view = inflater.inflate(R.layout.fragment_profil, container, false);
            initView(view);

            try {
                Picasso.get().load(Server.BASE_URL + "FileUpload/Foto/" + sharedPreferences.getString("foto", "")).into(ivFoto);
            }catch (Exception e){
                Toast.makeText(getContext(), "Gagal Memuat Gambar, Silahkan Periksa Koneksi", Toast.LENGTH_SHORT).show();
            }
            tvID.setText(": " + sharedPreferences.getString("id", ""));
            tvNip.setText(": " + sharedPreferences.getString("nip", ""));
            tvNama.setText(": " + sharedPreferences.getString("nama", ""));
            tvAlamat.setText(": " + sharedPreferences.getString("alamat", ""));
            tvNohp.setText(": " + sharedPreferences.getString("nohp", ""));
            tvEmail.setText(": " + sharedPreferences.getString("email", ""));

            btnUbahprofil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogubahprofil = inflater.inflate(R.layout.layout_ubah_profil, null);
                    dialog.setView(dialogubahprofil);
                    dialog.setCancelable(true);

                    ivpengguna = dialogubahprofil.findViewById(R.id.ubahprofil_image_foto);
                    nippengguna = dialogubahprofil.findViewById(R.id.ubahprofil_edittext_nip);
                    namapengguna = dialogubahprofil.findViewById(R.id.ubahprofil_edittext_nama);
                    alamatpengguna = dialogubahprofil.findViewById(R.id.ubahprofil_edittext_alamat);
                    nohppengguna = dialogubahprofil.findViewById(R.id.ubahprofil_edittext_nohp);
                    emailpengguna = dialogubahprofil.findViewById(R.id.ubahprofil_edittext_email);

                    Picasso.get().load(Server.BASE_URL + "FileUpload/Foto/" + sharedPreferences.getString("foto", "")).into(ivpengguna);
                    nippengguna.setText(sharedPreferences.getString("nip", ""));
                    namapengguna.setText(sharedPreferences.getString("nama", ""));;
                    alamatpengguna.setText(sharedPreferences.getString("alamat", ""));
                    nohppengguna.setText(sharedPreferences.getString("nohp", ""));
                    emailpengguna.setText(sharedPreferences.getString("email", ""));

                    String token = sharedPreferences.getString("token", "");
                    String id = sharedPreferences.getString("id", "");

                    ivpengguna.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                                pilihFile();
                            }else {
                                deteksiPermissionandroid();
                            }
                        }
                    });

                    dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String reqnippengguna = nippengguna.getText().toString();
                            String reqnamapengguna = namapengguna.getText().toString();
                            String reqalamatpengguna = alamatpengguna.getText().toString();
                            String reqnohppengguna = nohppengguna.getText().toString();
                            String reqemailpengguna = emailpengguna.getText().toString();
                            if (lokasifoto.equalsIgnoreCase("") || reqnippengguna.equalsIgnoreCase("") ||
                                    reqnamapengguna.equalsIgnoreCase("") || reqalamatpengguna.equalsIgnoreCase("") ||
                                    reqnohppengguna.equalsIgnoreCase("") || reqemailpengguna.equalsIgnoreCase("")
                            ){
                                Toast.makeText(getContext(), "Pastikan Inputan Sudah Terisi dan Foto Sudah Dipilih", Toast.LENGTH_SHORT).show();
                            }else {
                                File foto = new File(lokasifoto);
                                RequestBody reqid = RequestBody.create(MediaType.parse("text/plain"), id);
                                RequestBody reqnip = RequestBody.create(MediaType.parse("text/plain"), reqnippengguna);
                                RequestBody reqnama = RequestBody.create(MediaType.parse("text/plain"), reqnamapengguna);
                                RequestBody reqalamat = RequestBody.create(MediaType.parse("text/plain"), reqalamatpengguna);
                                RequestBody reqnohp = RequestBody.create(MediaType.parse("text/plain"), reqnohppengguna);
                                RequestBody reqemail = RequestBody.create(MediaType.parse("text/plain"), reqemailpengguna);
                                RequestBody requestBodyfile = RequestBody.create(MediaType.parse("/"), foto);
                                MultipartBody.Part filefoto = MultipartBody.Part.createFormData("foto", foto.getName(), requestBodyfile);

                                Call<ResponseUmum> responseUbahprofilCall = dataService.apiUbahprofil(token, reqid, reqnip, reqnama, reqalamat, reqnohp, reqemail, filefoto);
                                responseUbahprofilCall.enqueue(new Callback<ResponseUmum>() {
                                    @Override
                                    public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                                        if (response.code() == 200){
                                            if (response.body().isBerhasil()){
                                                dialog.dismiss();
                                                fungsiLogout();
                                                Toast.makeText(getContext(), "Berhasil Mengubah Profil", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(getContext(), "Pastikan Inputan Sudah Terisi dan Foto Sudah Dipilih 1", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(getContext(), "Pastikan Inputan Sudah Terisi dan Foto Sudah Dipilih 2", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseUmum> call, Throwable t) {
                                        Toast.makeText(getContext(), "Pastikan Inputan Sudah Terisi dan Foto Sudah Dipilih 3", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });

                    dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            btnUbahpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialoggantipassword = inflater.inflate(R.layout.layout_ganti_password, null);
                    dialog.setView(dialoggantipassword);
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TextView passwordlama = dialoggantipassword.findViewById(R.id.layoutgantipassword_passwordlama);
                            TextView passwordbaru = dialoggantipassword.findViewById(R.id.layoutgantipassword_passwordbaru);
                            TextView passwordbaru2 = dialoggantipassword.findViewById(R.id.layoutgantipassword_passwordbaru2);

                            String token = sharedPreferences.getString("token", "");
                            String id = sharedPreferences.getString("id", "");
                            String reqpasswordlama = passwordlama.getText().toString().trim();
                            String reqpasswordbaru = passwordbaru.getText().toString().trim();
                            String reqpasswordbaru2 = passwordbaru2.getText().toString().trim();

                            if (reqpasswordlama.equals("") || reqpasswordbaru.equals("") || reqpasswordbaru2.equals("")){
                                Toast.makeText(getContext(), "Pastikan Inputan Terisi", Toast.LENGTH_SHORT).show();
                            }else {
                                if (reqpasswordbaru.equals(reqpasswordbaru2)){
                                    Call<ResponseUmum> gantipasswordCall = dataService.apiGantipassword(token, id, reqpasswordlama, reqpasswordbaru);
                                    gantipasswordCall.enqueue(new Callback<ResponseUmum>() {
                                        @Override
                                        public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                                            if (response.code() == 200){
                                                if (response.body().isBerhasil()){
                                                    fungsiLogout();
                                                }else {
                                                    Toast.makeText(getContext(), "Gagal Mengubah Password, Pastikan Password Lama Benar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getContext(), "Gagal Mengubah Password, Pastikan Password Lama Benar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseUmum> call, Throwable t) {
                                            Toast.makeText(getContext(), "Gagal Mengubah Password, Pastikan Password Lama Benar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    Toast.makeText(getContext(), "Pastikan Password Sama", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fungsiLogout();
                }
            });
        }

        return view;
    }

    private void initView(View view){
        ivFoto = (ImageView) view.findViewById(R.id.profil_iv_foto);
        tvID = (TextView) view.findViewById(R.id.profil_tv_idpengguna);
        tvNip = (TextView) view.findViewById(R.id.profil_tv_nippengguna);
        tvNama = (TextView) view.findViewById(R.id.profil_tv_namapengguna);
        tvAlamat = (TextView) view.findViewById(R.id.profil_tv_alamatpengguna);
        tvNohp = (TextView) view.findViewById(R.id.profil_tv_nohppengguna);
        tvEmail = (TextView) view.findViewById(R.id.profil_tv_email);
        btnUbahprofil = (Button) view.findViewById(R.id.profil_btnubahprofil);
        btnUbahpassword = (Button) view.findViewById(R.id.profil_btnubahpassword);
        btnLogout = (Button) view.findViewById(R.id.profil_btnlogout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferenceseditor = sharedPreferences.edit();
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
    }

    private void fungsiLogout(){
        sharedPreferenceseditor.putBoolean("login", false);
        sharedPreferenceseditor.putString("token", "");
        sharedPreferenceseditor.putString("id", "");
        sharedPreferenceseditor.putString("nip", "");
        sharedPreferenceseditor.putString("nama", "");
        sharedPreferenceseditor.putString("alamat", "");
        sharedPreferenceseditor.putString("nohp", "");
        sharedPreferenceseditor.putString("email", "");
        sharedPreferenceseditor.putString("foto", "");
        sharedPreferenceseditor.putString("status", "");
        sharedPreferenceseditor.putString("statusaktivasi", "");
        sharedPreferenceseditor.putString("tgl_dibuat", "");
        sharedPreferenceseditor.putString("tgl_diupdate", "");
        sharedPreferenceseditor.apply();
        getActivity().finish();
        Toast.makeText(getContext(), "Berhasil Keluar", Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
        startActivity(intentLogin);
    }

    //Method Untuk Memeriksa Akses Membaca dan Menulis Penyimpanan
    private void deteksiPermissionandroid()  {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23

            // Check if we have read storage permission
            int pemissionread = ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (pemissionread != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }

            //check if we have write storage permission
            int permissionwrite = ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionwrite != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }
        }
    }

    //Method Untuk Menjalankan Intent/Activity Pemilihan File
    private void pilihFile() {
        String[] tipeFile =
                {"image/*"};

        Intent intentPilihfile = new Intent(Intent.ACTION_GET_CONTENT);
        intentPilihfile.addCategory(Intent.CATEGORY_OPENABLE);
        intentPilihfile.setType("*/*");
        intentPilihfile.putExtra(Intent.EXTRA_MIME_TYPES, tipeFile);
        startActivityForResult(Intent.createChooser(intentPilihfile,"Pilih File"), MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Akses File Diizinkan", Toast.LENGTH_SHORT).show();
                }
                // Cancelled or denied.
                else {
                    Toast.makeText(getContext(), "Akses File Tidak Diizinkan", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        Uri fileUri = data.getData();

                        String filePath = null;
                        try {
                            filePath = FileUtils.getPath(getContext(),fileUri);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.lokasifoto = filePath;
                        Picasso.get().load(new File(lokasifoto)).into(ivpengguna);
                        Toast.makeText(getContext(), "Foto Sudah Dipilih", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}