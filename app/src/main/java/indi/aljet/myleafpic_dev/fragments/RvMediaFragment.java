package indi.aljet.myleafpic_dev.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ThemeHelper;
import indi.aljet.myleafpic_dev.activitys.MainActivity;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.activitys.PaletteActivity;
import indi.aljet.myleafpic_dev.activitys.SingleMediaActivity;
import indi.aljet.myleafpic_dev.adapters.MediaAdapter;
import indi.aljet.myleafpic_dev.data.Album;
import indi.aljet.myleafpic_dev.data.AlbumsHelper;
import indi.aljet.myleafpic_dev.data.HandlingAlbums;
import indi.aljet.myleafpic_dev.data.Media;
import indi.aljet.myleafpic_dev.data.MediaHelper;
import indi.aljet.myleafpic_dev.data.filter.FilterMode;
import indi.aljet.myleafpic_dev.data.filter.MediaFilter;
import indi.aljet.myleafpic_dev.data.provider.CPHelper;
import indi.aljet.myleafpic_dev.data.sort.SortingMode;
import indi.aljet.myleafpic_dev.data.sort.SortingOrder;
import indi.aljet.myleafpic_dev.sql.AlbumsSqlData;
import indi.aljet.myleafpic_dev.util.AlertDialogsHelper;
import indi.aljet.myleafpic_dev.util.Measure;
import indi.aljet.myleafpic_dev.util.StringUtils;
import indi.aljet.myleafpic_dev.views.GridSpacingItemDecoration;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * Created by PC-LJL on 2017/11/20.
 */

public class RvMediaFragment extends BaseFragment {

    private static final String TAG = "asd";

    @BindView(R.id.media)
    RecyclerView rv;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refresh;

    private MediaAdapter adapter;
    private GridSpacingItemDecoration spacingDecoration;

    private MainActivity act;

    private Album album;

    public static RvMediaFragment make(Album album){
        RvMediaFragment f = new RvMediaFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("album",album);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        album = getArguments().getParcelable("album");
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = ((MainActivity) context);
    }

    @Override
    public void onResume() {
        super.onResume();
        clearSelected();
        updateToolbar();
    }

    private void display(){
        adapter.clear();
        CPHelper.getMedia(getContext(),
                album, sortingMode(),sortingOrder())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(media -> MediaFilter.getFilter(album
                .fileterMode()).accept(media))
                .subscribe(media -> adapter.add(media),
                        throwable -> {
                            refresh.setRefreshing(false);
                            Log.wtf("asd", throwable);
                        },() ->{
                            album.setCount(getCount());
                            act.nothingToShow(getCount() == 0);
                            refresh.setRefreshing(false);
                        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout
        .frament_rv_media,null);
        ButterKnife.bind(this,v);

        int spanCount = columnsCount();
        spacingDecoration = new GridSpacingItemDecoration(spanCount,
                Measure.pxToDp(3,getContext()),true);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(spacingDecoration);
        rv.setLayoutManager(new GridLayoutManager(getContext()
        ,spanCount));
        rv.setItemAnimator(new LandingAnimator(new
                OvershootInterpolator(1f)));

        adapter = new MediaAdapter(getContext(),
                sortingMode(),sortingOrder());

        adapter.getClicks()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pos -> {
                    Intent intent = new Intent(getActivity(),
                            SingleMediaActivity.class);
                    intent.setAction(SingleMediaActivity.
                            ACTION_OPEN_ALBUM);
                    intent.putExtra("album",RvMediaFragment
                    .this.album);
                    intent.putExtra("media",adapter.getMedia());
                    intent.putExtra("position",pos);
                    getContext().startActivity(intent);

                });
        adapter.getSelectedClicks()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers
                .mainThread()).subscribe(album
        -> {
            updateToolbar();
            getActivity().invalidateOptionsMenu();
        });
        refresh.setOnRefreshListener(this :: display);
        rv.setAdapter(adapter);
        return v;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        display();
    }



    /**
     *苦逼来
     * @return
     */


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpColumns();
    }

    public void setUpColumns(){
        int columnsCount = columnsCount();
        if(columnsCount != ((GridLayoutManager) rv
        .getLayoutManager()).getSpanCount()){
            ((GridLayoutManager) rv
            .getLayoutManager()).getSpanCount();
            rv.removeItemDecoration(spacingDecoration);
            spacingDecoration = new GridSpacingItemDecoration(
                    columnsCount,Measure.pxToDp(3,
                    getContext()),true);
            rv.setLayoutManager(new GridLayoutManager(getContext()
            ,columnsCount));
            rv.addItemDecoration(spacingDecoration);
        }
    }





    public int columnsCount(){
        return getResources().getConfiguration()
                .orientation == Configuration
                .ORIENTATION_PORTRAIT ? Hawk.get("n_columns_media",3)
                : Hawk.get("n_columns_media_landscape,4");
    }

    private void updateToolbar(){
        if(editMode()){
            act.updateToolbar(String
            .format(Locale.ENGLISH,
                    "%d/%d",adapter.getSelectedCount(),
                    adapter.getItemCount()),
                    GoogleMaterial.Icon.gmd_check,
                    v -> adapter.clearSelected());
        }else{
            act.updateToolbar(album.getName(),
                    GoogleMaterial.Icon
            .gmd_arrow_back,
                    v-> act.goBackToAlbums());
        }
    }

    public SortingMode sortingMode(){
        return adapter != null ?
                adapter.sortingMode() :
                album.settings.getSortingMode();
    }

    public SortingOrder sortingOrder(){
        return adapter != null
                ? adapter.sortingOrder()
                : album.settings.getSortingOrder();
    }

    private HandlingAlbums db(){
        return HandlingAlbums.getInstance(getContext()
        .getApplicationContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.grid_media,menu);
        menu.findItem(R.id.select_all)
                .setIcon(ThemeHelper.getToolbarIcon(getContext()
                ,GoogleMaterial.Icon.gmd_select_all));
        menu.findItem(R.id.delete).setIcon(
                ThemeHelper.getToolbarIcon(getContext(),
                        (GoogleMaterial.Icon.gmd_delete)));
        menu.findItem(R.id.sharePhotos).setIcon(ThemeHelper.getToolbarIcon(getContext(),(GoogleMaterial.Icon.gmd_share)));
        menu.findItem(R.id.sort_action).setIcon(ThemeHelper.getToolbarIcon(getContext(),(GoogleMaterial.Icon.gmd_sort)));
        menu.findItem(R.id.filter_menu).setIcon(ThemeHelper.getToolbarIcon(getContext(), (GoogleMaterial.Icon.gmd_filter_list)));
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean editMode = editMode();
        boolean oneSelected =
                getSelectedCount() == 1;
        menu.setGroupVisible(R.id.general_album_items,
                !editMode);
        menu.setGroupVisible(R.id.edit_mode_items,editMode);
        menu.setGroupVisible(R.id.one_selected_items,oneSelected);

        menu.findItem(R.id.select_all).setTitle(
                getSelectedCount() == getCount()
                ? R.string.clear_selected :
                        R.string.select_all);
        if(editMode){
            menu.findItem(R.id.filter_menu)
                    .setVisible(false);
            menu.findItem(R.id.sort_action).
                    setVisible(false);
        }else{
            menu.findItem(R.id.filter_menu).setVisible(true);
            menu.findItem(R.id.sort_action).setVisible(true);
            menu.findItem(R.id.ascending_sort_order)
                    .setChecked(sortingOrder() == SortingOrder
                    .ASCENDING);
            switch (sortingMode()){
                case NAME:
                    menu.findItem(R.id.name_sort_mode)
                            .setChecked(true);
                    break;
                case SIZE:
                    menu.findItem(R.id.size_sort_mode).
                            setChecked(true);
                    break;
                case DATE: default:
                    menu.findItem(R.id.date_taken_sort_mode).
                            setChecked(true);
                    break;
                case NUMERIC:  menu.findItem(R.id.numeric_sort_mode).
                        setChecked(true);
                    break;
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.all_media_filter:
                album.setFilterMode(FilterMode.ALL);
                item.setChecked(true);
                display();
                return true;
            case R.id.video_media_filter:
                album.setFilterMode(
                        FilterMode.VIDEO);
                item.setChecked(true);
                display();
                return true;
            case R.id.image_media_filter:
                album.setFilterMode(
                        FilterMode.IMAGES);
                display();
                return true;
            case R.id.gifs_media_filter:
                album.setFilterMode(FilterMode.GIF);
                item.setChecked(true);
                display();
                return true;
            case R.id.sharePhotos:
                Intent intent = new Intent();
                intent.setAction(Intent
                .ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.sent_to_action));
                ArrayList<Uri> files = new ArrayList<>();
                for(Media f : adapter.getSelected()){
                    files.add(f.getUri());
                }
                intent.putParcelableArrayListExtra(
                        Intent.EXTRA_STREAM,files);
                intent.setType("*/*");
                startActivity(Intent
                .createChooser(intent,
                        getResources().getText(R
                        .string.send_to
                        )));
                return true;
            case R.id.set_as_cover:
                String path = adapter.getFirstSelected()
                        .getPath();
                album.setCover(path);
//                db().setCover(album.getPath(),path);
                AlbumsSqlData.getInstance(getContext())
                        .setCover(album.getPath(),path);
                adapter.clearSelected();
                break;
            case R.id.action_palette:
                Intent paletteIntent = new Intent(act,
                        PaletteActivity.class);
                paletteIntent.setData(adapter
                .getFirstSelected().getUri());
                startActivity(paletteIntent);
                return true;

            case R.id.rename:
                final EditText editTextNewName = new
                        EditText(getActivity());
                editTextNewName.setText(StringUtils
                .getPhotoNameByPath(adapter
                .getFirstSelected().getPath()));

                AlertDialog renameDialog = AlertDialogsHelper
                        .getInsertTextDialog(act,
                                editTextNewName,R.string
                        .rename_photo_action);
                renameDialog.setButton(DialogInterface
                .BUTTON_POSITIVE,getString(R
                .string.ok_action).toUpperCase(),
                        (dialog,which) -> {
                            if(editTextNewName
                                    .length() != 0){
                                boolean b = MediaHelper
                                        .renameMedia(getActivity(),
                                                adapter.getFirstSelected(),
                                                editTextNewName
                                        .getText().toString());
                                if(!b ){
                                    StringUtils.showToast(getActivity(),
                                            getString(R
                                            .string.rename_error));
                                }else{
                                    adapter.clearSelected();
                                }
                            }else{
                                StringUtils.showToast(getActivity(),
                                        getString(R.string
                                        .nothing_changed));
                            }
                        } );
                renameDialog.setButton(DialogInterface
                        .BUTTON_NEGATIVE,getString(R.string.cancel)
                        .toUpperCase(),(dialog,which) ->dialog
                        .dismiss());
                renameDialog.show();
                return true;
            case R.id.select_all:
                if(adapter.getSelectedCount() ==
                        adapter.getItemCount()){
                    adapter.clearSelected();
                }else{
                    adapter.selectAll();
                }
                return true;
            case R.id.name_sort_mode:
                adapter.changeSortingMode(SortingMode.NAME);
                AlbumsHelper.setSortingMode(getContext()
                ,SortingMode.NAME);
                item.setChecked(true);
                return true;

            case R.id.date_taken_sort_mode:
                adapter.changeSortingMode(SortingMode.DATE);
                AlbumsHelper.setSortingMode(getContext()
                ,SortingMode.DATE);
                item.setChecked(true);
                return true;

            case R.id.size_sort_mode:
                adapter.changeSortingMode(SortingMode.SIZE);
                AlbumsHelper.setSortingMode(
                        getContext(),SortingMode.SIZE);
                item.setChecked(true);
                return true;
            case R.id.numeric_sort_mdoe:
                adapter.changeSortingMode(SortingMode.NUMERIC);
                AlbumsHelper.setSortingMode(getContext(),
                        SortingMode.NUMERIC);
                item.setChecked(true);
                return true;
            case R.id.ascending_sort_order:
                item.setChecked(!item.isChecked());
                SortingOrder sortingOrder = SortingOrder
                        .fromValue(item.isChecked());
                adapter.changeSortingOrder(sortingOrder);
                AlbumsHelper.setSortingOrder(getContext(),
                        sortingOrder);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public int getCount(){
        return adapter.getItemCount();
    }

    public int getSelectedCount(){
        return adapter.getSelectedCount();
    }

    @Override
    public boolean editMode() {
        return adapter.selecting();
    }

    @Override
    public void clearSelected() {
        adapter.clearSelected();
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        rv.setBackgroundColor(themeHelper
        .getBackgroundColor());
        adapter.refreshTheme(themeHelper);
        refresh.setColorSchemeColors(themeHelper
        .getAccentColor());
        refresh.setProgressBackgroundColorSchemeColor(
                themeHelper.getBackgroundColor()
        );
    }
}
