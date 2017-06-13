package com.example.fariq.ecomplaint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import org.w3c.dom.Text;

import java.util.List;

public class lihatPengaduanDetail extends AppCompatActivity implements View.OnClickListener{
    private int successValidation;

    TextView noPeng,email,noId,noHp,jenkel,jentut,detkel,status,solusi,waktu;
    Button buttonOk;
    String one,two,three,four,five,six,seven,eight,nine,ten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_pengaduan_detail);
        buttonOk = (Button) findViewById(R.id.buttonLihatOK);
        noPeng = (TextView) findViewById(R.id.tvnoPeng);
        email = (TextView) findViewById(R.id.tvEmail);
        noId = (TextView) findViewById(R.id.tvnoId);
        noHp = (TextView) findViewById(R.id.tvnohp);
        jenkel = (TextView) findViewById(R.id.tvjenkel);
        jentut = (TextView) findViewById(R.id.tvJentut);
        detkel = (TextView) findViewById(R.id.tvDetail);
        status = (TextView) findViewById(R.id.tvStatus);
        solusi = (TextView) findViewById(R.id.tvSolusi);
        waktu = (TextView) findViewById(R.id.textViewWaktu);

        buttonOk.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        one = bundle.getString("a");
        two = bundle.getString("b");
        three = bundle.getString("c");
        four = bundle.getString("d");
        five = bundle.getString("e");
        six = bundle.getString("f");
        seven = bundle.getString("g");
        eight = bundle.getString("h");
        nine = bundle.getString("i");
        ten = bundle.getString("j");

        noPeng.setText(ten);
        email.setText(one);
        noId.setText(three);
        noHp.setText(two);
        jenkel.setText(four);
        jentut.setText(nine);
        detkel.setText(five);
        solusi.setText(seven);
        status.setText(six);
        waktu.setText(eight);





    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(lihatPengaduanDetail.this, MainActivity.class));
    }
}
