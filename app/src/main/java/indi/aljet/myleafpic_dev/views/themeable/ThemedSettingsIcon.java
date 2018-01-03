package indi.aljet.myleafpic_dev.views.themeable;

import android.content.Context;
import android.util.AttributeSet;

import com.mikepenz.iconics.view.IconicsImageView;

import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.Themed;

/**
 * Created by PC-LJL on 2017/11/3.
 */

public class ThemedSettingsIcon extends IconicsImageView implements Themed {
    public ThemedSettingsIcon(Context context) {
        this(context,null);
    }

    public ThemedSettingsIcon(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ThemedSettingsIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        setColor(themeHelper.getIconColor());
    }
}
