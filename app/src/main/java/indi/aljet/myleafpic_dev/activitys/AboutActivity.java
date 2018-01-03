package indi.aljet.myleafpic_dev.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.view.IconicsImageView;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import horaapps.org.liz.ThemedActivity;
import indi.aljet.myleafpic_dev.BuildConfig;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.util.AlertDialogsHelper;
import indi.aljet.myleafpic_dev.util.CustomTabService;
import indi.aljet.myleafpic_dev.util.StringUtils;

/**
 * Created by LJL-lenovo on 2017/11/22.
 */

public class AboutActivity extends ThemedActivity {

    @BindView(R.id.toolbar)
     Toolbar toolbar;

    private CustomTabService cts;
    @BindView(R.id.aboutAct_scrollView)
     ScrollView scr;
    int emojiEasterEggCount = 0;

    @BindView(R.id.about_changelog_item_sub)
    TextView changelog_sub;

    @BindView(R.id.about_patryk_goworowski_item_sub)
    TextView goworowski_item_sub;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        cts = new CustomTabService(this);
        initUi();
        setUpActions();


    }

    @OnClick(R.id.ll_about_support_github)
    public void onClickGithub(){
        cts.launchUrl("https://github.com/HoraApps/LeafPic");
    }

    @OnClick(R.id.ll_about_report_bug)
    public void onClickReportBug(){
        cts.launchUrl("https://github.com/HoraApps/LeafPic/issues");
    }

    @OnClick(R.id.ll_about_support_translate)
    public void onClickTranslate(){
        cts.launchUrl("https://crowdin.com/project/leafpic");
    }

    @OnClick(R.id.ll_about_support_donate)
    public void onClickDonate(){
//        Intent intent = new Intent(AboutActivity.this,
//                DonateActivity.class);
//        startActivity(intent);
        Toast.makeText(AboutActivity.this,"功能还没用实现",Toast
        .LENGTH_SHORT).show();
        finish();
    }

    @OnClick(R.id.ll_about_license)
    public void onClickLicense(){
        cts.launchUrl("https://github.com/HoraApps/LeafPic/blob/master/LICENSE");
    }


    @OnClick(R.id.ll_about_changelog)
    public void onClickChangeLog(){
        AlertDialog alertDialog = AlertDialogsHelper
                .showChangeLogDialog(AboutActivity.this);
        alertDialog.setButton(DialogInterface
                .BUTTON_POSITIVE, getString(R.string
                .ok_action).toUpperCase(), (dialog,i) ->
        {

        });
        alertDialog.show();
    }

    @OnClick(R.id.about_author_donald_googleplus_item)
    public void onClickDonaldGooleplusItem(){
        cts.launchUrl("https://plus.google.com/103359244653769120543/about");
    }

    @OnClick(R.id.about_author_donald_github_item)
    public void onClickAuthorDonaldGithubItem(){
        cts.launchUrl("https://github.com/DNLDsht");
    }


    @OnClick(R.id.about_author_donald_mail_item)
    public void onClickAuthorDonaldMailItem(){
        mail("dnld.sht@gmail.com");
    }

    @OnClick(R.id.about_author_gilbert_googleplus_item)
    public void onClickAuthorGilbertGoogleplusItem(){
        cts.launchUrl("https://plus.google.com/118430643662868782426/about");
    }


    @OnClick(R.id.about_author_gilbert_github_item)
    public void onClickAuthorGilbertGithubItem(){
        cts.launchUrl("https://github.com/Mow3l");
    }


    @OnClick(R.id.about_author_gilbert_mail_item)
    public void onClickAuthorGilbertMailItem(){
        mail("jibo95@gmail.com");
    }

    @OnClick(R.id.about_gilbert_card)
    public void onClickGilberCard(){
        emojiEasterEgg();
    }

    private void setUpActions(){
        changelog_sub.setText(StringUtils
        .html(getString(R.string.changelog)+" <b>"+ BuildConfig.VERSION_NAME+"</b>"));
        goworowski_item_sub.setMovementMethod(
                LinkMovementMethod
        .getInstance());

    }

    private void emojiEasterEgg(){
        emojiEasterEggCount++;
        if(emojiEasterEggCount > 3){
            Toast.makeText(this,
                    (Hawk.get("emoji_easter_egg",0) == 0
                     ? this.getString(R.string.easter_egg_enable)
             : this.getString(R.string.easter_egg_disable))
            + " " + this.getString(R.string
                    .emoji_easter_egg)
                    ,Toast.LENGTH_SHORT).show();
            Hawk.put("emojo_easter_egg",
                    Hawk.get("emoji_easter_egg",0) == 0?
            1 : 0);
            emojiEasterEggCount = 0;
        }else{
            Toast.makeText(getBaseContext(),
                    String.valueOf(emojiEasterEggCount),
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void mail(String mail){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+mail));
        try{
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(AboutActivity.this,
                    getString(R.string.send_mail_error),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void initUi(){
        setSupportActionBar(toolbar);
        Glide.with(this)
                .load(R.drawable.donald_header)
                .into((ImageView) findViewById(R.id.donald_header_img));
        Glide.with(this)
                .load(R.drawable.donald_profile)
                //.error(new IconicsDrawable(this, "gmd-person").sizeDp(90).color(getIconColor()).paddingDp(24))

                .into((CircleImageView) findViewById(R.id.donald_profile_img));

        Glide.with(this)
                .load(R.drawable.gilbert_header)
                .into((ImageView) findViewById(R.id.gilbert_header_img));
        Glide.with(this)
                .load(R.drawable.gilbert_profile)
                //.error(new IconicsDrawable(this, "gmd-person").sizeDp(90).color)
                .into((CircleImageView) findViewById(R.id.gilbert_profile_img));

        ((TextView) findViewById(R.id.about_version_item_sub)).setText(BuildConfig.VERSION_NAME);
    }

    @CallSuper
    @Override
    public void updateUiElements() {
        super.updateUiElements();
        toolbar.setBackgroundColor(getPrimaryColor());
        toolbar.setNavigationIcon(getToolbarIcon(GoogleMaterial.Icon.gmd_arrow_back));
        toolbar.setNavigationOnClickListener((v) ->{
            onBackPressed();
        });

        setScrollViewColor(scr);
        setStatusBarColor();
        setNavBarColor();
        setRecentApp(getString(R.string.about));

        ((TextView) findViewById(R.id.about_patryk_goworowski_item_sub)).setLinkTextColor(getAccentColor());

        /**** Title Cards ***/
        int color = getAccentColor();
        ((TextView) findViewById(R.id.about_app_title)).setTextColor(color);
        ((TextView) findViewById(R.id.about_special_thanks_title)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_title)).setTextColor(color);
        ((TextView) findViewById(R.id.about_special_thanks_title)).setTextColor(color);
        ((TextView) findViewById(R.id.about_author_donald_mail_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_author_gilbert_mail_item)).setTextColor(color);

        /***** Donald Card *****/
        ((CircleImageView) findViewById(R.id.donald_profile_img)).setBorderColor(getInvertedBackgroundColor());
        ((CardView) findViewById(R.id.about_donald_card)).setCardBackgroundColor(getCardBackgroundColor());
        ((TextView) findViewById(R.id.donald_shtjefni)).setTextColor(getTextColor());
        ((TextView) findViewById(R.id.donald_description)).setTextColor(getSubTextColor());
        color = getAccentColor();
        ((TextView) findViewById(R.id.about_author_donald_mail_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_author_donald_googleplus_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_author_donald_github_item)).setTextColor(color);

        /***** Gilbert Card *****/
        ((CircleImageView) findViewById(R.id.gilbert_profile_img)).setBorderColor(getInvertedBackgroundColor());
        ((CardView) findViewById(R.id.about_gilbert_card)).setCardBackgroundColor(getCardBackgroundColor());
        ((TextView) findViewById(R.id.gilbert_ndresaj)).setTextColor(getTextColor());
        ((TextView) findViewById(R.id.gilbert_description)).setTextColor(getSubTextColor());
        color = getAccentColor();
        ((TextView) findViewById(R.id.about_author_gilbert_mail_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_author_gilbert_googleplus_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_author_gilbert_github_item)).setTextColor(color);


        findViewById(R.id.about_background).setBackgroundColor(getBackgroundColor());

        /** Cards **/
        color = getCardBackgroundColor();
        ((CardView) findViewById(R.id.about_app_card)).setCardBackgroundColor(color);
        ((CardView) findViewById(R.id.about_special_thanks_card)).setCardBackgroundColor(color);
        ((CardView) findViewById(R.id.about_support_card)).setCardBackgroundColor(color);

        /** Icons **/
        color = getIconColor();

        //ABOUT SUPPORT
        ((IconicsImageView) findViewById(R.id.about_support_translate_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.about_support_rate_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.about_support_github_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.about_support_report_bug_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.about_support_donate_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.about_license_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.about_changelog_icon)).setColor(color);

        /** TextViews **/
        color = getTextColor();
        ((TextView) findViewById(R.id.about_app_light_description)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_rate_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_translate_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_github_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_report_bug_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_donate_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_license_item)).setTextColor(color);
        ((TextView) findViewById(R.id.about_changelog_item)).setTextColor(color);

        /** Sub Text Views**/
        color = getSubTextColor();
        ((TextView) findViewById(R.id.about_version_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_patryk_goworowski_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_community_members_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_community_you_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_rate_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_translate_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_github_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_report_bug_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_support_donate_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.donald_shtjefni_role)).setTextColor(color);
        ((TextView) findViewById(R.id.gilbert_ndresaj_role)).setTextColor(color);
        ((TextView) findViewById(R.id.about_license_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_changelog_item_sub)).setTextColor(color);
    }
}
