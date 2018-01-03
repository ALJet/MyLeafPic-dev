package indi.aljet.myleafpic_dev.activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ThemedActivity;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.util.StringUtils;


/**
 * Created by PC-LJL on 2017/11/23.
 */

public class PaletteActivity extends ThemedActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.palette_image)
    ImageView paletteImg;
    private Uri uri;

    private Palette palette;
    @BindView(R.id.paletteRecycler)
    RecyclerView rvPalette;
    private PaletteAdapter paletteAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        uri = getIntent().getData();
        paletteImg.setImageURI(null);
        paletteImg.setImageURI(uri);
        setPalette();
    }

    @CallSuper
    @Override
    public void updateUiElements() {
        super.updateUiElements();
        toolbar.setBackgroundColor(getPrimaryColor());
        toolbar.setNavigationIcon(getToolbarIcon(
                GoogleMaterial.Icon.gmd_arrow_back));
        toolbar.setNavigationOnClickListener( v ->{
            onBackPressed();
        });
        setStatusBarColor();
        setNavBarColor();
        setRecentApp(getString(R.string.palette));

        findViewById(R.id.palette_background).
                setBackgroundColor(getBackgroundColor());
        ((CardView) findViewById(R.id.palette_image_card))
                .setCardBackgroundColor(getCardBackgroundColor());
        ((TextView) findViewById(R.id.palette_image_title)).
                setTextColor(getTextColor());
        ((TextView) findViewById(R.id.palette_image_caption)).
                setTextColor(getSubTextColor());
    }

    public void setPalette(){
        Bitmap myBitmap = ((BitmapDrawable) paletteImg
        .getDrawable()).getBitmap();
        ((TextView)findViewById(R.id.palette_image_title))
                .setText(uri.getPath().substring(uri.getPath()
                .lastIndexOf("/")));
        ((TextView)findViewById(R.id.palette_image_caption))
                .setText(uri.getPath());
        palette = Palette.from(myBitmap).generate();
        rvPalette.setLayoutManager(new
                LinearLayoutManager(this));
        rvPalette.setNestedScrollingEnabled(false);
        paletteAdapter = new PaletteAdapter(palette
        .getSwatches());
        rvPalette.setAdapter(paletteAdapter);
    }


    private View.OnClickListener onClickListener = view -> {
        String text = ((TextView) view.findViewById
                (R.id.palette_item_text)).getText().toString();
        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Palette Color",
                text);
        clipboardManager.setPrimaryClip(clip);
        StringUtils.showToast(getApplicationContext(),
                getString(R.string.color) + ": " + text
        + " " +getString(R.string.copy_clipboard));
    };

    private class PaletteAdapter extends
            RecyclerView.Adapter<PaletteAdapter.ViewHolder>{

        private List<Palette.Swatch> swatches;

        private PaletteAdapter(List<Palette.Swatch> sws){
            this.swatches = sws;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.palette_item,parent,false);
            v.setOnClickListener(onClickListener);
            return new PaletteActivity.PaletteAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Palette.Swatch sw = swatches.get(position);
            holder.txtColor.setTextColor(sw.getTitleTextColor());
            holder.txtColor.setText(String.format("#%06X",
                    (0xFFFFFF & sw.getRgb())));
            holder.itemBackground
                    .setBackgroundColor(sw.getRgb());
        }


        @Override
        public int getItemCount() {
            return null != swatches ?
                    swatches.size() : 0;
        }

         class ViewHolder extends RecyclerView.ViewHolder{

//            @BindView(R.id.palette_item_text)
            TextView txtColor;
//            @BindView(R.id.ll_palette_item)
            LinearLayout itemBackground;

              ViewHolder(View itemView) {
                super(itemView);
//                  ButterKnife.bind(this,itemView);
                 txtColor = (TextView) itemView.findViewById(R.id.palette_item_text);
                 itemBackground = (LinearLayout) itemView.findViewById(R.id.ll_palette_item);
            }
        }
    }



}
