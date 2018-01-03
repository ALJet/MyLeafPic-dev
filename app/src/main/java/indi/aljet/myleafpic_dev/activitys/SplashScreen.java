package indi.aljet.myleafpic_dev.activitys;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import horaapps.org.liz.ColorPalette;
import indi.aljet.myleafpic_dev.LookForMediaJob;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.activitys.base.SharedMediaActivity;
import indi.aljet.myleafpic_dev.data.Album;
import indi.aljet.myleafpic_dev.data.HandlingAlbums;
import indi.aljet.myleafpic_dev.util.PermissionUtils;
import indi.aljet.myleafpic_dev.util.StringUtils;

/**
 * Created by PC-LJL on 2017/11/22.
 */

public class SplashScreen extends SharedMediaActivity {


    private final String TAG = SplashScreen.class
            .getSimpleName();

    private final int READ_EXTERNAL_STORAGE_ID = 12;
    private static final int PICK_MEDIA_REQUEST = 44;

    final static String CONTENT = "content";
    final static String PICK_MODE = "pick_mode";

    final static int ALBUMS_PREFETCHED = 2376;
    final static int PHOTOS_PREFETCHED = 2567;
    final static int ALBUMS_BACKUP = 1312;
    private boolean PICK_INTENT = false;
    public final static String ACTION_OPEN_ALBUM =
            "indi.aljet.myleafpic_dev.OPEN_ALBUM";

    private Album tmpAlbum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView()
                .setSystemUiVisibility(View
                .SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setNavBarColor();
        setStatusBarColor();

        if(PermissionUtils.isDeviceInfoGranted(this)){
            if(getIntent().getAction().
                    equals(ACTION_OPEN_ALBUM)){
                Bundle data = getIntent()
                        .getExtras();
                if(data != null){
                    String ab = data.getString("albumPath");
                    if(ab != null){
                        File dir = new File(ab);
                        tmpAlbum = new Album(getApplicationContext(),
                                dir.getAbsolutePath(),data.getInt(
                                "albumId",-1),dir.getName(),-1);
                        start();
                    }
                }else{
                    StringUtils.showToast(getApplicationContext()
                    ,"Album not found");
                }
            }else{
                start();
            }
            PICK_INTENT = getIntent().getAction()
                    .equals(Intent.ACTION_GET_CONTENT) ||
                    getIntent().getAction().equals(
                            Intent.ACTION_PICK);
        }else{
            PermissionUtils.requestPermissions(this,
                    READ_EXTERNAL_STORAGE_ID,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void start(){
        startActivity(new Intent(SplashScreen.this,
                MainActivity.class));
        finish();
    }

    private void startLookingForMedia(){
        new Thread(() -> {
           if(Build.VERSION.SDK_INT >= Build
                   .VERSION_CODES.LOLLIPOP && getAlbums()
                   .getFoldersCount(HandlingAlbums.INCLUDED) > 0){
               JobInfo job = new JobInfo.Builder(0,new
                       ComponentName(getApplicationContext(),
                       LookForMediaJob.class))
                       .setPeriodic(1000)
                       .setRequiresDeviceIdle(true)
                       .build();

               JobScheduler scheduler = (JobScheduler)getSystemService(
                       Context.JOB_SCHEDULER_SERVICE);
               if(scheduler.getAllPendingJobs().size() == 0){
                   Log.wtf(TAG,scheduler.schedule(job)
                   == JobScheduler.RESULT_FAILURE ?
                           "LookForMediaJob scheduled successfully!" : "LookForMediaJob scheduled failed!");
               }
           }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PICK_MEDIA_REQUEST:
                if(resultCode == RESULT_OK){
                    setResult(RESULT_OK,data);
                    finish();
                }
                break;
            default:
                super.onActivityResult(requestCode,
                        resultCode,data);
        }
    }


    @Override
    public void setNavBarColor() {
        if(Build.VERSION.SDK_INT >= Build
                .VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(ColorPalette
            .getTransparentColor(ContextCompat.getColor(getApplicationContext()
            ,R.color.md_black_1000),70));
        }
    }

    @Override
    protected void setStatusBarColor() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES
                .LOLLIPOP){
            getWindow().setNavigationBarColor(
                    ColorPalette.getTransparentColor(ContextCompat
                    .getColor(getApplicationContext(),R
                    .color.md_black_1000),70));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case READ_EXTERNAL_STORAGE_ID:
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    start();
                }else{
                    Toast.makeText(SplashScreen
                    .this,getString(R.string.storage_permission_denied),
                            Toast.LENGTH_LONG).show();
                    break;
                }
                default:
                    super.onRequestPermissionsResult(requestCode,
                            permissions,grantResults);
        }
    }


    @Override
    public void updateUiElements() {
        super.updateUiElements();
        ((ProgressBar)findViewById(R.id.progress_splash))
                .getIndeterminateDrawable()
                .setColorFilter(getPrimaryColor(),
                        PorterDuff.Mode.SRC_ATOP);
        findViewById(R.id.Splah_Bg).setBackgroundColor(
                getBackgroundColor()
        );
    }
}
