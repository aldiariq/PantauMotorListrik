package com.example.monitoringmotorlistrik;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar pbloading;
    private int Progress;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Memanggil Method Inisialisasi Komponen View
        initView();

        //Membuat Thread Untuk Loading ProgressBar
        new Thread(new Runnable() {
            public void run() {
                while (Progress < 100) {
                    Progress += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            pbloading.setProgress(Progress);
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //Destroy Activity & Menjalankan MainActivity(Masuk)
                finish();
                Intent pindahhalamanmasuk = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(pindahhalamanmasuk);
            }
        }).start();
    }

    //Inisialisasi Komponen View
    private void initView(){
        pbloading = (ProgressBar) findViewById(R.id.pbloadingHalamansplashscreen);
        Progress = pbloading.getProgress();
        handler = new Handler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}