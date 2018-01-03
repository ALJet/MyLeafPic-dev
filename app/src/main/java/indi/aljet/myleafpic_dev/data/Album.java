package indi.aljet.myleafpic_dev.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

import indi.aljet.myleafpic_dev.data.filter.FilterMode;
import indi.aljet.myleafpic_dev.data.sort.SortingMode;
import indi.aljet.myleafpic_dev.data.sort.SortingOrder;
import indi.aljet.myleafpic_dev.util.StringUtils;

/**
 * Created by PC-LJL on 2017/11/14.
 */

public class Album implements CursorHandler,
        Parcelable {


    public static final long ALL_MEDIA_ALBUM_ID = 8000;
    private String name,path;
    private long id = -1,dateModified;
    private int count = -1;

    private boolean selected = false;
    public AlbumSettings settings = null;
    private Media lastMedia = null;

    public Album(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public Album(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public Album(String path, String name, long id, int count, long dateModified) {
        this.path = path;
        this.name = name;
        this.id = id;
        this.count = count;
        this.dateModified = dateModified;
    }


    public Album(String path,String name,
                 int count,long dateModified){
        this(path,name,-1,count,dateModified);
    }

    public Album(Cursor cur){
        this(StringUtils.getBucketPathByImagePath(cur.getString(3)),
                cur.getString(1),
                cur.getLong(0),
                cur.getInt(2),
                cur.getLong(4));
        setLastMedia(new Media(cur.getString(3)));
    }

    public static String[] getProjection(){
        return new String[]{
                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Images.Media
                .BUCKET_DISPLAY_NAME,
                "count(*)",
                MediaStore.Images.Media.DATA,
                "max (" + MediaStore.Images.Media.DATE_MODIFIED
                + ")"
        };
    }

    @Override
    public Album handle(Cursor cu) throws SQLException {
        return new Album(cu);
    }


    @Deprecated
    public Album(Context context,String path,
                 long id,String name,int count){
        this(path,name,id,count,0);
        settings = AlbumSettings.getDefaults();
    }

    public static Album getEmptyAlbum(){
        Album album = new Album(null,null);
        album.settings = AlbumSettings.getDefaults();
        return album;
    }

    public static Album getAllMediaAlbum(){
        Album album = new Album("All Media",
                ALL_MEDIA_ALBUM_ID);
        album.settings = AlbumSettings.getDefaults();
        return album;
    }


    static Album withPath(String path){
        Album emptyAlbum = getEmptyAlbum();
        emptyAlbum.path = path;
        return emptyAlbum;
    }


    public Album withSettings(AlbumSettings settings) {
        this.settings = settings;
        return this;
    }

    public String getName(){
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getCount() {
        return count;
    }

    public long getDateModified() {
        return dateModified;
    }

    public Media getCover(){
        if(hashCover()){
            return new Media(settings
            .coverPath);
        }
        if(lastMedia != null){
            return lastMedia;
        }
        return new Media();
    }

    public void setLastMedia(Media lastMedia) {
        this.lastMedia = lastMedia;
    }

    public void setCover(String path) {
        settings.coverPath = path;
    }

    public long getId() {
        return  this.id;
    }

    public boolean isHidden(){
        return new File(path,".nomedia")
                .exists();
    }

    public boolean isPinned(){
        return settings.pinned;
    }

    public boolean hashCover(){
        return settings.coverPath != null;
    }

    public FilterMode fileterMode(){
        return settings != null ? settings
                .filterMode : FilterMode.ALL;
    }

    public void setFilterMode(FilterMode newMode){
        settings.filterMode = newMode;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", id=" + id +
                ", count=" + count +
                '}';
    }

    public ArrayList<String> getParentsFolders(){
        ArrayList<String> result = new
                ArrayList<>();
        File f = new File(getPath());
        while(f != null && f.canRead()){
            result.add(f.getPath());
            f = f.getParentFile();
        }
        return result;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean setSelected(boolean selected) {
        if (this.selected == selected)
            return false;
        this.selected = selected;
        return true;
    }

    public boolean toggleSelected() {
        selected = !selected;
        return selected;
    }

    public void removeCoverAlbum() {
        settings.coverPath = null;
    }

    public void setDefaultSortingMode(Context context, SortingMode column) {
        settings.sortingMode = column.getValue();
    }

    public void setDefaultSortingAscending(Context context, SortingOrder sortingOrder) {
        settings.sortingOrder = sortingOrder.getValue();
    }

    public boolean togglePinAlbum() {
        settings.pinned = !settings.pinned;
        return settings.pinned;
    }


    @Deprecated
    public int moveSelectedMedia(Context context, String targetDir) {
		/*int n = 0;
		try
		{
			for (int i = 0; i < selectedMedia.size(); i++) {

				if (moveMedia(context, selectedMedia.get(i).getPath(), targetDir)) {
					String from = selectedMedia.get(i).getPath();
					scanFile(context, new String[]{ from, StringUtils.getPhotoPathMoved(selectedMedia.get(i).getPath(), targetDir) },
							new MediaScannerConnection.OnScanCompletedListener() {
								@Override
								public void onScanCompleted(String s, Uri uri) {
									Log.d("scanFile", "onScanCompleted: " + s);
								}
							});
					media.remove(selectedMedia.get(i));
					n++;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		setCount(media.size());
		return n;*/
        return -1;
    }

    public void sortPhotos() {
		/*Collections.sort(media, MediaComparators.getComparator(settings.getSortingMode(), settings.getSortingOrder()));*/
    }

    public boolean copySelectedPhotos(Context context, String folderPath) {
		/*boolean success = true;
		for (Media media : selectedMedia)
			if(!copyPhoto(context, media.getPath(), folderPath))
				success = false;
		return success;*/
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  Album){
            return path.equals(((Album) obj)
            .getPath());
        }
        return super.equals(obj);
    }

    public boolean deleteSelectedMedia(Context context) {
		/*boolean success = true;
		for (Media selectedMedia : this.selectedMedia) {
			if (deleteMedia(context, selectedMedia))
				media.remove(selectedMedia);
			else success = false;
		}
		if (success) {
			clearSelectedMedia();
			setCount(media.size());
		}
		return success;*/
        return false;
    }

    private boolean found_id_album = false;

    public boolean renameAlbum(final Context context, String newName) {
		/*found_id_album = false;
		boolean success;
		File dir = new File(StringUtils.getAlbumPathRenamed(getPath(), newName));
		if (success = StorageHelper.mkdir(context, dir)) {
			for (final Media m : media) {
				File from = new File(m.getPath());
				File to = new File(StringUtils.getPhotoPathRenamedAlbumChange(m.getPath(), newName));
				if (StorageHelper.moveFile(context, from, to)) {
					scanFile(context, new String[]{from.getAbsolutePath() });
					scanFile(context, new String[]{ to.getAbsolutePath() }, new MediaScannerConnection.OnScanCompletedListener() {
						@Override
						public void onScanCompleted(String s, Uri uri) {
							// TODO: 05/08/16 it sucks! look for a better solution!

							if (!found_id_album) {
								id = ContentProviderHelper.getAlbumId(context, s);
								found_id_album = true;
							}
							Log.d(s, "onScanCompleted: "+s);
							m.setPath(s); m.setUri(uri.toString());
						}
					});

				} else success = false;
			}
		}
		if(success) {
			path = dir.getAbsolutePath();
			name = newName;
			// NOTE: the following line doesn't work
			//id = ContentProviderHelper.getAlbumId(context, media.getValue(0).getPath());

		}
		return success;*/

        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.id);
        dest.writeLong(this.dateModified);
        dest.writeInt(this.count);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.settings);
        dest.writeParcelable(this.lastMedia, flags);
    }

    protected Album(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.id = in.readLong();
        this.dateModified = in.readLong();
        this.count = in.readInt();
        this.selected = in.readByte() != 0;
        this.settings = (AlbumSettings) in.readSerializable();
        this.lastMedia = in.readParcelable(Media.class.getClassLoader());
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };


}
