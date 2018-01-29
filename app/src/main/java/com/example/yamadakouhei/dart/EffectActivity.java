package com.example.yamadakouhei.dart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class EffectActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_effect);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        final Button DartButton = findViewById(R.id.button4);
        final VideoView videoView = (VideoView) findViewById(R.id.videoView);

        String fileName = "android.resource://" + getPackageName() + "/" + R.raw.effect;
        videoView.setVideoPath(fileName);

        DartButton.setText("画面タップでダーツを投げる");

        DartButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                repairs();
                return false;
            }
        });
        DartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                stop();

                double ido = 0.0, keido = 0.0;

                SetPlaces setp = new SetPlaces();
                SetPlaces value = setp.setplace(ido, keido);

                Intent intent = new Intent(getApplication(), MapsActivity.class);
                intent.putExtra("idoS", value.ido);
                intent.putExtra("keidoS", value.keido);
                startActivity(intent);

            }

        });

    }
    // 緯度経度を入れて経路を検索


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    //button4を非表示
    public void repairs() {
        findViewById(R.id.button4).setVisibility(View.INVISIBLE);
    }

    //５秒間停止
    public void stop() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    //座標を指定するクラス
    class SetPlaces {


        double ido = 0.0, keido = 0.0;


        public SetPlaces setplace(double ido, double keido) {

            SetPlaces setp = new SetPlaces();
            Boolean eki = true, kanko = true;

            //Home画面のトグルボタンの状態を取得する
            Intent intent = getIntent();

            int tb1 = 0, tb2 = 0;
            tb1 = intent.getIntExtra("tb1", tb1);
            tb2 = intent.getIntExtra("tb2", tb2);

            //トグルボタンの状態反映
            if (tb1 == 1) {
                eki = true;
            } else {
                eki = false;
            }
            if (tb2 == 1) {
                kanko = true;
            } else {
                kanko = false;
            }

            //トグルボタンが両方オンだった時
            if (eki == true && kanko == true) {
                Random rnd8 = new Random();
                int rnd7 = rnd8.nextInt(2);

                if (rnd7 == 0) {
                    eki = false;
                }
                if (rnd7 == 1){
                    kanko = false;
                }
                if (rnd7 == 2) {
                    kanko = false;
                }
            }

            //観光地座標群
            double ido1[] = {43.555605, 45.522889, 41.329643, 39.000579, 38.369728, 39.929218, 37.651139, 36.757695, 36.702555, 35.157666, 27.077190, 35.625055, 35.286049, 36.973428, 36.566513, 37.425283, 36.237656, 35.475099, 36.248754, 36.183675, 35.362769, 35.312750, 35.184901, 35.318263, 34.454758, 34.691562, 33.674208, 34.652538, 35.025252, 34.839356, 35.542684, 34.595729, 35.401823, 34.295911, 33.933915, 34.238757, 34.182307, 33.127938, 33.343054, 33.593371, 32.740053, 32.984376, 32.880612, 31.803903, 30.360760, 24.449871, 24.839533};
            double keido1[] = {144.506947, 141.936502, 141.091545, 141.097020, 141.064235, 139.766825, 140.064603, 139.600586, 139.207425, 139.834635, 142.205074, 139.243600, 139.693980, 138.749527, 137.662113, 136.999809, 136.125458, 138.666933, 137.638009, 137.424553, 138.730754, 138.587542, 136.899704, 136.161245, 136.725454, 135.794243, 135.887612, 135.506333, 135.762758, 134.693939, 134.227343, 133.771725, 132.685416, 132.319926, 130.931235, 134.643439, 134.085713, 132.817726, 132.014838, 130.351471, 130.261161, 131.520655, 131.084132, 131.475478, 130.531893, 122.934295, 125.280574};

            //観光地指定の時
            if (kanko == true && eki == false) {
                Random rnd3 = new Random();
                int rnd4 = rnd3.nextInt(45);
                setp.ido = ido1[rnd4];
                setp.keido = keido1[rnd4];
            }

            double ido2[] = {34.971923,35.681285,35.465759,38.259941,43.068921,35.170848,34.977923,34.702138,34.985430,34.397723,33.589740,45.416689,40.828330,37.912010,34.350685,35.212487,34.827323,35.347158,31.583625,33.886900,26.219131};
            double keido2[] = {138.388941,139.766482,139.622431,140.882173,141.350633,136.881317,138.338941,135.495567,135.758426,132.476130,130.420300,141.677245,140.693477,139.061956,134.045823,138.221304,134.690509,140.376683,130.541789,130.882425,127.725568};

            //駅指定がオンで観光地指定がオフ
            if (kanko == false && eki == true) {
                Random rnd5 = new Random();
                int rnd6 = rnd5.nextInt(21);
                setp.ido = ido2[rnd6];
                setp.keido = keido2[rnd6];
            }

            return setp;


        }
    }
}
