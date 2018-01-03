package indi.aljet.myleafpic_dev.data.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by PC-LJL on 2017/11/8.
 */

public class NotHiddenFoldersFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        return file.isDirectory() && !file.isHidden()
                && !new File(file,".nomedia").exists();
    }
}
