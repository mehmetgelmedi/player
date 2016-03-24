package com.pwsturk.meg.megplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lv = (ListView) findViewById(R.id.lvPlayList);
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) { //test icinokunan .mp3 yazdir
            //toast(mySongs.get(i).getName().toString());
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", ""); //isimleri oku sondaki .mp3 sil
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        lv.setAdapter(adp);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", position).putExtra("songlist", mySongs));
            }
        });
    }



    public ArrayList<File> findSongs(File root){
        ArrayList<File> al=new ArrayList<File>(); //muzik dosyalari tutmak icin
        File[] files =root.listFiles(); //dizinlere eris files dizisine aktar
        for(File singleFile : files){ // her dizindeki klasor
            if(singleFile.isDirectory() && !singleFile.isHidden()){ //dizin icersindeki dizine girme recursive fonk.
                al.addAll(findSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".MP3")) //||singleFile.getName().endsWith("wav") // dizinde .mp3 varsa al e ekle
                    al.add(singleFile);
            }
        }
        return  al;
    }

    public void toast(String text){ //gereksiz bir fonk.
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
