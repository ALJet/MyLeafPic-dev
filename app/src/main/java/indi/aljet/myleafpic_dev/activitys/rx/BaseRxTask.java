package indi.aljet.myleafpic_dev.activitys.rx;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJL-lenovo on 2017/11/28.
 */

public abstract class BaseRxTask<D,P> {

    protected  P[] p;
    protected Context mContext;
    private boolean needDialog = false;
    private AlertDialog dialog;


    public BaseRxTask(Context mContext, boolean needDialog, AlertDialog dialog) {
        this.mContext = mContext;
        this.needDialog = needDialog;
        this.dialog = dialog;
    }

    public BaseRxTask(Context mContext, boolean needDialog) {
        this.mContext = mContext;
        this.needDialog = needDialog;
    }

    public BaseRxTask(Context mContext, boolean needDialog, P... p) {
        this.mContext = mContext;
        this.needDialog = needDialog;
        this.p = p;
    }

    protected Observable<D> doInBackgroundObserVable(){
        return doInBackground();
    }

    public Observable<D> execute(){
        return getBaseDialogView();
    }


    private Observable<D> getBaseDialogView(){
        Observable<D> flowable = doInBackgroundObserVable();
        return flowable.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dialog.show();
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }



    protected abstract  Observable<D> doInBackground();



    protected abstract String getParam(P... p);


}
