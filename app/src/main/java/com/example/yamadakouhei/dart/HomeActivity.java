package com.example.yamadakouhei.dart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


public class HomeActivity extends Activity implements CompoundButton.OnCheckedChangeListener{
    int tb11 = 1, tb22 = 1;
    ToggleButton tb1, tb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button DartButton;
        DartButton = findViewById(R.id.button);

        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        toggleButton.setOnCheckedChangeListener(this);

        tb1 = (ToggleButton) findViewById(R.id.toggleButton);
        tb2 = (ToggleButton) findViewById(R.id.toggleButton2);

        // リスナーの登録
        tb1.setOnCheckedChangeListener(this);
        tb2.setOnCheckedChangeListener(this);

            DartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if (tb11 % 2 == 0) {
                        tb11 = 0;
                    } else {
                        tb11 = 1;
                    }
                    if (tb22 % 2 == 0) {
                        tb22 = 0;
                    } else {
                        tb22 = 1;
                    }


                    Intent intent = new Intent(getApplication(), EffectActivity.class);
                    intent.putExtra("tb1", tb11);
                    intent.putExtra("tb2", tb22);
                    startActivity(intent);

                }

            });

    }

    //駅指定、観光地指定のオンオフを判定
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if (buttonView.getId() == R.id.toggleButton) {
            Toast.makeText(this, "clicked togglebutton1: "+isChecked,
                    Toast.LENGTH_LONG).show();
            tb11++;
        }else if (buttonView.getId() == R.id.toggleButton2) {
            Toast.makeText(this, "clicked togglebutton2: "+isChecked,
                    Toast.LENGTH_LONG).show();
            tb22++;
        }


    }

    //戻るボタンを押した時の処理
    int i = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
           // Intent intent = new Intent(getApplication(), HomeActivity.class);
           // startActivity(intent);


            if (i==0){
                Toast.makeText(this, " 終了するには戻るをもう一度タップ ",
                        Toast.LENGTH_LONG).show();
                i++;
            }else{
                this.moveTaskToBack(true);
            }

            return true;
        }
        return false;

    }
}
