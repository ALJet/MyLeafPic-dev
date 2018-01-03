package indi.aljet.myleafpic_dev.data.filter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Created by PC-LJL on 2017/11/8.
 */

public class ImageFileFilter implements FilenameFilter {
    private Pattern pattern;

    public ImageFileFilter() {
    }

    public ImageFileFilter(boolean includeVideo){
        pattern = includeVideo
                ? Pattern.compile(".(jpg|png|gif|jpe|jpeg|bmp|webp|mp4|mkv|webm|avi)$"
        ,Pattern.CASE_INSENSITIVE) : Pattern.compile(".(jpg|png|gif|jpe|jpeg|bmp|webp)$"
        ,Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean accept(File file, String s) {
        return new File(file,s)
                .isFile() && pattern
                .matcher(s).find();
    }
}
