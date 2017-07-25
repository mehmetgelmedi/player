package com.pwsturk.meg.megplayer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mp;
    static ArrayList<File> mySongs;
    static SeekBar sb;
    static Button btPlay;
    Button btFF;
    Button btFB;
    Button btNxt;
    Button btPv;
    static ImageView imgResim;
    static int position;
    static Uri u;
    Thread updateSeekBar;
    private static Context context;
    static TextView tvParcaAdi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Player.context =getApplicationContext();
        btPlay=(Button) findViewById(R.id.btPlay);
        btFB=(Button) findViewById(R.id.btFB);
        btFF=(Button) findViewById(R.id.btFF);
        btNxt=(Button) findViewById(R.id.btNxt);
        btPv=(Button) findViewById(R.id.btPv);
        imgResim = (ImageView) findViewById(R.id.resim);
        
        btPlay.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        tvParcaAdi=(TextView) findViewById(R.id.parcaAdi);

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
                        Log.d("tag",currentPosition+""); // unutma
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
        tvParcaAdi.setText(mySongs.get(position).getName().toString().replace(".mp3", ""));

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
                playorstop();
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                next();
                AnimationGetir();
                bildirimEkle(position);
                break;
            case R.id.btPv:
                prev();
                AnimationGetir();
                bildirimEkle(position);
                break;
        }
    }
    public static void bildirimEkle(int oynId) {
        new Notification(getAppContext()).bildirimEkle(mySongs.get(position).getName());
    }
    public static void playorstop(){
        if(mp.isPlaying()){
            btPlay.setBackgroundResource(R.drawable.play);
            mp.pause();
        }
        else {
            btPlay.setBackgroundResource(R.drawable.stop);
            mp.start();
        }
    }
    public static void next(){
        mp.stop();
        sb.setProgress(0);
        mp.release();
        position=((position+1)%mySongs.size());
        u=Uri.parse(mySongs.get(position).toString());
        mp=MediaPlayer.create(getAppContext(), u);
        mp.start();
        bildirimEkle(position);
        sb.setMax(mp.getDuration());
        tvParcaAdi.setText(mySongs.get(position).getName().toString().replace(".mp3", ""));
    }
    public static void prev(){
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
        mp= MediaPlayer.create(getAppContext(),u);
        mp.start();
        bildirimEkle(position);
        sb.setMax(mp.getDuration());
        tvParcaAdi.setText(mySongs.get(position).getName().toString().replace(".mp3", ""));
    }
    public static Context getAppContext(){
        return  Player.context;
    }
    public  void  AnimationGetir(){
        ValueAnimator animator = ValueAnimator.ofFloat(0, 360);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                // 2
                imgResim.setRotation(value);
            }
        });

        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1999);
        animator.start();
    }
}

