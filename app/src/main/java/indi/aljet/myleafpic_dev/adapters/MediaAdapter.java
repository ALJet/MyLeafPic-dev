package indi.aljet.myleafpic_dev.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.ThemedAdapter;
import horaapps.org.liz.ThemedViewHolder;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.data.Media;
import indi.aljet.myleafpic_dev.data.sort.MediaComparators;
import indi.aljet.myleafpic_dev.data.sort.SortingMode;
import indi.aljet.myleafpic_dev.data.sort.SortingOrder;
import indi.aljet.myleafpic_dev.util.Measure;
import indi.aljet.myleafpic_dev.views.SquareRelativeLayout;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by PC-LJL on 2017/11/20.
 */

public class MediaAdapter extends ThemedAdapter<MediaAdapter
        .ViewHolder> {


    private ArrayList<Media> media;

    private final PublishSubject<Integer> onClickSubject =
            PublishSubject.create();

    private final PublishSubject<Media> onChangeSelectedSubject =
            PublishSubject.create();

    private int selectedCount = 0;
    private SortingOrder sortingOrder;
    private SortingMode sortingMode;

    private Drawable placeholder;


    public MediaAdapter(Context context,
                        SortingMode sortingMode,
                        SortingOrder sortingOrder
                        ) {
        super(context);
        media = new ArrayList<>();
        placeholder = getThemeHelper()
        .getPlaceHolder();
        this.sortingOrder = sortingOrder;
        this.sortingMode = sortingMode;
        setHasStableIds(true);
    }


    public void sort(){
        Collections.sort(media, MediaComparators
        .getComparator(sortingMode,sortingOrder));
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return media.get(position)
                .getUri().hashCode() ^ 1312;
    }

    public SortingOrder sortingOrder() {
        return sortingOrder;
    }

    public void changeSortingOrder(SortingOrder sortingOrder){
        this.sortingOrder = sortingOrder;
        Collections.reverse(media);
        notifyDataSetChanged();
    }


    public SortingMode sortingMode() {
        return sortingMode;
    }


    public void changeSortingMode(SortingMode sortingMode) {
        this.sortingMode = sortingMode;
        sort();
    }

    public ArrayList<Media> getSelected(){
        if(Build.VERSION.SDK_INT >= Build
                .VERSION_CODES.N){
            return new ArrayList<>(media.stream().
                    filter(Media::isSelected).
                    collect(Collectors.toList()));
        }else{
            ArrayList<Media> arrayList = new ArrayList<>(
                    selectedCount);
            for (Media m : media){
                if(m.isSelected()){
                    arrayList.add(m);
                }
            }
            return arrayList;
        }
    }


    public Media getFirstSelected(){
        if(selectedCount > 0){
            if(Build.VERSION.SDK_INT >= Build
                    .VERSION_CODES.N){
                return media.stream().filter
                        (Media::isSelected).findFirst().orElse(null);
            }else{
                for(Media m : media){
                    if (m.isSelected())
                        return m;
                }
            }
        }
        return null;
    }

    public ArrayList<Media> getMedia(){
        return media;
    }

    public int getSelectedCount(){
        return selectedCount;
    }

    public void selectAll(){
        for(int i = 0; i < media.size();i++){
            if(media.get(i).setSelected(true)){
                notifyDataSetChanged();
            }
            selectedCount = media.size();
            onChangeSelectedSubject.onNext(new Media());
        }
    }


    public void clearSelected(){
        for(int i = 0;i < media.size();i++){
            if(media.get(i).setSelected(false)){
                notifyDataSetChanged();
            }
        }
        selectedCount = 0;
        onChangeSelectedSubject.onNext(new Media());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
        .from(parent.getContext()).inflate(R
                .layout.card_photo,parent,false));
    }


    private void notifySelected(boolean increase){
        selectedCount += increase ? 1 : -1;
    }

    public boolean selecting(){
        return selectedCount > 0;
    }

    public Observable<Integer> getClicks(){
        return onClickSubject;
    }

    public Observable<Media> getSelectedClicks(){
        return onChangeSelectedSubject;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Media f = media.get(position);
        holder.icon.setVisibility(View.GONE);

        if(f.isGif()){
            Ion.with(holder.imageView.getContext())
                    .load(f.getPath())
                    .intoImageView(holder.imageView);
            holder.gifIcon.setVisibility(View.VISIBLE);
        }else{
            RequestOptions options = new
                    RequestOptions()
                    .signature(f.getSignature())
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .centerCrop()
                    .placeholder(placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(holder.imageView.getContext())
                    .load(f.getUri())
                    .apply(options)
                    .thumbnail(0.5f)
                    .into(holder.imageView);
            holder.gifIcon.setColor(View.GONE);
        }
        if(f.isVideo()){
            holder.icon.setVisibility(View.VISIBLE);
            holder.path.setVisibility(View.VISIBLE);
            holder.path.setText(f.getName());
            holder.icon.setIcon(CommunityMaterial
            .Icon.cmd_play_circle);
            holder.icon.animate().alpha(1)
                    .setDuration(250);
            holder.path.animate().alpha(1)
                    .setDuration(250);
        }else{
            holder.icon.setVisibility(View.GONE);
            holder.path.setVisibility(View.GONE);
            holder.icon.animate().alpha(0)
                    .setDuration(250);
            holder.path.animate().alpha(0)
                    .setDuration(250);
        }

        if(f.isSelected()){
            holder.icon.setIcon(CommunityMaterial
            .Icon.cmd_check);
            holder.icon.setVisibility(View.VISIBLE);
            holder.imageView.setColorFilter(0x88000000,
                    PorterDuff.Mode.SRC_ATOP);
            holder.layout.setPadding(15,15,15,15);
            holder.icon.animate().alpha(1)
                    .setDuration(250);
        }else{
            holder.imageView.clearColorFilter();
            holder.layout.setPadding(0,0,0,0);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selecting()){
                    notifySelected(f.toggleSelected());
                    notifyItemChanged(position);
                    onChangeSelectedSubject.onNext(f);
                }else{
                    onClickSubject.onNext(position);
                }
            }});
        holder.layout.setOnLongClickListener(v -> {
            if(!selecting()){
                notifySelected(f.toggleSelected());
                notifyItemChanged(position);
                onChangeSelectedSubject.onNext(f);
            }else{
                selectAllUpTo(f);
                onChangeSelectedSubject.onNext(new
                        Media());
            }
            return true;
        });
    }

    @Override
    public void refreshTheme(ThemeHelper theme) {
        placeholder = theme.getPlaceHolder();
    }


    public void selectAllUpTo(Media m){
        int targetIndex = media.indexOf(m);

        int indexRightBeforeOrAfter = -1;
        int indexNow;

        for(Media sm : getSelected()){
            indexNow = media.indexOf(sm);
            if(indexRightBeforeOrAfter == -1){
                indexRightBeforeOrAfter = indexNow;
            }
            if(indexNow > targetIndex){
                break;
            }
            indexRightBeforeOrAfter = indexNow;
        }

        if(indexRightBeforeOrAfter != -1){
            for(int index = Math.min(targetIndex,
                    indexRightBeforeOrAfter);index <= Math
                    .max(targetIndex,indexRightBeforeOrAfter);
                    index++){
                if(media.get(index) !=  null){
                    if(media.get(index).setSelected(true)){
                        notifySelected(true);
                        notifyItemChanged(index);
                    }
                }
            }
        }
    }

    public void clear(){
        media.clear();
        notifyDataSetChanged();
    }


    public int add(Media album){
        int i = Collections.binarySearch(media,
                album,MediaComparators
        .getComparator(sortingMode,sortingOrder));
        if(i < 0)
            i = ~i;
        media.add(i ,album);
        notifyItemInserted(i);
        return i;
    }


    @Override
    public int getItemCount() {
        return media.size();
    }


    static class ViewHolder extends
            ThemedViewHolder{
        @BindView(R.id.photo_preview)
        ImageView imageView;
        @BindView(R.id.photo_path)
        TextView path;
        @BindView(R.id.gif_icon)
        IconicsImageView gifIcon;
        @BindView(R.id.icon)
        IconicsImageView icon;
        @BindView(R.id.media_card_layout)
        SquareRelativeLayout layout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        @Override
        public void refreshTheme(ThemeHelper themeHelper) {
            icon.setColor(Color.WHITE);
            Log.wtf("asd","asdasd");
        }
    }
}
