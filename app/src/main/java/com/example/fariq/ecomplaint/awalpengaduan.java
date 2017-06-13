package com.example.fariq.ecomplaint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class awalpengaduan extends AppCompatActivity implements View.OnClickListener,Validator.ValidationListener{
    private int successValidation = 0;
    private Validator validator;
    private Button buttonKtp,buttonInput,buttonBatal;
    private LinearLayout linearLayout2;

    private  String namaFile,namaFilePath,namaFileStr;
    @NotEmpty
    @Email
    private EditText editTextEmail ;

    @NotEmpty
    private EditText editTextNohp;
    @NotEmpty
    private EditText editTextNoId;
    @NotEmpty(message = "Anda Harus Upload Foto")


    private static final String PHOTOS_KEY = "easy_image_photos_list";
    private ArrayList<File> photos = new ArrayList<>();
    private File compressedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awalpengaduan);

        validator = new Validator(this);
        validator.setValidationListener(this);
        linearLayout2 = (LinearLayout)findViewById(R.id.linearLayout2);

        namaFile = "";
        namaFilePath = "";
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextNohp = (EditText) findViewById(R.id.editTextNohp);
        editTextNoId = (EditText) findViewById(R.id.editTextNoId);
        buttonKtp = (Button) findViewById(R.id.buttonKtp);
        buttonInput = (Button) findViewById(R.id.buttonInput);
        buttonBatal = (Button) findViewById(R.id.buttonBatal);


        buttonKtp.setOnClickListener(this);
        buttonInput.setOnClickListener(this);
        buttonBatal.setOnClickListener(this);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(awalpengaduan.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


        EasyImage.configuration(this)
                .setImagesFolderName("Ecomplaint Upload")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true);



    }


    @Override
    public void onClick(View v) {
        if(v == buttonKtp){
            photos.clear();
            linearLayout2.removeAllViewsInLayout();
            //To do Select image
            EasyImage.openChooserWithGallery(awalpengaduan.this, "Pilih Sumber Foto", 3);

        }
        else if (v == buttonInput){
            validator.validate();
            if(successValidation == 1){
                Intent intent = new Intent(awalpengaduan.this, laporKeluhanV2.class);
                intent.putExtra("a",editTextEmail.getText().toString().trim());
                intent.putExtra("b",editTextNohp.getText().toString().trim());
                intent.putExtra("c",editTextNoId.getText().toString().trim());
                intent.putExtra("d",namaFileStr);

                startActivity(intent);
            }
            else {

            }

        }
        else if (v == buttonBatal){
            startActivity(new Intent(awalpengaduan.this, MainActivity.class));
        }
    }


    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Masukkan Data Laporan...", Toast.LENGTH_SHORT).show();
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
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
        successValidation = 0;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling

            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type)  {
                onPhotosReturned(imageFiles);

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(awalpengaduan.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });

    }


    public String compressStringImage(File bmp){
        compressedImage = Compressor.getDefault(this).compressToFile(bmp);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bm = BitmapFactory.decodeFile(compressedImage.getAbsolutePath(),options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        bm.recycle();
        return encodedImage;
    }

    private void onPhotosReturned(List<File> returnedPhotos) {
        photos.addAll(returnedPhotos);
        ImageView image = new ImageView(this);
        File gambar = photos.get(photos.size()-1);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmapGambar = BitmapFactory.decodeFile(gambar.getAbsolutePath(),options);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(linearLayout2.getWidth()/3,linearLayout2.getHeight()-10);
        layoutParams.gravity = Gravity.CENTER;
        image.setLayoutParams(layoutParams);
        image.setPadding(2,2,2,2);
        image.setImageBitmap(bitmapGambar);
        // Adds the view to the layout
        linearLayout2.addView(image);
        namaFilePath = gambar.getAbsolutePath();
        namaFile = namaFilePath.substring(namaFilePath.lastIndexOf("/") + 1);
        namaFileStr = compressStringImage(gambar);
        bitmapGambar = null;

    }

    @Override
    protected void onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(this);
        super.onDestroy();

    }


}

