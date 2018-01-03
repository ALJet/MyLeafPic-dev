package indi.aljet.myleafpic_dev.sql;

import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.exif.ExifDirectoryBase;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import indi.aljet.myleafpic_dev.data.Album;
import indi.aljet.myleafpic_dev.data.AlbumSettings;
import indi.aljet.myleafpic_dev.data.StorageHelper;
import indi.aljet.myleafpic_dev.data.sort.SortingMode;
import indi.aljet.myleafpic_dev.data.sort.SortingOrder;
import indi.aljet.myleafpic_dev.sql.model.Folders;

/**
 * Created by PC-LJL on 2017/11/15.
 */

public class AlbumsSqlData {

    private static final int EXCLUDED = 1;
    private static AlbumsSqlData mInstance = null;


    public AlbumsSqlData(Context context) {
        super();
    }

    public static AlbumsSqlData getInstance(Context context){
        if(mInstance == null){
            mInstance = new AlbumsSqlData(context);
        }
        return mInstance;
    }

    public void onUpgrade(){

    }

    public void excludeAlbum(Album album){
        changeStatusAlbum(album,EXCLUDED);
    }


    public void changeStatusAlbum(Album album, int status){
        ContentValues values = new ContentValues();
        if(exist(album.getPath())){
            values.put("status",status);
            DataSupport.update(Folders.class,values,1);
        }else{
            Folders folders = new Folders();
            folders.setPath(album.getPath());
            folders.setPinned(0);
            folders.setSorting_mode(SortingMode
            .DATE.getValue());
            folders.setSorting_order(SortingOrder
            .DESCENDING.getValue());
            folders.setId((int)album.getId());
            folders.setStatus(status);
            folders.save();
        }
    }


    public  void insertDefault(String path){
        Folders folders = new Folders();
        folders.setPath(path);
        folders.setPinned(0);
        folders.setSorting_mode(SortingMode
        .DATE.getValue());
        folders.setSorting_order(SortingOrder
        .DESCENDING.getValue());
        folders.setId(-1);
        folders.save();
    }


    public void clearStatusFolder(String path){
        ContentValues values = new ContentValues();
        values.put("status","");
        if(exist(path)){
            DataSupport.updateAll(Folders
            .class,values,"path = ?",path);
        }
    }


    public ArrayList<String> getFolders(int status){
        List<Folders> list = DataSupport
                .select("path")
                .where("status = ?",String.valueOf(status))
                .find(Folders.class);
        ArrayList<String> liststring = new ArrayList<>();
        for(int i = 0; i < list.size();i++){
            liststring.add(i,list.get(i).getPath());
        }
        return liststring;
    }


    public int getFoldersCount(int status){
        int count = DataSupport.where("status = ? ",
                String.valueOf(status)).
                count(Folders.class);
        return count;
    }


    public  ArrayList<String> getExcludedFolders(Context context){
        ArrayList<String>  list = new ArrayList<>();
        HashSet<File> storageRoots = StorageHelper.getStorageRoots(context);
        for(File file : storageRoots)
            // it has a lot of garbage
            list.add(new File(file.getPath(), "Android").getPath());
        list.addAll(getFolders(EXCLUDED));
        return list;

    }



    private  boolean exist(String path){
//        Folders folders = DataSupport(Folders.class);
//        List<Folders> lists = DataSupport.where("path = ? ",path)
//                .find(Folders.class);
        int count = DataSupport.where("path = ?",
                path).count(Folders.class);
        if(count >= 1)
            return true;
        return false;
    }


    public void setPined(String path,
                              boolean pinned){
        Folders f = new Folders();
        f.setPinned(pinned ? 1 : 0);
        f.updateAll("path = ?",path);
    }

    public void setCover(String path,
                         String mediaPath){
        Folders f = new Folders();
        f.setCover_path(mediaPath);
        f.updateAll("path = ?",path);
    }

    public void setSortingMode(String path,
                         int column){
        Folders f = new Folders();
        f.setSorting_mode(column);
        f.updateAll("path = ?",path);
    }

    public void setSoortingOrder(String path,
                         int sortingOrder){
        Folders f = new Folders();
        f.setPinned(sortingOrder);
        f.updateAll("path = ?",path);
    }


    @NotNull
    public AlbumSettings getSettings(String path){
        int count = DataSupport.where("path = ?",
                path).count(Folders.class);
        if(count == 0){
            insertDefault(path);
            Log.d("getSetting" , "count = 0  "+ count);
            return AlbumSettings.getDefaults();
        }else{
            Log.d("getSetting" , "count =   "+ count);
            Folders folders = DataSupport.where
                    ("path = ?" ,path).
                    findFirst(Folders.class);
            return new AlbumSettings(folders
            .getCover_path(),
                    Integer.valueOf(folders.getSorting_mode()),
                    Integer.valueOf(folders.getSorting_order()),
                    folders.getPinned());
        }

    }

    public  List<Folders> getFolders(String path){
        List<Folders> list = DataSupport
                .where("path = ? ",path)
                .find(Folders.class);
        return list;
    }
}
