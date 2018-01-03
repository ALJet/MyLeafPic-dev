package indi.aljet.myleafpic_dev.delete;

import indi.aljet.myleafpic_dev.data.Album;
import indi.aljet.myleafpic_dev.util.StringUtils;

/**
 * Created by PC-LJL on 2017/11/21.
 */

public class Folder {

    int count = -1;
    int progress = 0;
    String path;


    public Folder(String path, int count) {
        this.path = path;
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getName() {
        return StringUtils.getName(path);
    }

    public String getPath() {
        return path;
    }

    public int getCount() {
        return count;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Album)
            return path.equals(((Album) obj).getPath());
        if (obj instanceof String)
            return path.equals(obj);
        return super.equals(obj);
    }

}
