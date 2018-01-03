package indi.aljet.myleafpic_dev.data.provider;

import android.content.Context;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.util.ArrayList;

import indi.aljet.myleafpic_dev.data.Album;
import indi.aljet.myleafpic_dev.data.Media;
import indi.aljet.myleafpic_dev.data.StorageHelper;
import indi.aljet.myleafpic_dev.data.filter.FoldersFileFilter;
import indi.aljet.myleafpic_dev.data.filter.ImageFileFilter;
import indi.aljet.myleafpic_dev.data.sort.SortingMode;
import indi.aljet.myleafpic_dev.data.sort.SortingOrder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * Created by PC-LJL on 2017/11/15.
 */

public class CPHelper {



    public static Observable<Album> getAlbums(Context context
    , boolean hidden, ArrayList<String> excluded, SortingMode sortingMode
    , SortingOrder sortingOrder){
        return hidden ? getHiddenAlbums(context,
                excluded) : getAlbums(context,excluded,
                sortingMode,sortingOrder);
    }

    private static String getHavingCluause(int excludedCount){
        if(excludedCount == 0){
            return "(";
        }
        StringBuilder res = new StringBuilder();
        res.append("HAVING (");

        res.append(MediaStore.Images.Media.DATA).append(" NOT LIKE ?");

        for(int i = 1;i < excludedCount;i++){
            res.append(" AND ")
                    .append(MediaStore.Images.Media
                    .DATA)
                    .append(" NOT LIKE ?");
        }
        return res.toString();

    }


    private static Observable<Album> getAlbums(Context context,
                                               ArrayList<String>
                                               excludedAlbums,
                                               SortingMode
                                               sortingMode,
                                               SortingOrder
                                               sortingOrder){
        Query.Builder query = new Query.Builder()
                .uri(MediaStore.Files.getContentUri("external"))
                .projection(Album.getProjection())
                .sort(sortingMode.getAlbumsColumn())
                .ascending(sortingOrder.isAscending());

        ArrayList<Object> args = new ArrayList<>();
        //show_videos   set_include_video
        if(Hawk.get("set_include_video",true)){
            query.selection(String.format("%s=? or %s=?) group by (%s) %s ",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.PARENT,
                    getHavingCluause(excludedAlbums.size())));
            args.add(MediaStore.Files.FileColumns
            .MEDIA_TYPE_IMAGE);
            args.add(MediaStore.Files.FileColumns
            .MEDIA_TYPE_VIDEO);
        }else{
            query.selection(String.format("%s=?) group by (%s) %s ",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.PARENT,
                    getHavingCluause(excludedAlbums.size())));
            args.add(MediaStore.Files.FileColumns
            .MEDIA_TYPE_IMAGE);
        }
        for(String s : excludedAlbums){
            args.add(s + "%");
        }
        query.args(args.toArray());

        return QueryUtils.query(query.build(),
                context.getContentResolver(),
                Album::new);
    }



    private static Observable<Album> getHiddenAlbums(Context context,
                                                     ArrayList<String>
                                                     excludedAlbums){
        return Observable.create(subscriber -> {
            try{
                for(File storage : StorageHelper
                        .getStorageRoots(context)){
                    fetchRecursivelyHiddenFolder(storage,
                            subscriber,excludedAlbums,Hawk
                    .get("set_include_video",true));
                }
            }catch (Exception err){
                subscriber.onError(err);
            }
        });
    }


    private static void fetchRecursivelyHiddenFolder
            (File dir, ObservableEmitter<Album> emitter,
             ArrayList<String> excludedAlbums,boolean includeVideo){
        if(!isExcluded(dir.getPath(),excludedAlbums)){
            File[] folders = dir.listFiles(new
                    FoldersFileFilter());
            if(folders != null){
                for(File temp : folders){
                    File nomedia = new File(temp,
                            ".nomedia");
                    if(!isExcluded(temp.getPath(),
                            excludedAlbums) && (nomedia
                    .exists() || temp.isHidden())){
                        checkAndAddFolder(temp,emitter,
                                includeVideo);
                    }
                    fetchRecursivelyHiddenFolder
                            ( temp, emitter, excludedAlbums, includeVideo);

                }
            }
        }
    }


    private static void checkAndAddFolder(File dir
    ,ObservableEmitter<Album> emitter,boolean includeVideo){
        File[] files = dir.listFiles(new
                ImageFileFilter(includeVideo));
        if(files != null && files.length > 0){
            long lastMod = Long.MIN_VALUE;
            File choice = null;
            for(File file : files){
                if(file.lastModified() > lastMod){
                    choice = file;
                    lastMod = file.lastModified();
                }
            }
            if(choice != null){
                Album asd = new Album(dir
                .getAbsolutePath(),dir.getName(),
                        files.length,lastMod);
                asd.setLastMedia(new Media(choice
                .getAbsoluteFile()));
                emitter.onNext(asd);
            }
        }
    }

    private static boolean isExcluded(String path,
                                      ArrayList<String> excludedAlbums){
        for(String s : excludedAlbums){
            if(path.startsWith(s)){
                return true;
            }
        }
        return false;
    }

    public static Observable<Media> getMedia(Context context
    ,Album album,SortingMode sortingMode,SortingOrder sortingOrder){
        if(album.getId() == -1 ) {
            return getMediaFromStorage(context,album);
        }else if(album.getId() == Album.ALL_MEDIA_ALBUM_ID){
            return getAllMediaFromMediaStore(context,
                    sortingMode,sortingOrder);
        }else{
            return getMediaFromMediaStore(context,
                    album,sortingMode,sortingOrder);
        }
    }


    private static Observable<Media> getAllMediaFromMediaStore(
            Context context,SortingMode sortingMode,SortingOrder
             sortingOrder){
        Query.Builder query = new Query.Builder()
                .uri(MediaStore.Files.getContentUri("external"))
                .projection(Media.getProjection())
                .sort(sortingMode.getMediaColumn())
                .ascending(sortingOrder.isAscending());

        if (Hawk.get("set_include_video", true)) {
            query.selection(String.format("(%s=? or %s=?)",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE));
            query.args(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            query.selection(String.format("%s=?",
                    MediaStore.Files.FileColumns.MEDIA_TYPE));
            query.args(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        }

        return QueryUtils.query(query.build(), context.getContentResolver()
                , new Media());
    }

    private static Observable<Media> getMediaFromStorage(
            Context context,Album album){
        return Observable.create(subscriber -> {
           File dir = new File(album.getPath());
            File[] files = dir.listFiles(new
                    ImageFileFilter(Hawk
            .get("set_include_video",true)));
            try{
                if(files != null && files.length > 0){
                    for (File file : files){
                        subscriber.onNext(new
                                Media(file));
                    }
                    subscriber.onComplete();
                }
            }catch (Exception err){
                subscriber.onError(err);
            }
        });
    }

    private static Observable<Media> getMediaFromMediaStore(
            Context context,Album album,SortingMode sortingMode
            ,SortingOrder sortingOrder){
        Query.Builder query = new Query.Builder()
                .uri(MediaStore.Files.getContentUri("external"))
                .projection(Media.getProjection())
                .sort(sortingMode.getMediaColumn())
                .ascending(sortingOrder.isAscending());

        if (Hawk.get("set_include_video", true)) {
            query.selection(String.format("(%s=? or %s=?) and %s=?",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.PARENT));
            query.args(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                    album.getId());
        } else {
            query.selection(String.format("%s=? and %s=?",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.PARENT));
            query.args(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, album.getId());
        }

        return QueryUtils.query(query.build(), context.getContentResolver(), Media::new);
    }




}
