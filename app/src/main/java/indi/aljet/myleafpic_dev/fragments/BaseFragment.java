package indi.aljet.myleafpic_dev.fragments;

import horaapps.org.liz.Themed;
import horaapps.org.liz.ThemedFragment;

/**
 * Created by PC-LJL on 2017/11/20.
 */

public abstract class BaseFragment extends ThemedFragment implements
        IFragment, Themed{

    public boolean onBackPressed(){
        if(editMode()){
            clearSelected();
            return true;
        }
        return false;
    }
}
