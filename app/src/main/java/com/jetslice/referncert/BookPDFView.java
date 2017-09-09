package com.jetslice.referncert;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.barteksc.pdfviewer.PDFView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import is.arontibo.library.ElasticDownloadView;

public class BookPDFView extends AppCompatActivity {
    private StorageReference mStorageRef;
    File localFile;
    PDFView pdfView;
    String bookname;
    int clsno,chapterno;
    private ElasticDownloadView bnp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        pdfView= (PDFView) findViewById(R.id.pdfView);
        bnp= (ElasticDownloadView) findViewById(R.id.elastic_download_view);


        bookname=getIntent().getStringExtra("iBookname");
        clsno=getIntent().getIntExtra("iClassno",4);
        chapterno=getIntent().getIntExtra("iChapter",3);
        ArrayList<String> chapterset=getchapterset();

        Toast.makeText(getBaseContext(),"Class "+clsno+"/"+bookname.trim()+"/"+chapterset.get(chapterno)+".pdf",Toast.LENGTH_SHORT).show();
        String url="Class "+clsno+"/"+bookname+"/"+chapterset.get(chapterno)+".pdf";
        dotask(clsno,bookname.trim(),chapterset.get(chapterno));
    }

    private ArrayList<String> getchapterset() {
        ArrayList<String> chapters=new ArrayList<>();
        chapters.add("Chapter01");
        chapters.add("Chapter02");
        chapters.add("Chapter03");
        chapters.add("Chapter04");
        chapters.add("Chapter05");
        chapters.add("Chapter06");
        chapters.add("Chapter07");
        chapters.add("Chapter08");
        chapters.add("Chapter09");
        chapters.add("Chapter10");
        chapters.add("Chapter11");
        chapters.add("Chapter12");
        chapters.add("Chapter13");
        chapters.add("Chapter14");
        chapters.add("Chapter15");
        chapters.add("Chapter16");
        chapters.add("Chapter17");
        chapters.add("Chapter18");
        chapters.add("Chapter19");
        chapters.add("Chapter20");
        chapters.add("Chapter21");
        chapters.add("Chapter22");
        chapters.add("Chapter23");
        chapters.add("Chapter24");
        chapters.add("Chapter25");
        chapters.add("Chapter26");
        chapters.add("Chapter27");
        chapters.add("Chapter28");
        chapters.add("Chapter29");
        chapters.add("Chapter30");
        chapters.add("Chapter31");
        chapters.add("Chapter32");
        chapters.add("Chapter33");
        chapters.add("Chapter34");
        chapters.add("Chapter35");
        return chapters;

    }

    private void dotask(int clsno, final String booknamex, final String url) {
        StorageReference riversRef = mStorageRef.child("Class "+clsno+"/"+booknamex+"/"+url+".pdf");
        localFile = null;
        try {
            localFile = File.createTempFile("chap10", "pdf");
            Toast.makeText(getBaseContext(),"Downloading......."+booknamex,Toast.LENGTH_SHORT).show();
            bnp.startIntro();


        } catch (IOException e) {
            e.printStackTrace();
        }

        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        //TODO
                        //Add Progress Bar here

                        loadinpdf(localFile);
                        Toast.makeText(getBaseContext(),"Loaded",Toast.LENGTH_SHORT).show();
                        bnp.onEnterAnimationFinished();
                        bnp.success();






                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getBaseContext(),"Failed"+url+exception,Toast.LENGTH_SHORT).show();
                bnp.fail();
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            @SuppressWarnings("VisibleForTests")

            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                Double progress = (100.0 * taskSnapshot.getBytesTransferred())/ taskSnapshot.getTotalByteCount();

                Log.d("BookPDFView","onProgress: The value of the max is: "+taskSnapshot.getTotalByteCount());
                Log.d("BookPDFView","onProgress: The progress is: "+progress);

                if(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()<1) {

                    bnp.setProgress((float) Math.floor(progress));
                }
                if((taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()==0.99)){
                    bnp.success();

                }


                if(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()==1){
                    bnp.setVisibility(View.GONE);



                }


                if(!isOnline()){
                    bnp.fail();
                    Toast.makeText(BookPDFView.this, "Network not available", Toast.LENGTH_SHORT).show();
                }







            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    //TODO



    // Stop download on back pressed
    private void loadinpdf(File localFile) {
        pdfView.fromFile(localFile).load();
    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}