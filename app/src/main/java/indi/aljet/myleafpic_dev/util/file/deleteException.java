package indi.aljet.myleafpic_dev.util.file;

import indi.aljet.myleafpic_dev.data.Media;

/**
 * Created by LJL-lenovo on 2017/11/8.
 */

public class deleteException extends Exception {
    Media media;

    public deleteException() {
        this(null);
    }
    public deleteException(Media media) {
        super("Cannot delete Media");
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }
}
