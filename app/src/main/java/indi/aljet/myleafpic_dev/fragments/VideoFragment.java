package indi.aljet.myleafpic_dev.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.drew.lang.annotations.Nullable;
import com.mikepenz.iconics.view.IconicsImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.activitys.SingleMediaActivity;
import indi.aljet.myleafpic_dev.data.Media;
import indi.aljet.myleafpic_dev.data.StorageHelper;

/**
 * Created by PC-LJL on 2017/11/20.
 */

public class VideoFragment extends Fragment {

    @BindView(R.id.media_view)
    ImageView picture;
    @BindView(R.id.icon)
    IconicsImageView videoInd;


    private Media video;
    public static VideoFragment newInstance(Media media){
        VideoFragment videoFragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putParcelable("video",media);
        videoFragment.setArguments(args);
        return videoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        video = getArguments().getParcelable("video");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout
        .fragment_video,container,false);
        ButterKnife.bind(this,view);
        videoInd.setOnClickListener(v ->{
            startActivity(new Intent(Intent
            .ACTION_VIEW).setDataAndType(StorageHelper
            .getUriForFile(getContext(),video
            .getFile()),video.getMimeType()));
    });
        RequestOptions options = new RequestOptions()
                .signature(video.getSignature())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getContext())
                .load(video.getUri())
                .apply(options)
                .into(picture);
        picture.setOnClickListener(
                v -> ((SingleMediaActivity) getActivity())
                        .toggleSystemUI());

        return view;
    }
}
