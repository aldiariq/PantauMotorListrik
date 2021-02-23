package com.example.monitoringmotorlistrik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;
import com.example.monitoringmotorlistrik.response.ResponseUmum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LupapassActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnReset, btnLogin;
    private DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupapass);

        initView();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (email.equalsIgnoreCase("")){
                    Toast.makeText(LupapassActivity.this, "Pastikan Inputan Terisi", Toast.LENGTH_SHORT).show();
                }else {
                    Call<ResponseUmum> resetpasswordCall = dataService.apiResetpassword(email);
                    resetpasswordCall.enqueue(new Callback<ResponseUmum>() {
                        @Override
                        public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                            if (response.code() == 200){
                                if (response.body().isBerhasil()){
                                    finish();
                                    Toast.makeText(LupapassActivity.this, "Berhasil Mengirim Permintaan Reset Password", Toast.LENGTH_SHORT).show();
                                    Intent intentLogin = new Intent(LupapassActivity.this, LoginActivity.class);
                                    startActivity(intentLogin);
                                }else {
                                    Toast.makeText(LupapassActivity.this, "Pastikan Email Terdaftar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(LupapassActivity.this, "Pastikan Email Terdaftar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUmum> call, Throwable t) {
                            Toast.makeText(LupapassActivity.this, "Pastikan Email Terdaftar dan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentLogin = new Intent(LupapassActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            }
        });
    }

    private void initView(){
        etEmail = (EditText) findViewById(R.id.lupapassword_edittext_email);
        btnReset = (Button) findViewById(R.id.lupapassword_btn_reset);
        btnLogin = (Button) findViewById(R.id.lupapassword_btn_login);
        dataService = (DataService) ServiceGenerator.createBaseService(LupapassActivity.this, DataService.class);
    }
}