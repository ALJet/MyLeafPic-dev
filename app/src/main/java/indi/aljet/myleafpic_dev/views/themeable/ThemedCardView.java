package indi.aljet.myleafpic_dev.views.themeable;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.Themed;

/**
 * Created by PC-LJL on 2017/11/3.
 */

public class ThemedCardView extends CardView implements Themed {

    public ThemedCardView(Context context) {
        this(context,null);
    }

    public ThemedCardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ThemedCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        setCardBackgroundColor(themeHelper
        .getCardBackgroundColor());
    }
}
