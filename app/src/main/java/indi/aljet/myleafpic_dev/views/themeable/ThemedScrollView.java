package indi.aljet.myleafpic_dev.views.themeable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.Themed;

/**
 * Created by PC-LJL on 2017/11/3.
 */

public class ThemedScrollView extends ScrollView implements Themed {

    public ThemedScrollView(Context context) {
        this(context,null);
    }

    public ThemedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ThemedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        themeHelper.setScrollViewColor(this);
    }
}
