package indi.aljet.myleafpic_dev.data.provider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;


import org.reactivestreams.Subscriber;

import indi.aljet.myleafpic_dev.data.CursorHandler;
import io.reactivex.Observable;

/**
 * Created by PC-LJL on 2017/11/15.
 */

public class QueryUtils {


    public static <T> Observable<T> query(Query q,
                                        ContentResolver cr,
                                        CursorHandler<T> ch){
        return Observable.create(subscriber -> {
            Cursor cursor = null;
            try{
                cursor = q.getCursor(cr);
                if(cursor != null && cursor
                        .getCount() > 0){
                    while(cursor.moveToNext()){
                        subscriber.onNext(ch.handle(cursor));
                    }
                    subscriber.onComplete();
                }
            }catch (Exception err){
                subscriber.onError(err);
            }finally{
                if(cursor != null)
                    cursor.close();
            }
        });
    }

    public static <T> Observable<T> querySingle(Query q,ContentResolver cr,
                                                CursorHandler<T> ch){
        return Observable.create(subscriber -> {
           Cursor cursor = null;
            try{
                cursor = q.getCursor(cr);
                if(cursor != null && cursor.moveToFirst()){
                    subscriber.onNext(ch.handle(cursor));
                    subscriber.onComplete();
                }
            }catch (Exception err){
                subscriber.onError(err);
            }finally{
                if(cursor != null){
                    cursor.close();
                }
            }
        });
    }

}
