package indi.aljet.myleafpic_dev.data.sort;

/**
 * Created by PC-LJL on 2017/11/14.
 */

public enum  SortingOrder {
    ASCENDING (1) ,DESCENDING (0);

    int value;
    SortingOrder(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isAscending(){
        return value == ASCENDING.getValue();
    }

    public static SortingOrder fromValue(boolean value) {
        return value ? ASCENDING : DESCENDING;
    }

    public static SortingOrder fromValue(int value) {
        return value == 0 ? DESCENDING : ASCENDING;
    }

}
