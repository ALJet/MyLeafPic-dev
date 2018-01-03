package indi.aljet.myleafpic_dev.data.filter;

import indi.aljet.myleafpic_dev.data.Media;

/**
 * Created by PC-LJL on 2017/11/8.
 */

public interface IMediaFilter {
    boolean accept(Media media);
}
