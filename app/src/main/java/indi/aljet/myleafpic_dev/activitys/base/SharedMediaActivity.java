package indi.aljet.myleafpic_dev.activitys.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import horaapps.org.liz.ThemedActivity;
import indi.aljet.myleafpic_dev.App;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.data.Album;
import indi.aljet.myleafpic_dev.data.HandlingAlbums;
import indi.aljet.myleafpic_dev.data.StorageHelper;
import indi.aljet.myleafpic_dev.util.AlertDialogsHelper;

/**
 * Created by PC-LJL on 2017/11/15.
 */

public abstract class SharedMediaActivity extends ThemedActivity {
    private int REQUEST_CODE_SD_CARD_PERMISSIONS = 42;


    @Deprecated
    public HandlingAlbums getAlbums() {
        return ((App) getApplicationContext()).
                getAlbums();
    }

    @Deprecated
    public Album getAlbum() {
        return ((App) getApplicationContext()).getAlbum();
    }


    public void requestSdCardPermissions(){
        AlertDialog textDialog = AlertDialogsHelper
                .getTextDialog(this, R.string
                .sd_card_write_permission_title,R.string
                .sd_card_permissions_message);
        textDialog.setButton(DialogInterface
                        .BUTTON_POSITIVE, getString(R.string
                        .ok_action).toUpperCase(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(Build.VERSION.SDK_INT >= Build
                                .VERSION_CODES.LOLLIPOP){
                            startActivityForResult(new Intent(Intent
                            .ACTION_OPEN_DOCUMENT_TREE),
                                    REQUEST_CODE_SD_CARD_PERMISSIONS);
                        }
                    }
                });
        textDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_SD_CARD_PERMISSIONS){
                Uri treeUri = data.getData();
                StorageHelper.saveSdCardInfo(getApplicationContext(),
                        treeUri);
                getContentResolver()
                        .takePersistableUriPermission(treeUri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                Toast.makeText(this,R.string.got_permission_wr_sdcard,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
