package com.pwsturk.meg.megplayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;


public class SongLoader extends AsyncTask<Void, Void,ArrayList<File>> {
    private Context context;
    private ProgressDialog progressDialog;

    public SongLoader(Context context){
        this.context=context;
    }

    private ArrayList<File> findSongs(File root){
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

    @Override
    protected ArrayList<File> doInBackground(Void... params) {
        return findSongs(Environment.getExternalStorageDirectory());
    }

    @Override
    protected void onPreExecute() {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<File> files) {
        progressDialog.dismiss();
    }
}
