package indi.aljet.myleafpic_dev;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePalApplication;

import indi.aljet.myleafpic_dev.data.Album;
import indi.aljet.myleafpic_dev.data.HandlingAlbums;

/**
 * Created by PC-LJL on 2017/11/6.
 */

public class App extends LitePalApplication {

    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);
        Hawk.init(this).build();
        mInstance = this;

    }

    public static App getmInstance() {
        return mInstance;
    }

    @Deprecated
    public Album getAlbum(){
        return Album.getEmptyAlbum();
    }

    @Deprecated
    public HandlingAlbums getAlbums() {
        return HandlingAlbums.getInstance(getApplicationContext());
    }


}
