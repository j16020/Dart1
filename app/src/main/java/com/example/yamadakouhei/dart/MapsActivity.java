package com.example.yamadakouhei.dart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private final static int RESULT_CAMERA = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button RootB;
        RootB = findViewById(R.id.button6);
        RootB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                test1();

            }



        });

        Button CameraB;
        CameraB = findViewById(R.id.button2);
        CameraB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);


            }
        });


            }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA&&data!=null) {
            Bitmap bitmap;
            // cancelしたケースも含む
            if (data.getExtras() == null) {
                Log.d("debug", "cancel ?");
                return;
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");

                // 画像サイズを計測
                int bmpWidth = bitmap.getWidth();
                int bmpHeight = bitmap.getHeight();
                Log.d("debug", String.format("w= %d", bmpWidth));
                Log.d("debug", String.format("h= %d", bmpHeight));
            }

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        Intent intent = getIntent();

        Double idoS = 0.0;
        idoS = intent.getDoubleExtra("idoS",idoS);

        Double keidoS = 0.0;
        keidoS = intent.getDoubleExtra("keidoS",keidoS);



        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(idoS,keidoS);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(idoS,keidoS), 8));

        LatLng location = new LatLng(idoS, keidoS);
        MarkerOptions options = new MarkerOptions();
        options.position(location);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.dart4);
        options.icon(icon);
        mMap.addMarker(options);
    }
    private void test1(){
        // 起点の緯度経度
        Intent intent = getIntent();

        Double idoS = 0.0;
        idoS = intent.getDoubleExtra("idoS",idoS);
       String idos = String.valueOf(idoS);

        Double keidoS = 0.0;
        keidoS = intent.getDoubleExtra("keidoS",keidoS);
        String keidos = String.valueOf(keidoS);


        String src_lat = "34.971880";
        String src_ltg = "138.388970";

        // 目的地の緯度経度
        String des_lat = idos;
        String des_ltg = keidos;



        intent.setAction(Intent.ACTION_VIEW);

        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");

        // 起点の緯度,経度, 目的地の緯度,経度
        String str = String.format(Locale.US,
                "http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s",
                src_lat, src_ltg, des_lat, des_ltg);

        intent.setData(Uri.parse(str));
        startActivity(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
            return true;
        }
        return false;

    }




}
