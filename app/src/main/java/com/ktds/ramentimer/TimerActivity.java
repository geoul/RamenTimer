package com.ktds.ramentimer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ktds.ramentimer.model.RamenModel;

public class TimerActivity extends AppCompatActivity {

    private boolean isBinding; // 기본값 false

    private TextView tv_ramen;
    private TextView tv_minute;
    private TextView tv_second;
    private Button btn_timer_start;
    private Button btn_timer_stop;

    private Handler handler; // Thread 에서 UI 에 접근하기 위해서 사용한다.

    private IMyTimerInterface binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { // 현재 요청되고 있는 binder 가 파라미터로 들어온다.
            binder = IMyTimerInterface.Stub.asInterface(service);  // Service 에 있는 binder 와 Activity 에 있는 binder 가 같아진다. (동일시 시켜준다)
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        handler = new Handler();

        tv_ramen = (TextView) findViewById(R.id.tv_ramen);
        tv_minute = (TextView) findViewById(R.id.tv_minute);
        tv_second = (TextView) findViewById(R.id.tv_second);
        btn_timer_start = (Button) findViewById(R.id.btn_timer_start);
        btn_timer_stop = (Button) findViewById(R.id.btn_timer_stop);

//        Intent intent = getIntent();
//        intent.getStringExtra("RAMEN");

        btn_timer_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !isBinding ) {
                    Intent intent = new Intent(TimerActivity.this, TimerService.class);
                    intent.putExtra("TIME", 180);
                    bindService(intent, connection, BIND_AUTO_CREATE);

                    Thread thread = new Thread(new GetCountThread());
                    thread.start();
                }
            }
        });

        btn_timer_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( isBinding ) {
                    isBinding = false;
                    unbindService(connection);
                }
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1000 && resultCode == RESULT_OK) {
//            String result = data.getStringExtra("RAMEN");
//            tv_ramen.setText(result);
//        }
//    }

    // Thread 를 띄운다.
    class GetCountThread implements Runnable {

        int count = 180;

        @Override
        public void run() {

            isBinding = true;

            // service 가 언제 끝날 지 모르기 때문에 무한반복을 시킨다.
            while ( isBinding ) {

                if ( binder == null ) {
                    continue;
                }

                try {
                    count = binder.getCount();
                    if ( count == -1 ) {
                        break;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_minute.setText(count/60 + "");
                        tv_second.setText(count%60 + "");
                    }
                });

                // sleep() 을 하지 않으면 Thread 가 계속 돌아간다.
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            if ( isBinding ) {
                TimerActivity.this.unbindService(connection);
            }

            isBinding = false;

        }
    }
}
