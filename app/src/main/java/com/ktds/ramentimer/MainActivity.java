package com.ktds.ramentimer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ll_neoguri;
    private LinearLayout ll_anseongtangmean;
    private LinearLayout ll_jin_ramen;
    private TextView tv_neoguri;
    private TextView tv_anseongtangmean;
    private TextView tv_jin_ramen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll_neoguri = (LinearLayout) findViewById(R.id.ll_neoguri);
        ll_anseongtangmean = (LinearLayout) findViewById(R.id.ll_anseongtangmean);
        ll_jin_ramen = (LinearLayout) findViewById(R.id.ll_jin_ramen);

        tv_neoguri = (TextView) findViewById(R.id.tv_neoguri);
        tv_anseongtangmean = (TextView) findViewById(R.id.tv_anseongtangmean);
        tv_jin_ramen = (TextView) findViewById(R.id.tv_jin_ramen);

        ll_neoguri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                intent.putExtra("Ramen", tv_neoguri.getText().toString());
                startActivity(intent);
            }
        });

        ll_anseongtangmean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                intent.putExtra("Ramen", tv_anseongtangmean.getText().toString());
                startActivity(intent);
            }
        });

        ll_jin_ramen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                intent.putExtra("Ramen", tv_jin_ramen.getText().toString());
                startActivityForResult(intent, 1000);
            }
        });
    }
}
