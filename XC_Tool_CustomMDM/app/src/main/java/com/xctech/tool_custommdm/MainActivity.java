package com.xctech.tool_custommdm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.custom.mdm.CustomAPI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomAPI.init(this);
        ((Button) findViewById(R.id.btn_nav_show)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setNavigationBar(true);
            }
        });
        ((Button) findViewById(R.id.btn_nav_hide)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setNavigationBar(false);
            }
        });
        ((Button) findViewById(R.id.btn_bk_show)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setBackKey(true);
            }
        });
        ((Button) findViewById(R.id.btn_bk_hide)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setBackKey(false);
            }
        });
        ((Button) findViewById(R.id.btn_hk_show)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setHomeKey(true);
            }
        });
        ((Button) findViewById(R.id.btn_hk_hide)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setHomeKey(false);
            }
        });
        ((Button) findViewById(R.id.btn_rk_show)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setRecentKey(true);
            }
        });
        ((Button) findViewById(R.id.btn_rk_hide)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setRecentKey(false);
            }
        });
        ((Button) findViewById(R.id.btn_sb_show)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setStatusBarDisplay(true);
            }
        });
        ((Button) findViewById(R.id.btn_sb_hide)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setStatusBarDisplay(false);
            }
        });
        ((Button) findViewById(R.id.btn_sb_en)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setStatusBar(true);
            }
        });
        ((Button) findViewById(R.id.btn_sb_dis)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.setStatusBar(false);
            }
        });
        ((Button) findViewById(R.id.btn_pk_en)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.disablePowerKey(false);
            }
        });
        ((Button) findViewById(R.id.btn_pk_dis)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAPI.disablePowerKey(true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomAPI.release();
    }
}