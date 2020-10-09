package com.skripsi.greenlab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        gridLayout=findViewById(R.id.gridLayout);
        setSingleEvent(gridLayout);
        mediaPlayer= MediaPlayer.create(MainActivity.this,R.raw.selamat_datang);
        mediaPlayer.start();
    }
    private void setSingleEvent(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI==0) {
                        Intent intent = new Intent(MainActivity.this, MasterActivity.class);
                        mediaPlayer.stop();
                        startActivity(intent);
                    }else if (finalI==1){
                        Intent intent = new Intent(MainActivity.this, TransaksiActivity.class);
                        mediaPlayer.stop();
                        startActivity(intent);
                    }else if(finalI==2){
                        Intent intent = new Intent(MainActivity.this, LaporanActivity.class);
                        mediaPlayer.stop();
                        startActivity(intent);
                    }else if(finalI==3){
                        Intent intent=new Intent(MainActivity.this,TentangActivity.class);
                        mediaPlayer.stop();
                        startActivity(intent);
                    }
                }
            });
        }
    }
}