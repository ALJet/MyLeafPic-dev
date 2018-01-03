package indi.aljet.myleafpic_dev.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by PC-LJL on 2017/11/14.
 */

public class PermissionUtils {

    public static boolean checkPermissions(Context context,
                                           String... permissions){
        for (String permission : permissions){
            if(!checkPermission(context,permission)){
                return false;
            }
        }
        return true;
    }


    public static boolean checkPermission(Context context,
                                          String permission){
        return ContextCompat.checkSelfPermission(context,
                permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isDeviceInfoGranted(Context context){
        return checkPermissions(context, Manifest
        .permission.READ_EXTERNAL_STORAGE);
    }

    public static void requestPermissions(Object o,
                                          int permissionId,
                                          String... permissions){
        if(o instanceof Activity){
            ActivityCompat.requestPermissions
                    ((AppCompatActivity) o, permissions, permissionId);
        }
    }
}