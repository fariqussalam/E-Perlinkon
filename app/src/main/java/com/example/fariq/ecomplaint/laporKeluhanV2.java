package com.example.fariq.ecomplaint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.TextViewDoubleAdapter;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import id.zelory.compressor.Compressor;


public class laporKeluhanV2 extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    private TextView textViewTuntutan;
    private LinearLayout ldisplay;
    private ArrayList<Uri> photos = new ArrayList<>();
    private String awalEmail,awalNohp,awalNoId,awalFotoKtp;
    private Bitmap bitmap2,bitmap3,bitmap4;
    private String im2String,im3String,im4String;
    private int successValidation;
    private Validator validator;
    private String jenkel,jenisTuntutan;
    @NotEmpty(message = "Kolom Pengaduan Tidak Boleh Kosong")
    private EditText editTextPengaduan;
    private Button buttonUploadBukti,buttonKirim,buttonBack,buttonHome;
    private Spinner spinnerJenkel,spinnerJenisTuntutan;
    private String pil[] = {
            "Pilih Jenis Pengaduan","Pengaduan", "Penyampaian Informasi"
    };
    private String tuntutan[] = {
            "Tidak Ada", "Pengembalian Uang", "Penggantian Barang/Jasa", "Pemberian Santunan"
    };
    private int REQUEST_CODE_CHOOSE = 23;
    private List<Uri> mSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor_keluhan_v2);


        validator = new Validator(this);
        validator.setValidationListener(this);
        buttonUploadBukti = (Button) findViewById(R.id.buttonUploadBukti);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonKirim = (Button) findViewById(R.id.buttonKirim);
        buttonHome = (Button) findViewById(R.id.buttonBatal);
        editTextPengaduan = (EditText) findViewById(R.id.editText2);
        textViewTuntutan = (TextView) findViewById(R.id.textView4);

        Bundle bundle = getIntent().getExtras();
        awalEmail = bundle.getString("a");
        awalNohp = bundle.getString("b");
        awalNoId = bundle.getString("c");
        awalFotoKtp = bundle.getString("d");


        buttonHome.setOnClickListener(this);
        buttonUploadBukti.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonKirim.setOnClickListener(this);
        spinnerJenkel = (Spinner) findViewById(R.id.spinner3);
        spinnerJenisTuntutan = (Spinner) findViewById(R.id.spinner4);

        spinnerJenisTuntutan.setVisibility(View.GONE);
        textViewTuntutan.setVisibility(View.GONE);

        ldisplay = (LinearLayout) findViewById(R.id.linearLayout);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, pil);

        // mengeset Array Adapter tersebut ke Spinner
        spinnerJenkel.setAdapter(adapter);

        // mengeset listener untuk mengetahui saat item dipilih
        spinnerJenkel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                jenkel = adapter.getItem(i);
                if (jenkel == "Pengaduan"){
                    textViewTuntutan.setVisibility(View.VISIBLE);
                    spinnerJenisTuntutan.setVisibility(View.VISIBLE);
                } else {
                    textViewTuntutan.setVisibility(View.GONE);
                    spinnerJenisTuntutan.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                jenkel = "";
            }
        });

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, tuntutan);

        // mengeset Array Adapter tersebut ke Spinner
        spinnerJenisTuntutan.setAdapter(adapter2);

        // mengeset listener untuk mengetahui saat item dipilih
        spinnerJenisTuntutan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                jenisTuntutan = adapter2.getItem(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                jenisTuntutan = "";
            }
        });

        spinnerJenisTuntutan.setVisibility(View.GONE);
        textViewTuntutan.setVisibility(View.GONE);




    }

    @Override
    public void onClick(View v) {
        if (v == buttonUploadBukti){
            ldisplay.removeAllViewsInLayout();
            photos.clear();
            Matisse.from(laporKeluhanV2.this)
                    .choose(MimeType.allOf())
                    .countable(true)
                    .maxSelectable(3)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        }
        else if (v == buttonKirim){
            validator.validate();
            if (successValidation == 1){
                addKeluhan();
            }
            else{

            }
        }
        else if(v == buttonBack){
            Intent intent = new Intent(laporKeluhanV2.this,awalpengaduan.class);
            startActivity(intent);
        }
        else if(v == buttonHome){
            Intent intent = new Intent(laporKeluhanV2.this,MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
            onPhotosReturned(mSelected);
        }
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Harap Tunggu...", Toast.LENGTH_SHORT).show();
        successValidation = 1;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
            else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
        successValidation = 0;

    }



    public String getStringImage(Uri ImageUri){
        File gambar = new File(getPath(this,ImageUri));
        Bitmap bmp = Compressor.getDefault(this).compressToBitmap(gambar);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        bmp.recycle();
        return encodedImage;
    }





    private void onPhotosReturned(List<Uri> returnedPhotos) {
        photos.addAll(returnedPhotos);
        for(int i=0;i<photos.size();i++)
        {
            ImageView image = new ImageView(this);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(200,200));
            image.setPadding(2,2,2,2);
            image.setMaxHeight(300);
            image.setMaxWidth(150);
            Uri ImageUri = photos.get(i);
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(),ImageUri);
                if (i == 0){
                    im2String = getStringImage(ImageUri);
                } else if (i == 1){
                    im3String =  getStringImage(ImageUri);
                } else if (i == 2){
                    im4String =  getStringImage(ImageUri);
                }

                image.setImageBitmap(bm);
                // Adds the view to the layout
                ldisplay.addView(image);
                if (photos.size() == 0){
                    im2String = "kosong";
                    im3String = "kosong";
                    im4String = "kosong";
                } else if (photos.size() == 1){
                    im3String = "kosong";
                    im4String = "kosong";
                } else if (photos.size() == 2){
                    im4String = "kosong";
                }
                bm.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void addKeluhan(){
        final String email = awalEmail;
        final String nohp = awalNohp;
        final String noktp = awalNoId;
        final String imktp = awalFotoKtp;
        final String keluhan = editTextPengaduan.getText().toString().trim();
        final String im2 = im2String;
        final String im3 = im3String;
        final String im4 = im4String;
        final String jenisPeng = jenkel;
        final String jenisTun = jenisTuntutan;
        Random r = new Random();
        SimpleDateFormat format= new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        String myDate = format.format(new Date());
        final String generatedNumber = String.valueOf(r.nextInt(999 - 111) + 111);
        final String noPengaduan = myDate + generatedNumber;




        class AddKeluhan extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(laporKeluhanV2.this,"Mengirim...","Silahkan Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(laporKeluhanV2.this,s,Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(laporKeluhanV2.this);
                builder1.setMessage("Terima Kasih telah mengirimkan laporan Anda \n No. Pengaduan Anda : " + noPengaduan);
                builder1.setCancelable(false);

                builder1.setNeutralButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(laporKeluhanV2.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_KEL_EMAIL,email);
                params.put(Config.KEY_KEL_NOHP,nohp);
                params.put(Config.KEY_KEL_NOKTP,noktp);
                params.put(Config.KEY_KEL_DETKEL,keluhan);
                params.put(Config.KEY_KEL_IMKTP,imktp);
                params.put(Config.KEY_KEL_IM2,im2);
                params.put(Config.KEY_KEL_IM3,im3);
                params.put(Config.KEY_KEL_IM4,im4);
                params.put(Config.KEY_KEL_NOPENGADUAN,noPengaduan);
                params.put(Config.KEY_KEL_JENKEL,jenisPeng);
                params.put(Config.KEY_KEL_JENTUT,jenisTun);



                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }

        AddKeluhan ak = new AddKeluhan();
        ak.execute();


    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}
