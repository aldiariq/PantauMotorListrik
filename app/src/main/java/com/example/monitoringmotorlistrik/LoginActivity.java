package com.example.monitoringmotorlistrik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoringmotorlistrik.model.User;
import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;
import com.example.monitoringmotorlistrik.response.ResponseMasuk;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView txtLupapassword;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferenceseditor;
    private DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        if (sharedPreferences.getBoolean("login", false)){
            finish();
            Intent intentDashboard = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intentDashboard);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.equalsIgnoreCase("") || password.equalsIgnoreCase("")){
                    Toast.makeText(LoginActivity.this, "Pastikan Inputan Terisi", Toast.LENGTH_SHORT).show();
                }else {
                    Call<ResponseMasuk> masukCall = dataService.apiMasuk(email, password);
                    masukCall.enqueue(new Callback<ResponseMasuk>() {
                        @Override
                        public void onResponse(Call<ResponseMasuk> call, Response<ResponseMasuk> response) {
                            if (response.code() == 200){
                                if (response.body().isBerhasil()){
                                    List<User> pengguna = (List<User>) response.body().getPengguna();
                                    sharedPreferenceseditor.putBoolean("login", true);
                                    sharedPreferenceseditor.putString("token", response.body().getToken());
                                    sharedPreferenceseditor.putString("id", pengguna.get(0).getId());
                                    sharedPreferenceseditor.putString("nip", pengguna.get(0).getNip());
                                    sharedPreferenceseditor.putString("nama", pengguna.get(0).getNama());
                                    sharedPreferenceseditor.putString("alamat", pengguna.get(0).getAlamat());
                                    sharedPreferenceseditor.putString("nohp", pengguna.get(0).getNohp());
                                    sharedPreferenceseditor.putString("email", pengguna.get(0).getEmail());
                                    sharedPreferenceseditor.putString("foto", pengguna.get(0).getFoto());
                                    sharedPreferenceseditor.putString("status", pengguna.get(0).getStatus());
                                    sharedPreferenceseditor.putString("statusaktivasi", pengguna.get(0).getStatusaktivasi());
                                    sharedPreferenceseditor.putString("tgl_dibuat", pengguna.get(0).getTglDibuat());
                                    sharedPreferenceseditor.putString("tgl_diupdate", pengguna.get(0).getTglDiupdate());
                                    sharedPreferenceseditor.apply();

                                    finish();
                                    Toast.makeText(LoginActivity.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                    Intent intentDashboard = new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(intentDashboard);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Pastikan Password Benar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseMasuk> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        txtLupapassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentLupapass = new Intent(LoginActivity.this, LupapassActivity.class);
                startActivity(intentLupapass);
            }
        });
    }

    private void initView(){
        etEmail = (EditText) findViewById(R.id.login_edittext_email);
        etPassword = (EditText) findViewById(R.id.login_edittext_password);
        btnLogin = (Button) findViewById(R.id.login_btnlogin);
        btnRegister = (Button) findViewById(R.id.login_btnregister);
        txtLupapassword = (TextView) findViewById(R.id.login_txtlupapassword);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        sharedPreferenceseditor = sharedPreferences.edit();
        dataService = (DataService) ServiceGenerator.createBaseService(LoginActivity.this, DataService.class);
    }
}