package indi.aljet.myleafpic_dev.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 试一下这个bitmap工具类
 *
 * Created by PC-LJL on 2017/11/14.
 */

public class BitmapUtils {

    public static Bitmap addWhiteBorder(Bitmap bmp,
                                        int borderSize){
        Bitmap bmpWithBorder = Bitmap.createBitmap(
                bmp.getWidth() + borderSize * 2,
                bmp.getHeight() + borderSize * 2,
                bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp,borderSize,
                borderSize,null);
        return bmpWithBorder;
    }


    public static Bitmap getCroppedBitmap(Bitmap srcBmp){
        Bitmap dstBmp;
        if(srcBmp.getWidth() >= srcBmp.getHeight()){
            dstBmp = Bitmap.createBitmap(srcBmp,srcBmp
            .getWidth() / 2 - srcBmp.getHeight() / 2,0,
                    srcBmp.getHeight(),srcBmp.getHeight());
        }else{
            dstBmp = Bitmap.createBitmap(srcBmp,0,
                    srcBmp.getHeight() /2 - srcBmp
            .getWidth() /2,
                    srcBmp.getWidth(),srcBmp.getWidth());
        }
        return dstBmp;
    }




}
