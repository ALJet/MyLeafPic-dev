package indi.aljet.myleafpic_dev.data.filter;

import indi.aljet.myleafpic_dev.data.Media;

/**
 * Created by PC-LJL on 2017/11/8.
 */

public class MediaFilter {

    /**
     * 这个方法怎么解释
     * @param mode
     * @return
     */
    public static IMediaFilter getFilter(FilterMode mode){
        switch (mode){
            case ALL:default:
                return Media->true;
            case GIF:
                return Media::isGif;
            case VIDEO:
                return Media::isVideo;
            case IMAGES:
                return Media::isImage;
        }
    }
}
