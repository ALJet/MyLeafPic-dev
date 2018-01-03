package indi.aljet.myleafpic_dev.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import indi.aljet.myleafpic_dev.activitys.SingleMediaActivity;
import indi.aljet.myleafpic_dev.data.Media;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by PC-LJL on 2017/11/20.
 */

public class GifFragment extends Fragment {

    private Media gif;

    public static GifFragment newInstance(Media media){
        GifFragment gifFragment = new GifFragment();

        Bundle args = new Bundle();
        args.putParcelable("gif",media);
        gifFragment.setArguments(args);
        return gifFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gif = getArguments().getParcelable("gif");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        PhotoView photoView = new PhotoView(
                container.getContext());
        Ion.with(getContext()).load(gif.getPath())
                .intoImageView(photoView);

        photoView.setOnClickListener(view ->
                ((SingleMediaActivity) getActivity()).
                        toggleSystemUI());
        return photoView;
    }
}
