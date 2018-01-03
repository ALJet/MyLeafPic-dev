package indi.aljet.myleafpic_dev.activitys.rx;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

import indi.aljet.myleafpic_dev.App;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.util.Affix;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by LJL-lenovo on 2017/11/28.
 */

public class GetRx extends BaseRxTask<Affix.Options,
        String> {

    public GetRx(Context mContext, boolean needDialog) {
        super(mContext, needDialog);
    }

    public GetRx(Context mContext, boolean needDialog, AlertDialog dialog) {
        super(mContext, needDialog, dialog);
    }

    public GetRx(Context mContext, boolean needDialog, String... p) {
        super(mContext, needDialog, p);
    }

    @Override
    protected Observable<Affix.Options> doInBackground() {
        return  Observable.create(
                new ObservableOnSubscribe<Affix.Options>() {
                    @Override
                    public void subscribe(ObservableEmitter<Affix.Options> e) throws Exception {
                        ArrayList<Bitmap> bitmapArray = new ArrayList<>();
                        if(bitmapArray.size() > 1){
                            Affix.AffixBitmapList(mContext,
                                    bitmapArray, (Affix.Options) e);
                        }else{
                            Toast.makeText(mContext,
                                    mContext.getString(R.string
                                    .affix_error),Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    @Override
    protected String getParam(String... p) {
        return null;
    }
}
