package indi.aljet.myleafpic_dev.views.themeable;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.Themed;

/**
 * Created by PC-LJL on 2017/10/13.
 */

public class ThemedToolbar extends Toolbar implements Themed {

    public ThemedToolbar(Context context) {
        super(context);
    }

    public ThemedToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemedToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        setBackgroundColor(themeHelper
        .getPrimaryColor());
    }
}
