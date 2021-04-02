package com.example.bashapabokoi;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.bashapabokoi.Helper.LocaleHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.paperdb.Paper;

public class ReportABugActivity extends AppCompatActivity {

    WebView web;
    TextView title;
    FloatingActionButton returnFromReport;

    private static final String url = "https://docs.google.com/forms/d/e/1FAIpQLSeQKxF146Z0-N-HchR8AF3FxJvmGlVCcYRz4mY4knJhSBKVjg/viewform";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            setTheme(R.style.Theme_BashaPaboKoi_Dark);
        } else{

            setTheme(R.style.Theme_BashaPaboKoi);
        }

        setContentView(R.layout.activity_report_a_bug);

        Paper.init(this);

        String language = Paper.book().read("language");
        if(language == null){

            Paper.book().write("language", "en");
        }

        title = findViewById(R.id.report_a_bug_title);

        returnFromReport = findViewById(R.id.return_from_report);

        returnFromReport.setOnClickListener(v -> finish());

        web = findViewById(R.id.found_a_bug_form);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.setWebViewClient(new Callback());
        web.loadUrl(url);

        updateView(Paper.book().read("language"));
    }

    private class Callback extends WebViewClient{

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
            pm.getPackageInfo(ReportABugActivity.url, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return false;
    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        title.setText(resources.getString(R.string.report_a_bug));
    }
}