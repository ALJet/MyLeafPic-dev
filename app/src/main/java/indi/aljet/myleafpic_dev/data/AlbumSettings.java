package indi.aljet.myleafpic_dev.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import indi.aljet.myleafpic_dev.data.filter.FilterMode;
import indi.aljet.myleafpic_dev.data.sort.SortingMode;
import indi.aljet.myleafpic_dev.data.sort.SortingOrder;

/**
 * Created by PC-LJL on 2017/11/14.
 */

public class AlbumSettings implements Serializable ,
        Parcelable{

    String coverPath;
    int sortingMode,sortingOrder;
    boolean pinned;
    FilterMode filterMode = FilterMode.ALL;

    public static AlbumSettings getDefaults(){
        return new AlbumSettings
                (null, SortingMode.DATE.getValue(), 1, 0);
    }

    public AlbumSettings(String cover, int sortingMode,
                         int sortingOrder, int pinned){
        this.coverPath = cover;
        this.sortingMode = sortingMode;
        this.sortingOrder = sortingOrder;
        this.pinned = pinned == 1;
    }


    public SortingMode getSortingMode(){
        return SortingMode.fromValue(sortingOrder);
    }

    public SortingOrder getSortingOrder() {
        return SortingOrder.fromValue(sortingOrder);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.coverPath);
        dest.writeInt(this.sortingMode);
        dest.writeInt(this.sortingOrder);
        dest.writeByte(this.pinned ? (byte) 1 : (byte) 0);
        dest.writeInt(this.filterMode == null ? -1 : this.filterMode.ordinal());
    }

    public static final Creator<AlbumSettings> CREATOR = new Creator<AlbumSettings>() {
        @Override
        public AlbumSettings createFromParcel(Parcel source) {
            return new AlbumSettings(source);
        }

        @Override
        public AlbumSettings[] newArray(int size) {
            return new AlbumSettings[size];
        }
    };

    protected AlbumSettings(Parcel in) {
        this.coverPath = in.readString();
        this.sortingMode = in.readInt();
        this.sortingOrder = in.readInt();
        this.pinned = in.readByte() != 0;
        int tmpFilterMode = in.readInt();
        this.filterMode = tmpFilterMode == -1 ? null : FilterMode.values()[tmpFilterMode];
    }
}
