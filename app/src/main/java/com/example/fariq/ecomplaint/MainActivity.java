package com.example.fariq.ecomplaint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonAdd;
    private Button buttonView;
    private Button buttonAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button)findViewById(R.id.buttonAdd);
        buttonAbout = (Button) findViewById(R.id.buttonABout);
    }

    public void laporKeluhan(View v){
        startActivity(new Intent(MainActivity.this, awalpengaduan.class));
    }


    public void lihatKeluhan(View v){
        startActivity(new Intent(MainActivity.this, LihatPengaduan.class));
    }
    public void aboutUs(View v){
        startActivity(new Intent(MainActivity.this, tentangKami.class));
    }

}
