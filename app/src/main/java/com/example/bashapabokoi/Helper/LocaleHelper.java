package com.example.bashapabokoi.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import androidx.work.impl.model.Preference;

import java.util.Locale;



public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context onAttach(Context context){

        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }

    public static Context onAttach(Context context, String defaultLanguage){

        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    public static Context setLocale(Context context, String lang){

        persist(context, lang);
        return updateResources(context,lang);
    }

    private static Context updateResources(Context context, String lang){

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);

        return context.createConfigurationContext(config);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String lang){

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        config.setLayoutDirection(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        return context;
    }

    private static void persist(Context context, String lang){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, lang);
        editor.apply();
    }

    private static String getPersistedData(Context context, String language){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, language);
    }
}
