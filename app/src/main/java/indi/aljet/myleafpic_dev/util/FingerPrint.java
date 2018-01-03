package indi.aljet.myleafpic_dev.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.drew.lang.StringUtil;

import indi.aljet.myleafpic_dev.R;

/**
 * Created by PC-LJL on 2017/11/14.
 */

public class FingerPrint {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkFinger(Context ctx){
        KeyguardManager keyguardManager = (KeyguardManager)
                ctx.getSystemService(Context.KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager)
                ctx.getSystemService(Context.FINGERPRINT_SERVICE);
        try{

            if(!fingerprintManager.isHardwareDetected()){
                StringUtils.showToast(ctx,
                        ctx.getString(R.string.fp_not_supported));
                return false;
            }
            if(!fingerprintManager.hasEnrolledFingerprints()){
                StringUtils.showToast(ctx,ctx
                .getString(R.string.fp_not_configured));
                return false;
            }
            if(!keyguardManager.isKeyguardSecure()){
                StringUtils.showToast(ctx,ctx
                .getString(R.string.fp_not_enabled_sls));
                return false;
            }
        }catch (SecurityException se){
            se.printStackTrace();
        }
        return true;
    }
}
