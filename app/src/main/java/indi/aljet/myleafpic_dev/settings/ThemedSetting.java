package indi.aljet.myleafpic_dev.settings;

import horaapps.org.liz.ThemedActivity;

/**
 * Created by PC-LJL on 2017/11/16.
 */

public class ThemedSetting {
    private ThemedActivity activity;

    public ThemedSetting() {
    }

    public ThemedSetting(ThemedActivity activity) {
        this.activity = activity;
    }

    public ThemedActivity getActivity() {
        return activity;
    }
}
