package indi.aljet.myleafpic_dev.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import indi.aljet.myleafpic_dev.data.Media;
import indi.aljet.myleafpic_dev.fragments.GifFragment;
import indi.aljet.myleafpic_dev.fragments.ImageFragment;
import indi.aljet.myleafpic_dev.fragments.VideoFragment;

/**
 * Created by PC-LJL on 2017/11/17.
 */

public class MediaPagerAdapter extends
        FragmentStatePagerAdapter {

    private final String TAG = "asd";
    private ArrayList<Media> media;
    private SparseArray<Fragment> registeredFragments =
            new SparseArray<>();

    public MediaPagerAdapter(FragmentManager fm,
                             ArrayList<Media> media) {
        super(fm);
        this.media = media;
    }

    @Override
    public Fragment getItem(int position) {
        Media media = this.media.get(position);
        if(media.isVideo()){
            return VideoFragment
                    .newInstance(media);
        }
        if(media.isGif()){
            return GifFragment
                    .newInstance(media);
        }else{
            return ImageFragment
                    .newInstance(media);
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super
                .instantiateItem(container,position);
        registeredFragments.put(position,
                fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container,position
        ,object);
    }

    public Fragment getRegisteredFragment(int position){
        return registeredFragments.get(position);
    }

    public void swapDataSet(ArrayList<Media> media){
        this.media = media;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return media.size();
    }
}
