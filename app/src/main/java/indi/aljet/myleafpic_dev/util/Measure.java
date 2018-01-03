package indi.aljet.myleafpic_dev.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by PC-LJL on 2017/11/13.
 */

public class Measure {

    public static final String TAG = "Measure";
    public  static int pxToDp(int px, Context c){
        DisplayMetrics displayManager = c.getResources()
                .getDisplayMetrics();
        return Math.round(px * (displayManager.ydpi /
        DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float dpToPx(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public static int getStatusBarHeight(Resources r){
        int resourceId = r.getIdentifier(
                "status_bar_height", "dimen", "android");
        if(resourceId > 0){
            return r.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getNavBarHeight(Context ct){
        return getNavigationBarSize(ct).y;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Point getNavigationBarSize(Context context){
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        if(appUsableSize.x < realScreenSize.x){
            return new Point(realScreenSize.x
             - appUsableSize.x,appUsableSize.y);
        }
        if(appUsableSize.y < realScreenSize.y){
            return new Point(appUsableSize.x,
                    realScreenSize.y - appUsableSize.y);
        }
        return new Point();
    }

    private static Point getAppUsableScreenSize(Context context){
        WindowManager windowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Point getRealScreenSize(Context context){
        WindowManager windowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager
                .getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }

    public static int rotateBy(int current,int degrees){
        return (current + degrees) % 360;
    }





}
