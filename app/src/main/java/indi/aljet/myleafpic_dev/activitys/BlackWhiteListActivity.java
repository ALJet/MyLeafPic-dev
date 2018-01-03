package indi.aljet.myleafpic_dev.activitys;

import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.view.IconicsImageView;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.SelectAlbumBuilder;
import indi.aljet.myleafpic_dev.activitys.base.SharedMediaActivity;
import indi.aljet.myleafpic_dev.data.HandlingAlbums;
import indi.aljet.myleafpic_dev.data.filter.ImageFileFilter;
import indi.aljet.myleafpic_dev.data.provider.ContentProviderHelper;
import indi.aljet.myleafpic_dev.util.StringUtils;

/**
 * Created by LJL-lenovo on 2017/11/22.
 */

public class BlackWhiteListActivity extends
        SharedMediaActivity {

    public static final String EXTRA_TYPE =
            "typeExcluded";

    @BindView(R.id.excluded_albums)
    RecyclerView mRecyclerView;
    private ItemsAdapter adapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<String> folders = new
            ArrayList<>();
    private boolean typeExcluded;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_white_list);
        ButterKnife.bind(this);
        initUi();
        loadFolders(getIntent()
        .getIntExtra(EXTRA_TYPE, HandlingAlbums
        .EXCLUDED));
    }

    private void loadFolders(int type){
        this.typeExcluded = type == HandlingAlbums
                .EXCLUDED;
        folders = getAlbums().getFolders(type);
        checkNothing();
        if(isExcludedMode()){
            setTitle(getString(R.string
            .excluded_items));
        }else{
            setTitle(getString(R.string
            .white_list));
        }
        adapter.notifyDataSetChanged();
        supportInvalidateOptionsMenu();
    }

    private boolean isExcludedMode(){
        return typeExcluded;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu
        .menu_black_white_list,menu);
        menu.findItem(R.id.action_add)
                .setIcon(getToolbarIcon(GoogleMaterial
                .Icon.gmd_add_circle));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(isExcludedMode()){
            menu.findItem(R.id.action_add)
                    .setVisible(false);
            menu.findItem(R.id.action_toggle)
                    .setTitle(R.string.white_list);
        }else{
            menu.findItem(R.id.action_add).
                    setVisible(true);
            menu.findItem(R.id.action_toggle).
                    setTitle(R.string.excluded_items);
        }
        return super.onPrepareOptionsMenu(menu);
    }



    private void addFolder(final File dir){
        String[] list = dir.list(new
                ImageFileFilter(true));
        final boolean[] found = {false};
        if(list != null && list.length > 0){
            MediaScannerConnection.scanFile(getApplicationContext()
            ,list,null,(s,uri) ->{
                        long albumId = ContentProviderHelper
                                .getAlbumId(getApplicationContext(),
                                        s);
                        if(albumId != -1){
                            found[0] = true;
                            Toast.makeText(BlackWhiteListActivity.this,
                                    "got the ID",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            getAlbums().addFolderToWhiteList(dir.getPath());
            folders.add(0,dir.getPath());
            adapter.notifyItemInserted(0);
            checkNothing();
        }else{
            Toast.makeText(this,R.string
            .no_media_in_this_folder,Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void checkNothing(){
        findViewById(R.id.white_list_description_card)
                .setVisibility(isExcludedMode() || !(Hawk.get(
                        "preference_show_tips"
                ,true)) ? View.GONE : View.VISIBLE);
        findViewById(R.id.nothing_to_show_text_emoji_easter_egg)
                .setVisibility(
                        folders.size() < 1 && isExcludedMode() &&
                                Hawk.get("emoji_easter_egg",0)
                        == 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.ll_emoji_easter_egg)
                .setVisibility(folders.size() < 1 &&
                isExcludedMode() && Hawk.get("emoji_easter_egg",0)
                 == 1 ? View.VISIBLE : View.GONE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                SelectAlbumBuilder.with(getSupportFragmentManager())
                        .title(getString(R.string
                        .chose_folders)).exploreMode(true)
                        .force(true).onFolderSelected(
                        (path) ->{
                            addFolder(new File(path));
                        }).show();
                return true;
            case R.id.action_toggle:
                loadFolders(isExcludedMode() ?
                HandlingAlbums.INCLUDED :
                HandlingAlbums.EXCLUDED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUi(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getToolbarIcon(GoogleMaterial
        .Icon.gmd_arrow_back));
        toolbar.setNavigationOnClickListener(
                v ->{
                    onBackPressed();
                });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter((adapter = new
                ItemsAdapter()));
        mRecyclerView.setLayoutManager(new
                GridLayoutManager(this,1));
        mRecyclerView.setItemAnimator(new
                DefaultItemAnimator());
    }

    @CallSuper
    @Override
    public void updateUiElements() {
        super.updateUiElements();
        toolbar.setBackgroundColor(getPrimaryColor());
        mRecyclerView.setBackgroundColor(getBackgroundColor());
        setStatusBarColor();
        setNavBarColor();
        toolbar.setTitle(getTitle());
        setRecentApp(getTitle().toString());
        ((CardView) findViewById(R.id.
                white_list_description_card)).setCardBackgroundColor(getCardBackgroundColor());
        ((TextView) findViewById(R.id.
                white_list_description_txt)).setTextColor(getTextColor());
        //TODO: EMOJI EASTER EGG - THERE'S NOTHING TO SHOW
        ((TextView) findViewById(R.id.emoji_easter_egg)).setTextColor(getSubTextColor());
        ((TextView) findViewById(R.id.nothing_to_show_text_emoji_easter_egg)).setTextColor(getSubTextColor());

        findViewById(R.id.rl_ea).setBackgroundColor(getBackgroundColor());
    }

    class  ItemsAdapter extends RecyclerView.Adapter
    <ItemsAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent
            .getContext())
                    .inflate(R.layout.card_track_folder,parent
                    ,false);
            v.findViewById(R.id.remove_icon)
                    .setOnClickListener(listener);
            return new ItemsAdapter
                    .ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String itm = folders.get(position);
            holder.path.setText(itm);
            holder.name.setText(StringUtils.getName(itm));
            holder.imgRemove.setTag(itm);

            holder.name.setTextColor(getTextColor());
            holder.path.setTextColor(getSubTextColor());
            holder.imgFolder.setColor(getIconColor());
            holder.imgRemove.setColor(getIconColor());
            holder.layout.setBackgroundColor(getCardBackgroundColor());
        }

        @Override
        public int getItemCount() {
            return folders.size();
        }

        private View.OnClickListener listener = v ->{
            String path = (String) v.getTag();
            int i = folders.indexOf(path);
            getAlbums().clearStatusFolder(path);
            folders.remove(i);
            notifyItemRemoved(i);
            checkNothing();
        };

        public class ViewHolder extends
                RecyclerView.ViewHolder{

            @BindView(R.id.linear_card_excluded)
            LinearLayout layout;
            @BindView(R.id.folder_icon)
            IconicsImageView imgFolder;
            @BindView(R.id.remove_icon)
            IconicsImageView imgRemove;
            @BindView(R.id.folder_name)
            TextView name;
            @BindView(R.id.folder_path)
            TextView path;



            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }


}
