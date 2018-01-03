package indi.aljet.myleafpic_dev.activitys;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.view.IconicsImageView;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.ThemedActivity;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.util.Security;

/**
 * Created by PC-LJL on 2017/11/23.
 */

public class SecurityActivity extends ThemedActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.root)
    LinearLayout llroot;

    @BindView(R.id.active_security_switch)
    SwitchCompat swActiveSecurity;
    @BindView(R.id.security_body_apply_delete_switch)
    SwitchCompat swApplySecurityDelete;
    @BindView(R.id.security_body_apply_hidden_switch)
    SwitchCompat swApplySecurityHidden;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        ButterKnife.bind(this);

        initUi();

        swActiveSecurity.setChecked(
                Security.isPasswordSet(getApplicationContext()));
        swActiveSecurity.setClickable(false);

        swApplySecurityHidden.setChecked(Hawk
        .get("password_on_hidden",false));
        swApplySecurityHidden.setClickable(false);
        swApplySecurityDelete.setChecked(Hawk
        .get("password_on_delete",false));
        swApplySecurityDelete.setChecked(false);

    }


    @OnClick(R.id.ll_active_security)
    public void activeSecurityOnClick(View view){
        swActiveSecurity.setChecked(!swActiveSecurity
        .isChecked());
        setSwitchColor(getAccentColor(),swActiveSecurity);
        if(swActiveSecurity.isChecked()){
            setPasswordDialog();
        }else{
            Security.clearPassword(getApplicationContext());
        }
        toggleEnabledChild(swActiveSecurity
        .isChecked());
    }

    @OnClick(R.id.ll_security_body_apply_hidden)
    public void BodyHiddenOnClick(View view){
        swApplySecurityHidden.setChecked(!swApplySecurityHidden
        .isChecked());
        Security.setPasswordOnHidden(getApplicationContext(),
                swApplySecurityHidden.isChecked());
        setSwitchColor(getAccentColor(),
                swApplySecurityHidden);

    }

    @OnClick(R.id.ll_security_body_apply_delete)
    public void BodyApplyDeleteOnClick(View view){
        swApplySecurityDelete.setChecked(!swApplySecurityDelete
        .isChecked());
        Security.setPasswordOnDelete(getApplicationContext()
        ,swApplySecurityDelete.isChecked());
        setSwitchColor(getAccentColor(),swApplySecurityDelete);
    }


    private void initUi(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getToolbarIcon(GoogleMaterial
        .Icon.gmd_arrow_back));
        toolbar.setNavigationOnClickListener( view -> {
            onBackPressed();
        });
    }

    private void setPasswordDialog(){
        final AlertDialog.Builder passwordDialog =
                new AlertDialog.Builder(SecurityActivity
                .this,getDialogStyle());
        final View passwordDialogLayout =
                getLayoutInflater().inflate(R.layout
                .dialog_set_password,null);
        final TextView passwordDialogTitle = (TextView)
                passwordDialogLayout.findViewById(R.id.
                password_dialog_title);
        final CardView passwordDialogCard = (CardView) passwordDialogLayout
                .findViewById(R.id.password_dialog_card);
        final EditText editTextPassword = (EditText) passwordDialogLayout.
                findViewById(R.id.password_edittxt);
        final EditText editTextConfirmPassword = (EditText)
                passwordDialogLayout.findViewById
                        (R.id.confirm_password_edittxt);
        passwordDialogTitle.setBackgroundColor(getPrimaryColor());
        passwordDialogCard.setBackgroundColor(getCardBackgroundColor());

        editTextPassword.getBackground().mutate()
                .setColorFilter(getTextColor(), PorterDuff
                .Mode.SRC_ATOP);

        editTextPassword.setTextColor(getTextColor());
        editTextPassword.setHintTextColor(getSubTextColor());
        ThemeHelper.setCursorColor(editTextConfirmPassword
        ,getTextColor());
        editTextConfirmPassword.getBackground()
                .mutate().setColorFilter(getTextColor(),PorterDuff
        .Mode.SRC_ATOP);
        editTextConfirmPassword.setTextColor(getSubTextColor());
        ThemeHelper.setCursorColor(editTextConfirmPassword,
                getTextColor());
        passwordDialog.setView(passwordDialogLayout);

        AlertDialog dialog = passwordDialog.create();
        dialog.setCancelable(false);

        dialog.setButton(DialogInterface
        .BUTTON_POSITIVE,getString(R.string
        .cancel).toUpperCase(),(dialogInterface,which) -> {
            swActiveSecurity.setChecked(false);
            setSwitchColor(getAccentColor(),
                    swActiveSecurity);
            toggleEnabledChild(swActiveSecurity
            .isChecked());
            Security.clearPassword(getApplicationContext());
        });
        dialog.setButton(DialogInterface
        .BUTTON_NEGATIVE,getString(R.string.ok_action)
        .toUpperCase(),(dialog2,which) ->{
            if(editTextConfirmPassword.length() > 3){
                if(editTextPassword.getText()
                        .toString().equals(editTextConfirmPassword
                        .getText().toString())){
                    if(Security.setPassword(getApplicationContext()
                    ,editTextPassword.getText().toString())){
                        swActiveSecurity.setChecked(true);
                        toggleEnabledChild(true);
                        Toast.makeText(getApplicationContext(),
                                R.string.remember_password_message,
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SecurityActivity.this,
                                R.string.error_contact_developer,
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            R.string.password_dont_match, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),
                        R.string.error_password_length,
                        Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void toggleEnabledChild(boolean enable){
        findViewById(R.id.ll_security_body_apply_hidden)
                .setEnabled(enable);
        findViewById(R.id.ll_security_body_apply_delete)
                .setClickable(enable);
        if(enable){
            ((IconicsImageView)findViewById(
                    R.id.security_body_apply_hidden_icon))
                    .setColor(getIconColor());
            ((TextView) findViewById
                    (R.id.security_body_apply_hidden_title)).
                    setTextColor(getTextColor());
            ((IconicsImageView) findViewById
                    (R.id.security_body_apply_delete_icon)).
                    setColor(getIconColor());
            ((TextView) findViewById
                    (R.id.security_body_apply_delete_title))
                    .setTextColor(getTextColor());
        }else{
            ((IconicsImageView) findViewById
                    (R.id.security_body_apply_hidden_icon)).
                    setColor(getSubTextColor());
            ((TextView) findViewById
                    (R.id.security_body_apply_hidden_title)).
                    setTextColor(getSubTextColor());
            ((IconicsImageView) findViewById
                    (R.id.security_body_apply_delete_icon)).
                    setColor(getSubTextColor());
            ((TextView) findViewById
                    (R.id.security_body_apply_delete_title)).
                    setTextColor(getSubTextColor());
        }
    }


    @Override
    public void updateUiElements() {
        super.updateUiElements();

        setRecentApp(getString(R.string.security));

        toolbar.setBackgroundColor(getPrimaryColor());

        setSwitchColor(getAccentColor(),swActiveSecurity
        ,swApplySecurityHidden,swApplySecurityDelete);
        toggleEnabledChild(swActiveSecurity
        .isChecked());

        setStatusBarColor();
        setNavBarColor();

        llroot.setBackgroundColor(getBackgroundColor());
        ((CardView)findViewById(R.id.security_dialog_card))
                .setCardBackgroundColor(getCardBackgroundColor());

        int color = getIconColor();
        ((IconicsImageView)findViewById(R.id.active_security_icon))
                .setColor(color);
        ((IconicsImageView) findViewById(R.id.security_body_apply_hidden_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.security_body_apply_delete_icon)).setColor(color);

        /** TEXTVIEWS **/
        color = getTextColor();
        ((TextView) findViewById(R.id.active_security_item_title)).setTextColor(color);
        ((TextView) findViewById(R.id.security_body_apply_on)).setTextColor(color);
        ((TextView) findViewById(R.id.security_body_apply_hidden_title)).setTextColor(color);
        ((TextView) findViewById(R.id.security_body_apply_delete_title)).setTextColor(color);
    }
}
