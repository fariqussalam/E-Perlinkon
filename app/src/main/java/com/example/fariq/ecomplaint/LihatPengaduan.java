package com.example.fariq.ecomplaint;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;


public class LihatPengaduan extends AppCompatActivity implements Validator.ValidationListener {

    private Button buttonKirim,buttonKembali;

    Validator validator;

    @NotEmpty(message = "Anda Harus Memasukkan No Pengaduan")
    private EditText noPeng;

    private String noPengaduan;
    private int successValidation;
    private String a,b,c1,d,e,f,g,h,i,j;
    TextView isiNoPeng,isiEmail,isiNoKtp,isiNoHp,isiJenkel,isiDetkel,isiSolusi,isiWaktu,isiJentut,isiStatus;
    TextView tvNoPeng,tvEmail,tvNoKtp,tvNoHp,tvJenkel,tvDetkel,tvSolusi,tvWaktu,tvJentut,tvStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_pengaduan);

        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonKirim = (Button) findViewById(R.id.buttonCek);
        buttonKembali = (Button) findViewById(R.id.buttonKembali);
        noPeng = (EditText) findViewById(R.id.noPeng);




    }

   // @Override
   // public void onClick(View v) {
   //     if (v == buttonKirim){
    //        noPengaduan = noPeng.getText().toString().trim();
    //        buttonKirim.setText(noPengaduan);
    //        getEmployee(noPengaduan);
    //    }
    //    else if (v == buttonKembali){
     //       startActivity(new Intent(LihatPengaduan.this, MainActivity.class));
      //  }

    //}

    private void getEmployee(final String idPeng){
        class GetEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatPengaduan.this,"Loading...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                    showEmployee(s);

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_KEL,idPeng);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String email = c.getString(Config.TAG_EMAIL);
            String nohp = c.getString(Config.TAG_NOHP);
            String noId = c.getString(Config.TAG_NOKTP);
            String jenkel = c.getString(Config.TAG_JENKEL);
            String detkel = c.getString(Config.TAG_DETKEL);
            String status = c.getString(Config.TAG_STATUS);
            String solusi = c.getString(Config.TAG_SOLUSI);
            String waktu = c.getString(Config.TAG_WAKTU);
            String jentut = c.getString(Config.TAG_JENTUT);

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LihatPengaduan.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_detail,null);
                TextView isiNoPeng = (TextView) mView.findViewById(R.id.textViewIsiNoPengaduan);
                TextView isiEmail = (TextView) mView.findViewById(R.id.textViewIsiEmail);
                TextView isiNoHp = (TextView) mView.findViewById(R.id.textViewIsiNoHp);
                TextView isiNoKtp = (TextView) mView.findViewById(R.id.textViewIsiNoKtp);
                TextView isiJenkel = (TextView) mView.findViewById(R.id.textViewIsiJenisPengaduan);
                TextView isiDetkel = (TextView) mView.findViewById(R.id.textViewIsiDetailPengaduan);
                TextView isiStatus = (TextView) mView.findViewById(R.id.textViewIsiStatusPengaduan);
                TextView isiSolusi = (TextView) mView.findViewById(R.id.textViewIsiSolusiPengaduan);
                TextView isiWaktu = (TextView) mView.findViewById(R.id.textViewIsiWaktuPengaduan);
                TextView isiJentut = (TextView) mView.findViewById(R.id.textViewIsiJenisTuntutan);
                Button buttonOK = (Button) mView.findViewById(R.id.buttonOkPengaduan);

                isiNoPeng.setText(noPengaduan);
                isiEmail.setText(email);
                isiNoHp.setText(nohp);
                isiNoKtp.setText(noId);
                isiJenkel.setText(jenkel);
                isiDetkel.setText(detkel);
                isiStatus.setText(status);
                isiSolusi.setText(solusi);
                isiWaktu.setText(waktu);
                isiJentut.setText(jentut);

                mBuilder.setView(mView);
                final AlertDialog mAlert = mBuilder.create();
                mAlert.show();

            buttonOK.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mAlert.dismiss();
                }
            });


        } catch (JSONException e)
        {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(LihatPengaduan.this);
            builder2.setMessage("No Pengaduan Anda Tidak Tercatat");
            builder2.setCancelable(false);
            builder2.setNeutralButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert12 = builder2.create();
            alert12.show();

            e.printStackTrace();
        }
    }


    public void kirimNoPeng(View view) {
        validator.validate();
        if (successValidation == 1){
            noPengaduan = noPeng.getText().toString().trim();
         getEmployee(noPengaduan);



        }
        else {
            //do nothing
        }

    }

    public void kembaliMain(View view) {
        startActivity(new Intent(LihatPengaduan.this, MainActivity.class));
    }

    @Override
    public void onValidationSucceeded() {
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
}
