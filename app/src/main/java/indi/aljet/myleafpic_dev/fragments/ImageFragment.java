package indi.aljet.myleafpic_dev.fragments;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.activitys.SingleMediaActivity;
import indi.aljet.myleafpic_dev.data.Media;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by PC-LJL on 2017/11/20.
 */

@SuppressWarnings("ResourceType")
public class ImageFragment extends Fragment {

    View view;
    private Media img;

    @BindView(R.id.subsampling_view)
    SubsamplingScaleImageView subsampling;

    @BindView(R.id.photo_view)
    PhotoView photoView;


    public static ImageFragment newInstance(Media media) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putParcelable("image", media);
        imageFragment.setArguments(args);
        return imageFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img = getArguments().getParcelable("image");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo,
                container, false);
        ButterKnife.bind(this, view);

        displayMedia();
        photoView.setOnClickListener(v -> {
            ((SingleMediaActivity) getActivity())
                    .toggleSystemUI();
        });

        photoView.setMaximumScale(8.0F);
        photoView.setMediumScale(3.0F);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!getActivity().isDestroyed()){
            Glide.with(getContext())
                    .clear(photoView);
        }
    }


    private void displayMedia(){
        RequestOptions options = new RequestOptions()
                .signature(img.getSignature())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(getContext())
                .load(img.getUri())
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(photoView);
    }


    private void addZoomableView(){
        subsampling.setMaxScale(10f);
        subsampling.setImage(ImageSource.uri(img
        .getUri()));
        subsampling.setOrientation(
                SubsamplingScaleImageView.
                        ORIENTATION_USE_EXIF);
        subsampling.setOnImageEventListener(
                new SubsamplingScaleImageView.OnImageEventListener() {
                    @Override
                    public void onReady() {

                    }

                    @Override
                    public void onImageLoaded() {
                        Log.wtf("Asd", "yeeeee");
                        subsampling.setVisibility(View.VISIBLE);
                        subsampling.setDoubleTapZoomScale(getDoubleTapZoomScale());
                    }

                    @Override
                    public void onPreviewLoadError(Exception e) {
                        subsampling.setVisibility(View.GONE);
                    }

                    @Override
                    public void onImageLoadError(Exception e) {
                        subsampling.setVisibility(View.GONE);
                    }

                    @Override
                    public void onTileLoadError(Exception e) {
                        subsampling.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPreviewReleased() {

                    }
                }
        );
    }

    private float getDoubleTapZoomScale(){
        BitmapFactory.Options options = new
                BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(img.getPath(),
                options);
        int width = options.outWidth;
        int height = options.outHeight;
        float bitmapAspectRation = (float) height /
                (float)(width);
        if(getContext() == null){
            return 2f;
        }
        return 2f;
    }


    public boolean rotatePicture(int rotation) {
        // TODO: 28/08/16 not working yet
        /*PhotoView photoView = (PhotoView) getView();

        int orientation = Measure.rotateBy(img.getOrientation(), rotation);
        Log.wtf("asd", img.getOrientation()+" + "+ rotation+" = " +orientation);

        if(photoView != null && img.setOrientation(orientation)) {
            Glide.clear(photoView);
            Glide.with(getContext())
                    .load(img.getUri())
                    .asBitmap()
                    .signature(img.getSignature())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    //.thumbnail(0.5f)
                    .transform(new RotateTransformation(getContext(), rotation , true))
                    .into(photoView);

            return true;
        }*/
        return false;
    }
}
