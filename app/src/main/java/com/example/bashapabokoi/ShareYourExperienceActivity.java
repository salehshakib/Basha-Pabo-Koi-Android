package com.example.bashapabokoi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.bashapabokoi.Helper.LocaleHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.paperdb.Paper;

public class ShareYourExperienceActivity extends AppCompatActivity {

    WebView web;
    TextView title;
    FloatingActionButton returnFromExp;

    private static final String url = "https://docs.google.com/forms/d/e/1FAIpQLSd74TzUTwH-0epbKBQgvZtz1u3nT65l6P2wpBnB-6JQVtOlpQ/viewform";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            setTheme(R.style.Theme_BashaPaboKoi_Dark);
        } else{

            setTheme(R.style.Theme_BashaPaboKoi);
        }

        setContentView(R.layout.activity_share_your_experience);

        Paper.init(this);

        String language = Paper.book().read("language");
        if(language == null){

            Paper.book().write("language", "en");
        }

        title = findViewById(R.id.share_your_exp_title);

        returnFromExp = findViewById(R.id.return_from_exp);

        returnFromExp.setOnClickListener(v -> finish());

        web = findViewById(R.id.share_your_exp_form);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.setWebViewClient(new Callback());
        web.loadUrl(url);

        updateView(Paper.book().read("language"));
    }

    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            if( URLUtil.isNetworkUrl(url) ) {
                return false;
            }
            if (appInstalledOrNot()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }

            return true;
        }
    }

    private boolean appInstalledOrNot() {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(ShareYourExperienceActivity.url, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return false;
    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        title.setText(resources.getString(R.string.share_your_experience));
    }
}