package indi.aljet.myleafpic_dev.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.Themed;
import horaapps.org.liz.ThemedActivity;
import indi.aljet.myleafpic_dev.App;
import indi.aljet.myleafpic_dev.R;

/**
 * Created by PC-LJL on 2017/11/3.
 */

public class SettingWithSwitchView extends FrameLayout
        implements View.OnClickListener,Themed {

    private final String iconString;
    private final String preferenceKey;
    @StringRes
    private final int titleRes;
    @StringRes
    private final int captionRes;
    private final boolean defaultValue;
    private final String cationStr;
    @BindView(R.id.icon)
    IconicsImageView icon;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.caption)
    TextView caption;
    @BindView(R.id.toggle)
    SwitchCompat toggle;
    @Nullable
    private OnClickListener clickListener;


    public SettingWithSwitchView(Context context) {
        this(context,null);
    }

    public SettingWithSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingWithSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        Hawk.init(App.getmInstance()).build();
        setBackgroundResource(R.drawable
        .ripple);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_setting_switch,this);
        TypedArray a = getContext()
                .obtainStyledAttributes(attrs,R.styleable
                .SettingWithSwitchView);
        iconString = a.getString
                (R.styleable.SettingWithSwitchView_settingIcon);
        final int prefKeyRes = a.getResourceId(R.styleable
        .SettingWithSwitchView_settingPreferenceKey,0);
        if(prefKeyRes == 0)
            throw  new IllegalArgumentException("Invalid preference reference");
        preferenceKey = getResources().getString(prefKeyRes);
        titleRes = a.getResourceId(R.styleable.SettingWithSwitchView_settingTitle,0);
        captionRes = a.getResourceId(R.styleable.SettingWithSwitchView_settingCaption,0);
        cationStr = a.getString(R.styleable.SettingWithSwitchView_settingCaption);
        defaultValue = a.getBoolean(R.styleable
        .SettingWithSwitchView_settingDefaultValue,false);
        int minimumApi = 0;
        a.recycle();
        if(Build.VERSION.SDK_INT < minimumApi){
            setVisibility(GONE);
        }

    }

    @Override
    protected void onFinishInflate() {

        ButterKnife.bind(this);
        icon.setIcon(iconString);
        title.setText(titleRes);
        caption.setText(cationStr);
//        caption.setText("XXXXX");
        toggle.setChecked(isChecked());
        super.setOnClickListener(this);
        super.onFinishInflate();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.clickListener = l;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            refreshTheme(((ThemedActivity) getContext()).getThemeHelper());
        }
    }


    @Override
    public void onClick(View view) {
        toggle();
        refreshTheme(((ThemedActivity) getContext())
        .getThemeHelper());
        if(clickListener != null){
            clickListener.onClick(this);
        }
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        themeHelper.setSwitchCompactColor(toggle,
                ThemeHelper.getAccentColor(getContext()));
    }

    public boolean isChecked(){
        return Hawk.get(preferenceKey,
                defaultValue);
    }

    public boolean toggle(){
        Hawk.put(preferenceKey, !isChecked());
        boolean checked = isChecked();
        toggle.setChecked(checked);
        return checked;
    }
}
