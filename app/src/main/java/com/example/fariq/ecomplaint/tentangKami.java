package com.example.fariq.ecomplaint;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class tentangKami extends AppCompatActivity {

    Spanned tentangKamiHtml;
    TextView textHTML;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_kami);
        textHTML = (TextView) findViewById(R.id.textViewHtml);
        getEmployee();

    }

    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void  onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(tentangKami.this,"Loading...","Tunggu...",false,false);
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
                String s = rh.sendGetRequest(Config.URL_ABOUT);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    @SuppressWarnings("deprecation")
    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String htmlContent = c.getString(Config.TAG_KONTEN);
            textHTML.setText(Html.fromHtml(htmlContent));

        } catch (JSONException e)
        {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(tentangKami.this);
            builder2.setMessage("Error Mengambil Data Dari Server. Periksa Kembali Koneksi Internet Anda");
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
}
