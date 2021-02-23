package com.example.monitoringmotorlistrik.ui.report;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.Server;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;
import com.example.monitoringmotorlistrik.response.ResponseCetak;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ReportFragment extends Fragment {

    private View view;
    private DataService dataService;

    private String tanggalMulai;
    private String tanggalSelesai;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private DownloadManager downloadManager;
    private DownloadManager.Request downloadManagerrequest;

    private SharedPreferences sharedPreferences;

    private EditText tglMulai, tglSelesai;
    private Spinner spinJenisdokumen;
    private Button btnHarusservice, btnSedangservice, btnSelesai, btnDiambil;

    public ReportFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_report, container, false);
            initView(view);

            tglMulai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateDialog(1);
                }
            });

            tglSelesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateDialog(2);
                }
            });

            btnHarusservice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perintahCetak(tanggalMulai, tanggalSelesai, spinJenisdokumen.getSelectedItem().toString(), 1);
                }
            });

            btnSedangservice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perintahCetak(tanggalMulai, tanggalSelesai, spinJenisdokumen.getSelectedItem().toString(), 2);
                }
            });

            btnSelesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perintahCetak(tanggalMulai, tanggalSelesai, spinJenisdokumen.getSelectedItem().toString(), 3);
                }
            });

            btnDiambil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perintahCetak(tanggalMulai, tanggalSelesai, spinJenisdokumen.getSelectedItem().toString(), 4);
                }
            });
        }

        return view;
    }

    private void initView(View view){
        tanggalMulai = "0000-00-00";
        tanggalSelesai = "0000-00-00";
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tglMulai = (EditText) view.findViewById(R.id.report_tv_tglmulai);
        tglSelesai = (EditText) view.findViewById(R.id.report_tv_tglselesai);
        spinJenisdokumen = (Spinner) view.findViewById(R.id.report_spin_jenisdokumen);
        btnHarusservice = (Button) view.findViewById(R.id.report_btn_harusdiservice);
        btnSedangservice = (Button) view.findViewById(R.id.report_btn_sedangdiservice);
        btnSelesai = (Button) view.findViewById(R.id.report_btn_sudahselesai);
        btnDiambil = (Button) view.findViewById(R.id.report_btn_sudahdiambil);
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
    }

    private void showDateDialog(int angka){

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                if (angka == 1){
                    tanggalMulai = dateFormatter.format(newDate.getTime()).toString();
                    tglMulai.setText(tanggalMulai);
                    Toast.makeText(getContext(), "Tanggal Dipilih : " + tanggalMulai, Toast.LENGTH_SHORT).show();
                }else if (angka == 2){
                    tanggalSelesai = dateFormatter.format(newDate.getTime()).toString();
                    tglSelesai.setText(tanggalSelesai);
                    Toast.makeText(getContext(), "Tanggal Dipilih : " + tanggalSelesai, Toast.LENGTH_SHORT).show();
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void perintahCetak(String tanggalmulai, String tanggalselesai, String jenisdokumen, int opsiCetak){
        Log.i("TANGGALMULAI", tanggalmulai);
        Log.i("TANGGALSELESAI", tanggalselesai);
        Log.i("JENISDOKUMEN", jenisdokumen);
        Log.i("OPSICETAK", String.valueOf(opsiCetak));
        if (tanggalmulai.equalsIgnoreCase("0000-00-00") || tanggalselesai.equalsIgnoreCase("0000-00-00")
        ){
            Toast.makeText(getContext(), "Pastikan Inputan Terisi", Toast.LENGTH_SHORT).show();
        }else {
            String token = sharedPreferences.getString("token", "");
            Call<ResponseCetak> responseCetakCall = dataService.apiCetaklaporan(token, tanggalmulai, tanggalselesai, jenisdokumen, opsiCetak);
            responseCetakCall.enqueue(new Callback<ResponseCetak>() {
                @Override
                public void onResponse(Call<ResponseCetak> call, Response<ResponseCetak> response) {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    if (response.code() == 200){
                        if (response.body().isBerhasil()){
                            String urlfileEnkripsi = Server.BASE_URL + "FileLaporan/" + response.body().getLokasifile();
                            downloadManagerrequest = new DownloadManager.Request(Uri.parse(urlfileEnkripsi));
                            downloadManagerrequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                    .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS , response.body().getLokasifile())
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                            downloadManager.enqueue(downloadManagerrequest);
                            Toast.makeText(getContext(), "Berhasil Mengunduh Laporan, Silahkan Cek File Manager", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Gagal Mencetak Laporan", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Gagal Mencetak Laporan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseCetak> call, Throwable t) {
                    Toast.makeText(getContext(), "Gagal Mencetak Laporan", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}