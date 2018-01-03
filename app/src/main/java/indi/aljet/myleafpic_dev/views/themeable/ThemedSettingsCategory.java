package indi.aljet.myleafpic_dev.views.themeable;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.Themed;

/**
 * Created by PC-LJL on 2017/11/3.
 */

public class ThemedSettingsCategory extends AppCompatTextView implements Themed {

    public ThemedSettingsCategory(Context context) {
        this(context,null);
    }

    public ThemedSettingsCategory(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ThemedSettingsCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        themeHelper.setTextViewColor(this,
                themeHelper.getAccentColor());
    }
}
