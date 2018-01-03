package indi.aljet.myleafpic_dev.data.provider;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;

import indi.aljet.myleafpic_dev.data.Album;

/**
 * Created by PC-LJL on 2017/11/14.
 */

@Deprecated
public class ContentProviderHelper {

    @Deprecated
    public static long getAlbumId(Context context,
                                  String mediaPath){
        long id = -1;
        Cursor cur = context.getContentResolver()
                .query(MediaStore.Files.getContentUri("external"),
                        new String[]{MediaStore.Files
                        .FileColumns.PARENT},
                        MediaStore.Files.FileColumns.DATA +
                "= ?",new String[]{mediaPath },null);
        if(cur != null && cur.moveToFirst()){
            id = cur.getLong(0);
            cur.close();
        }
        return id;
    }


    @Deprecated
    public static Album getAlbumFromMedia(Context context,
                                          String mediaPath){
        File parentFolder = new File(mediaPath)
                .getParentFile();
        if(parentFolder == null || !parentFolder.isDirectory()){
            return null;
        }
        return new Album(context, parentFolder.getPath(),
                getAlbumId(context, mediaPath),
                parentFolder.getName(), 0);
    }
}
