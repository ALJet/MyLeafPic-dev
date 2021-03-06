package indi.aljet.myleafpic_dev.util;

import com.drew.lang.GeoLocation;

import java.util.Locale;

import indi.aljet.myleafpic_dev.SecretConstants;

/**
 * Created by PC-LJL on 2017/11/13.
 */

public enum StaticMapProvider {
    GOOGLE_MAPS(0),MAP_BOX(1),MAP_BOX_DARK(2),
    MAP_BOX_LIGHT(3),TYLER(4);

    int value;

    StaticMapProvider(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }


    public String getUrl(GeoLocation location){
        if(value >= 1 && value <= 3){
            return String.format(Locale.US,getUrl(value),
                    location.getLatitude(),location
            .getLatitude());
        }
        return String.format(Locale.US,getUrl(value),
                location.getLatitude(),location.getLongitude());
    }


    private String getUrl(int value){
        switch (value){
            case 0:
            default :
                return("http://maps.google.com/maps/api/staticmap?center=%f,%f&zoom=15&size=500x300&scale=2&sensor=false");
            case 1: return "https://api.mapbox.com/v4/mapbox.streets/%f,%f,15/500x300.jpg?access_token=" + SecretConstants.MAP_BOX_TOKEN;
            case 2: return "https://api.mapbox.com/v4/mapbox.dark/%f,%f,15/500x300.jpg?access_token="+ SecretConstants.MAP_BOX_TOKEN;
            case 3: return "https://api.mapbox.com/v4/mapbox.light/%f,%f,15/500x300.jpg?access_token="+ SecretConstants.MAP_BOX_TOKEN;
            case 4: return "https://tyler-demo.herokuapp.com/?greyscale=false&lat=%f&lon=%f&zoom=15&width=500&height=300&tile_url=http://[abcd].tile.stamen.com/watercolor/{zoom}/{x}/{y}.jpg";

        }
    }

    public static StaticMapProvider fromValue(int value){
        switch (value){
            case 0: default: return GOOGLE_MAPS;
            case 1: return MAP_BOX;
            case 2: return MAP_BOX_DARK;
            case 3: return MAP_BOX_LIGHT;
            case 4: return TYLER;
        }
    }

}
