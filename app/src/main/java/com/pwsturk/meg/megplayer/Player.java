package com.pwsturk.meg.megplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    SeekBar sb;
    Button btPlay,btFF,btFB,btNxt,btPv;
    int position;
    Uri u;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay=(Button) findViewById(R.id.btPlay);
        btFB=(Button) findViewById(R.id.btFB);
        btFF=(Button) findViewById(R.id.btFF);
        btNxt=(Button) findViewById(R.id.btNxt);
        btPv=(Button) findViewById(R.id.btPv);
        btPlay.setBackgroundResource(R.drawable.btnpause);


        btPlay.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        sb =(SeekBar) findViewById(R.id.seekBar);
        updateSeekBar =new Thread() {
            @Override
            public void run() {
                //super.run();
                int totalDuration=mp.getDuration(); // mp3 ün süresi
                int currentPosition =0; // o anki konum
                while(currentPosition<totalDuration){
                    try {
                        sleep(500);//seekbar değişim için bekleme süresi 0.5s,  tahminen problemler burada
                        currentPosition =mp.getCurrentPosition(); // mp3 ün o anki konumu
                        sb.setProgress(currentPosition); // o anki konumu seekbar a at
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
            if(mp!=null){
            mp.stop();
            mp.release();
        }


        Intent i =getIntent();
        Bundle b=i.getExtras();
        mySongs=(ArrayList)b.getParcelableArrayList("songlist");
        position=b.getInt("pos", 0);

        Uri u=Uri.parse(mySongs.get(position).toString());
        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekBar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });


    }

    @Override
    public void onClick(View v) {
        int id =v.getId();

        switch (id){
            case R.id.btPlay:
                if(mp.isPlaying()){
                    btPlay.setText("PP");
                    mp.pause();
                }
                else {
                    btPlay.setText("II");
                    mp.start();
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                mp.stop();
                sb.setProgress(0);
                mp.release();
                position=((position+1)%mySongs.size());
                u=Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.btPv:
                mp.stop();
                sb.setProgress(0);
                mp.release();
                position=((position-1<0)?mySongs.size()-1: position-1);
                /*if(position-1<0){ // yukardaki işlemin açılmış hali :)
                    position=mySongs.size()-1;
                }
                else
                {
                    position=position-1;
                } */
                u=Uri.parse(mySongs.get(position).toString());
                mp= MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
        }
    }
}

