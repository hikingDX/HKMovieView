package com.hiking.mymovieview0929;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SSView mSSView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSSView = (SSView) findViewById(R.id.ssview);
        mSSView.setOnSeatClickListener(new SSView.OnSeatClickListener() {
            @Override
            public boolean a(int row, int colums, boolean isChecked) {
                Toast.makeText(MainActivity.this,"你点击了"+row+"---"+colums,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}
