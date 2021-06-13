package com.mehmetsakiratasayin.dailycalorierecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Hakkinda extends AppCompatActivity {
    Switch aSwitch;
    MediaPlayer ply;
    SharedPreferences sharedPreferences;
    String firstShared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakkinda);
        ply=MediaPlayer.create(Hakkinda.this,R.raw.song);

        sharedPreferences =this.getSharedPreferences("com.mehmetsakiratasayin.dailycalorierecord", Context.MODE_PRIVATE);

        aSwitch=findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(
                        isChecked==true){
                    ply.start();
                }
                else{
                    ply.pause();
                }
            }
        });
    }


    public void okey(View view) {

        firstShared = sharedPreferences.getString("firstkey", "first");
        if(firstShared=="first"){
            ply.stop();
            Intent intent = new Intent(Hakkinda.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            String first = "Last";
            sharedPreferences.edit().putString("firstkey", first).apply();
            startActivity(intent);
        }
        else{
            ply.stop();
            Intent intent = new Intent(Hakkinda.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
finish();
            startActivity(intent);
        }

    }
}