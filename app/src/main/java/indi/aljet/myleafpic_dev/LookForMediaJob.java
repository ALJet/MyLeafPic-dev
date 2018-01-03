package indi.aljet.myleafpic_dev;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import indi.aljet.myleafpic_dev.data.HandlingAlbums;
import indi.aljet.myleafpic_dev.data.Media;
import indi.aljet.myleafpic_dev.data.filter.ImageFileFilter;

/**
 * Created by PC-LJL on 2017/11/16.
 */

public class LookForMediaJob extends JobService {
    private final String TAG = "LookForMediaJob";
    private boolean DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.wtf(TAG, "JOB created");
    }


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.wtf(TAG, "JOB started");
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                try{
                    ArrayList<String> whiteList = HandlingAlbums
                            .getInstance(getApplicationContext())
                            .getFolders(HandlingAlbums.INCLUDED);
                    for(String s: whiteList){
                        scanFolder(s);
                        Log.wtf(TAG, "Scanned: " + s);
                    }
                    if(DEBUG)
                        notification(whiteList);
                }finally{
                    jobFinished(jobParameters,false);
                }
            }
        }).start();
        return true;
    }


    private void notification(ArrayList<String> list){
        StringBuilder builder = new StringBuilder();
        for(String s : list){
            builder.append(s).append("\n");
        }
        NotificationCompat.Builder builder1 =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap
                .ic_launcher)
                .setContentTitle("Looked for media")
                .setAutoCancel(true)
                .setContentText(builder
                .toString());
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(
                        Service.NOTIFICATION_SERVICE);
        Notification notification = builder1.build();
        notificationManager.notify(0,notification);
    }


    private void scanFolder(String path){
        String[] list = new File(path).list(new
                ImageFileFilter(true));
        if(list != null){
            MediaScannerConnection.scanFile(getApplicationContext(),
                    list,null,null);
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.wtf(TAG,"JOB stop");
        return false;
    }
}
