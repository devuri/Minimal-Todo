package com.sumpfhupe.apps.minitodo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView mVersionTextView;
    private String appVersion = null;
    private Toolbar toolbar;
    private TextView contactMe;
    String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme = getSharedPreferences(MainActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(MainActivity.THEME_SAVED, MainActivity.LIGHTTHEME);
        if(theme.equals(MainActivity.DARKTHEME)){
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        else{
            setTheme(R.style.CustomStyle_LightTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        final Drawable backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        if(backArrow!=null){
            backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        try{
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = info.versionName;
        }
        catch (NameNotFoundException e){
            Log.e("Mini", "Package name not found", e);
            appVersion = "0";
        }


        mVersionTextView = (TextView)findViewById(R.id.aboutVersionTextView);
        mVersionTextView.setText(String.format(getResources().getString(R.string.app_version), appVersion));
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(this)!=null){
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
