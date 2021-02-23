package com.example.monitoringmotorlistrik;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.monitoringmotorlistrik.network.DataService;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText etNip, etPassword, etPassword2, etNama, etAlamat, etNohp, etEmail;
    private ImageView imgFoto;
    private Button btnRegister, btnLogin;

    private DataService dataService;

    private String lokasifoto = "";

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    pilihFile();
                }else {
                    deteksiPermissionandroid();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nip = etNip.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String password2 = etPassword2.getText().toString().trim();
                String nama = etNama.getText().toString().trim();
                String alamat = etAlamat.getText().toString().trim();
                String nohp = etNohp.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                if (lokasifoto.equalsIgnoreCase("") || nip.equalsIgnoreCase("") || password.equalsIgnoreCase("")
                        || password2.equalsIgnoreCase("") || nama.equalsIgnoreCase("") || alamat.equalsIgnoreCase("")
                        || nohp.equalsIgnoreCase("") || email.equalsIgnoreCase("")
                ){
                    Toast.makeText(RegisterActivity.this, "Pastikan Inputan Sudah Terisi dan Foto Sudah Dipilih", Toast.LENGTH_SHORT).show();
                }else {
                    if (password.equals(password2)){
                        File foto = new File(lokasifoto);
                        RequestBody reqnip = RequestBody.create(MediaType.parse("text/plain"), nip);
                        RequestBody reqpassword = RequestBody.create(MediaType.parse("text/plain"), password);
                        RequestBody reqnama = RequestBody.create(MediaType.parse("text/plain"), nama);
                        RequestBody reqalamat = RequestBody.create(MediaType.parse("text/plain"), alamat);
                        RequestBody reqnohp = RequestBody.create(MediaType.parse("text/plain"), nohp);
                        RequestBody reqemail = RequestBody.create(MediaType.parse("text/plain"), email);
                        RequestBody requestBodyfile = RequestBody.create(MediaType.parse("/"), foto);
                        MultipartBody.Part filefoto = MultipartBody.Part.createFormData("foto", foto.getName(), requestBodyfile);

                        Call<ResponseUmum> daftarCall = dataService.apiDaftar(reqnip, reqpassword, reqnama, reqalamat, reqnohp, reqemail, filefoto);
                        daftarCall.enqueue(new Callback<ResponseUmum>() {
                            @Override
                            public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                                if (response.code() == 200){
                                    if (response.body().isBerhasil()){
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "Berhasil Mendaftar", Toast.LENGTH_SHORT).show();
                                        Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intentLogin);
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Pastikan Email Belum Terdaftar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(RegisterActivity.this, "Pastikan Email Belum Terdaftar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseUmum> call, Throwable t) {
                                Toast.makeText(RegisterActivity.this, "Pastikan Email Belum Terdaftar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(RegisterActivity.this, "Pastikan Password Sama", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            }
        });
    }

    private void initView(){
        etNip = (EditText) findViewById(R.id.register_edittext_nip);
        etPassword = (EditText) findViewById(R.id.register_edittext_password);
        etPassword2 = (EditText) findViewById(R.id.register_edittext_password2);
        etNama = (EditText) findViewById(R.id.register_edittext_nama);
        etAlamat = (EditText) findViewById(R.id.register_edittext_alamat);
        etNohp = (EditText) findViewById(R.id.register_edittext_nohp);
        etEmail = (EditText) findViewById(R.id.register_edittext_email);
        imgFoto = (ImageView) findViewById(R.id.register_image_foto);
        btnRegister = (Button) findViewById(R.id.register_btnregister);
        btnLogin = (Button) findViewById(R.id.register_btnlogin);
        dataService = (DataService) ServiceGenerator.createBaseService(RegisterActivity.this, DataService.class);
    }

    //Method Untuk Memeriksa Akses Membaca dan Menulis Penyimpanan
    private void deteksiPermissionandroid()  {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23

            // Check if we have read storage permission
            int pemissionread = ActivityCompat.checkSelfPermission(RegisterActivity.this,
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
            int permissionwrite = ActivityCompat.checkSelfPermission(RegisterActivity.this,
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
                    Toast.makeText(RegisterActivity.this, "Akses File Diizinkan", Toast.LENGTH_SHORT).show();
                }
                // Cancelled or denied.
                else {
                    Toast.makeText(RegisterActivity.this, "Akses File Tidak Diizinkan", Toast.LENGTH_SHORT).show();
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
                            filePath = FileUtils.getPath(RegisterActivity.this,fileUri);
                        } catch (Exception e) {
                            Toast.makeText(RegisterActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.lokasifoto = filePath;
                        Picasso.get().load(new File(lokasifoto)).into(imgFoto);
                        Toast.makeText(RegisterActivity.this, "Foto Sudah Dipilih", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}