package indi.aljet.myleafpic_dev.data;

import android.database.Cursor;
import android.database.SQLException;

/**
 * Created by PC-LJL on 2017/11/8.
 */

/**
 * jdk 1.8
 *
 * @param <T>
 */
public interface CursorHandler<T> {

//    T handle(Cursor cu) throws SQLException;
//    static String [] getProjection() {
//        return new String[0];
//    }


    T handle(Cursor cu)throws SQLException;

//    static String [] getProjection() {
//        return new String[0];
//    }

}
