package com.example.yamadakouhei.dart;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //スプラッシュスクリーン
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);//画面遷移のためのIntentを準備
                startActivity(intent);//実際の画面遷移を開始
                finish();//現在のActivityを削除
            }
        }, 2000);//2秒後にrun()を行う

    }
}
