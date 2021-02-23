package com.example.monitoringmotorlistrik.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.adapter.KerusakanmotorAdapter;
import com.example.monitoringmotorlistrik.model.Alatwo;
import com.example.monitoringmotorlistrik.model.Kerusakanmotor;
import com.example.monitoringmotorlistrik.model.Pemakai;
import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.Server;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;
import com.example.monitoringmotorlistrik.response.ResponseAlatwo;
import com.example.monitoringmotorlistrik.response.ResponseKerusakanmotor;
import com.example.monitoringmotorlistrik.response.ResponsePemakai;
import com.example.monitoringmotorlistrik.response.ResponseUmum;
import com.example.monitoringmotorlistrik.utils.FileUtils;
import com.example.monitoringmotorlistrik.utils.OnDeleteClickListener;
import com.example.monitoringmotorlistrik.utils.OnEditClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BelumdiperbaikiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BelumdiperbaikiFragment extends Fragment implements OnEditClickListener, OnDeleteClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private String tanggalmasuk;
    private String tanggalselesai;
    private String tanggalkeluar;

    private String lokasifotosesudah;

    private DataService dataService;

    private SharedPreferences sharedPreferences;

    private RecyclerView rvSudahdiperbaiki;
    private KerusakanmotorAdapter kerusakanmotorAdapter;

    private ImageView ivfotosebelum;
    private ImageView ivfotosesudah;
    private TextView tglmasuk;
    private TextView tglselesai;
    private TextView tglkeluar;
    private Spinner kodealatwo;
    private Spinner kodepemakai;
    private EditText materialbearing;
    private EditText materialkawatkg;
    private EditText nomorwo;
    private EditText keterangan;
    private EditText dll;

    private EditText cari;

    private String token;

    private ArrayList<Kerusakanmotor> kerusakanmotors = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    public BelumdiperbaikiFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BelumdiperbaikiFragment newInstance(String param1, String param2) {
        BelumdiperbaikiFragment fragment = new BelumdiperbaikiFragment();
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
        view = inflater.inflate(R.layout.fragment_belumdiperbaiki, container, false);
        initView(view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.belumdiperbaiki_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        rvSudahdiperbaiki.setAdapter(kerusakanmotorAdapter);
        rvSudahdiperbaiki.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                kerusakanmotorAdapter.getFilter().filter(cari.getText().toString());
            }
        });

        return view;
    }

    private void initView(View view){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        kerusakanmotorAdapter = new KerusakanmotorAdapter(getContext());
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        rvSudahdiperbaiki = (RecyclerView) view.findViewById(R.id.fragmenbelumdiperbaiki_rv);
        kerusakanmotorAdapter.setOnEditClickListener(this);
        kerusakanmotorAdapter.setOnDeleteClickListener(this);
        tanggalmasuk = "0000-00-00";
        tanggalselesai = "0000-00-00";
        tanggalkeluar = "0000-00-00";
        lokasifotosesudah = "";
        cari = view.findViewById(R.id.fragmenbelumdiperbaiki_cari);
        token = sharedPreferences.getString("token", "");
    }

    private void showDateDialog(int angka){
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                if (angka == 1){
                    tanggalmasuk = dateFormatter.format(newDate.getTime()).toString();
                    Toast.makeText(getContext(), "Tanggal Dipilih : " + tanggalmasuk, Toast.LENGTH_SHORT).show();
                }else if(angka == 2){
                    tanggalselesai = dateFormatter.format(newDate.getTime()).toString();
                    tglselesai.setText(tanggalselesai);
                    Toast.makeText(getContext(), "Tanggal Dipilih : " + tanggalselesai, Toast.LENGTH_SHORT).show();
                }else if (angka == 3){
                    tanggalkeluar = dateFormatter.format(newDate.getTime()).toString();
                    tglkeluar.setText(tanggalkeluar);
                    Toast.makeText(getContext(), "Tanggal Dipilih : " + tanggalkeluar, Toast.LENGTH_SHORT).show();
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void loadData(){
        //Menampung idPengguna yang ada dalam Shared Preference
        kerusakanmotors.clear();
        kerusakanmotorAdapter.clear();
        String token = sharedPreferences.getString("token","");
        Call<ResponseKerusakanmotor<List<Kerusakanmotor>>> loadKerusakanmotor = dataService.apiLihatkerusakanmotorbelum(token);
        loadKerusakanmotor.enqueue(new Callback<ResponseKerusakanmotor<List<Kerusakanmotor>>>() {

            @Override
            public void onResponse(Call<ResponseKerusakanmotor<List<Kerusakanmotor>>> call, Response<ResponseKerusakanmotor<List<Kerusakanmotor>>> response) {
                if (response.code() == 200){
                    kerusakanmotors.addAll(response.body().getKerusakanmotor());
                    kerusakanmotorAdapter.addAll(kerusakanmotors);
                    kerusakanmotorAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(), "Gagal Mengambil Kerusakan Motor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseKerusakanmotor<List<Kerusakanmotor>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil Kerusakan Motor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        String token = sharedPreferences.getString("token", "");
        String id = kerusakanmotors.get(position).getId();

        Call<ResponseUmum> callhapuskerusakan = dataService.apiHapuskerusakanmotor(token, id);
        callhapuskerusakan.enqueue(new Callback<ResponseUmum>() {
            @Override
            public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                if (response.code() == 200){
                    if (response.body().isBerhasil()){
                        Toast.makeText(getContext(), "Berhasil Menghapus Kerusakan Motor", Toast.LENGTH_SHORT).show();
                        loadData();
                    }else {
                        Toast.makeText(getContext(), "Gagal Menghapus Kerusakan Motor", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Gagal Menghapus Kerusakan Motor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUmum> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Menghapus Kerusakan Motor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(int position) {
        String id = kerusakanmotors.get(position).getId();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogtambahkerusakan = inflater.inflate(R.layout.layout_kelola_motor_rusak, null);
        dialog.setView(dialogtambahkerusakan);
        dialog.setCancelable(true);

        ivfotosebelum = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_iv_fotosebelum);
        ivfotosesudah = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_iv_fotosesudah);
        tglmasuk = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_tv_tglmasuk);
        tglselesai = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_tv_tglselesai);
        tglkeluar = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_tv_tglkeluar);
        kodealatwo = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_sp_kode_alat);
        kodepemakai = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_sp_kode_pemakai);
        materialbearing = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_material_bearing);
        materialkawatkg = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_material_kawat_kg);
        nomorwo = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_nomor_wo);
        keterangan = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_keterangan);
        dll = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_dll);

        if (kerusakanmotors.get(position).getFoto_sebelum() != null){
            Picasso.get().load(Server.BASE_URL + "FileUpload/Fotokerusakan/" + kerusakanmotors.get(position).getFoto_sebelum()).into(ivfotosebelum);
        }
        if (kerusakanmotors.get(position).getFoto_sesudah() != null){
            Picasso.get().load(Server.BASE_URL + "FileUpload/Fotokerusakan/" + kerusakanmotors.get(position).getFoto_sesudah()).into(ivfotosesudah);
        }

        tanggalmasuk = kerusakanmotors.get(position).getTglMasuk();
        tglmasuk.setText(kerusakanmotors.get(position).getTglMasuk());
        tglselesai.setText(kerusakanmotors.get(position).getTglSelesai());
        tglkeluar.setText(kerusakanmotors.get(position).getTglKeluar());
        Call<ResponseAlatwo<List<Alatwo>>> responseAlatwoCall = dataService.apiLihatalatwo(token);
        responseAlatwoCall.enqueue(new Callback<ResponseAlatwo<List<Alatwo>>>() {
            @Override
            public void onResponse(Call<ResponseAlatwo<List<Alatwo>>> call, Response<ResponseAlatwo<List<Alatwo>>> response) {
                List<Alatwo> alatwoArrayList = response.body().getAlatwo();
                List<String> listSpinner = new ArrayList<String>();
                for (int i=0;i<alatwoArrayList.size();i++){
                    listSpinner.add(alatwoArrayList.get(i).getKodeAlatWo());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                kodealatwo.setAdapter(adapter);
                for (int i=0;i<alatwoArrayList.size();i++){
                    if (alatwoArrayList.get(i).getKodeAlatWo().equalsIgnoreCase(kerusakanmotors.get(position).getKodeAlatWo())){
                        kodealatwo.setSelection(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAlatwo<List<Alatwo>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil Kode Alat", Toast.LENGTH_SHORT).show();
            }
        });

        Call<ResponsePemakai<List<Pemakai>>> responsePemakaiCall = dataService.apiLihatpemakai(token);
        responsePemakaiCall.enqueue(new Callback<ResponsePemakai<List<Pemakai>>>() {
            @Override
            public void onResponse(Call<ResponsePemakai<List<Pemakai>>> call, Response<ResponsePemakai<List<Pemakai>>> response) {
                List<Pemakai> pemakaiList = response.body().getPemakai();
                List<String> listSpinner = new ArrayList<String>();
                for (int i=0;i<pemakaiList.size();i++){
                    listSpinner.add(pemakaiList.get(i).getKodePemakai());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                kodepemakai.setAdapter(adapter);
                for (int i=0;i<pemakaiList.size();i++){
                    if (pemakaiList.get(i).getKodePemakai().equalsIgnoreCase(kerusakanmotors.get(position).getKodePemakai())){
                        kodepemakai.setSelection(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePemakai<List<Pemakai>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil Kode Alat", Toast.LENGTH_SHORT).show();
            }
        });

        kodealatwo.setEnabled(false);
        kodepemakai.setEnabled(false);
        materialbearing.setText(kerusakanmotors.get(position).getMaterialBearing());
        materialbearing.setEnabled(false);
        materialkawatkg.setText(kerusakanmotors.get(position).getMaterialKawatKg());
        materialkawatkg.setEnabled(false);
        nomorwo.setText(kerusakanmotors.get(position).getNomorWo());
        nomorwo.setEnabled(false);
        keterangan.setText(kerusakanmotors.get(position).getKeterangan());
        keterangan.setEnabled(false);
        dll.setText(kerusakanmotors.get(position).getDll());
        dll.setEnabled(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        ivfotosesudah.setOnClickListener(new View.OnClickListener() {
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

        tglselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(2);
            }
        });

        tglkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(3);
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                lokasifotosesudah = "";
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                lokasifotosesudah = "";
            }
        });

        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File foto = new File(lokasifotosesudah);
                String reqid = id;
                RequestBody reqtanggalmasuk = RequestBody.create(MediaType.parse("text/plain"), tanggalmasuk);
                RequestBody reqtanggalselesai = RequestBody.create(MediaType.parse("text/plain"), tanggalselesai);
                RequestBody reqtanggalkeluar = RequestBody.create(MediaType.parse("text/plain"), tanggalkeluar);
                RequestBody reqkodealatwo = RequestBody.create(MediaType.parse("text/plain"), kodealatwo.getSelectedItem().toString());
                RequestBody reqkodepemakai = RequestBody.create(MediaType.parse("text/plain"), kodepemakai.getSelectedItem().toString());
                RequestBody reqmaterialbearing = RequestBody.create(MediaType.parse("text/plain"), materialbearing.getText().toString());
                RequestBody reqmateialkawat = RequestBody.create(MediaType.parse("text/plain"), materialkawatkg.getText().toString());
                RequestBody reqnomorwo = RequestBody.create(MediaType.parse("text/plain"), nomorwo.getText().toString());
                RequestBody reqketerangan = RequestBody.create(MediaType.parse("text/plain"), keterangan.getText().toString());
                RequestBody reqdll = RequestBody.create(MediaType.parse("text/plain"), dll.getText().toString());
                RequestBody reqidpengguna = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString("id", ""));
                RequestBody requestBodyfile = RequestBody.create(MediaType.parse("/"), foto);
                MultipartBody.Part filefoto = MultipartBody.Part.createFormData("foto_sesudah", foto.getName(), requestBodyfile);

                if (lokasifotosesudah.equalsIgnoreCase("") || tanggalmasuk.equalsIgnoreCase("") || tanggalselesai.equalsIgnoreCase("") || tanggalkeluar.equalsIgnoreCase("")
                        || kodealatwo.getSelectedItem().toString().equalsIgnoreCase("") || kodepemakai.getSelectedItem().toString().equalsIgnoreCase("") || materialbearing.getText().toString().equalsIgnoreCase("")
                        || materialkawatkg.getText().toString().equalsIgnoreCase("") || nomorwo.getText().toString().equalsIgnoreCase("") || keterangan.getText().toString().equalsIgnoreCase("")
                        || dll.getText().toString().equalsIgnoreCase("") || sharedPreferences.getString("id", "").equalsIgnoreCase("")
                ){
                    Toast.makeText(getContext(), "Mohon Lengkapi Inputan", Toast.LENGTH_SHORT).show();
                }else {
                    Call<ResponseUmum> ubahkerusakanmotorCall = dataService.apiUbahkerusakanmotor(
                            token,
                            reqid,
                            reqtanggalmasuk,
                            reqtanggalselesai,
                            reqtanggalkeluar,
                            reqkodealatwo,
                            reqkodepemakai,
                            reqmaterialbearing,
                            reqmateialkawat,
                            reqnomorwo,
                            reqketerangan,
                            reqdll,
                            filefoto,
                            reqidpengguna
                    );

                    ubahkerusakanmotorCall.enqueue(new Callback<ResponseUmum>() {
                        @Override
                        public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                            if (response.code() == 200){
                                if (response.body().isBerhasil()){
                                    Toast.makeText(getContext(), "Berhasil Mengubah Kerusakan", Toast.LENGTH_SHORT).show();
                                    loadData();
                                }else {
                                    Toast.makeText(getContext(), "Pastikan Inputan Kode Alaw dan Kode Pemakai Benar", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "Pastikan Inputan Kode Alaw dan Kode Pemakai Benar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUmum> call, Throwable t) {
                            Toast.makeText(getContext(), "Pastikan Inputan Kode Alaw dan Kode Pemakai Benar", Toast.LENGTH_SHORT).show();
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
                        this.lokasifotosesudah = filePath;
                        Picasso.get().load(new File(lokasifotosesudah)).into(ivfotosesudah);
                        Toast.makeText(getContext(), "Foto Sudah Dipilih", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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

    @Override
    public void onRefresh() {
        loadData();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}