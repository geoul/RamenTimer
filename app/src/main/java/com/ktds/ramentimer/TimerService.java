package com.ktds.ramentimer;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TimerService extends Service {

    // Toast 를 사용하기 위해서는 Handler 를 사용한다.
    private Handler handler;
    // 글씨를 읽어주는 역할을 한다.
    private TextToSpeech tts;
    private int i;
    private String ramen;

    private boolean isBinding; // 기본값 false

    // Stub 객체를 정의만 해서는 쓸 수 없고 onBind()에서 return 해줘야 한다.
    private IMyTimerInterface.Stub binder = new IMyTimerInterface.Stub() {
        @Override
        public int getCount() throws RemoteException {
            return i;
        }

//        @Override
//        public String getInterfaceDescriptor() {
//            return ramen;
//        }
    };

    public TimerService() {
    }

    // Service 가 만들어지면 수행된다.
    @Override
    public void onCreate() {
        super.onCreate();

        isBinding = true;

        handler = new Handler();
        i = 180;
//        ramen = "";
        Thread counter = new Thread(new CountThread());
        counter.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        isBinding = true;
        Log.d("COUNT", "BIND");
        i = intent.getIntExtra("TIME", 0);
//        ramen = intent.getStringExtra("RAMEN");

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("COUNT", "UNBIND");
        isBinding = false;

        while ( i != -1 ) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

            Log.d("COUNT", "기다리는 중...");
        }

        i = 180;

        return true;
    }

    class CountThread implements Runnable {

        @Override
        public void run() {

            for ( ; i >= 0; i--) {
                if ( !isBinding ) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            tts.setLanguage(Locale.ENGLISH); // 말하는 언어
                            tts.setSpeechRate(1.0f); // 말하는 속도

                            tts.speak("Counting End", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    });
                }
            });

            i = -1;

        }
    }
}
