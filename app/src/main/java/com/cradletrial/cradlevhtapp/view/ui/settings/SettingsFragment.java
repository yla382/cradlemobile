package com.cradletrial.cradlevhtapp.view.ui.settings;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.dagger.MyApp;
import com.cradletrial.cradlevhtapp.model.Settings;
import com.cradletrial.cradlevhtapp.utilitiles.Util;
import com.cradletrial.cradlevhtapp.view.ReadingActivity;

import java.util.Arrays;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener
{
    // Data Model
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    Settings settings;

//    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        // inject:
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);

        // initialize
        setPreferencesFromResource(R.xml.preferences, rootKey);

        setupHealthCentres();
    }

    private void setupHealthCentres() {
        Preference hcPref = findPreference("setting_health_centres");

        if (hcPref != null) {
            hcPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Log.d("TAG", "You clicked " + preference.getKey());
                    Intent intent = SettingNamedPairsActivity.makeLaunchIntent(getContext(),
                            SettingNamedPairsActivity.SelectPair.SELECT_PAIR_HEALTH_CENTRES);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }


    // source: https://stackoverflow.com/a/44110510/3475174
    @Override
    public void onResume() {
        super.onResume();

        // register as observer
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        initSummary(getPreferenceScreen());
    }

    @Override
    public void onPause() {
        // unregister as observer
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }


    // observer callback
    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefsArg, String key) {
        Preference pref = getPreferenceScreen().findPreference(key);
        setSummary(pref);
        settings.loadFromSharedPrefs();

    }


    // update whole screen
    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            setSummary(p);
        }
    }


    /**
     * Summaries
     */
    private void setSummary(Preference pref) {
        // special case prefs

        // PIN
        if (pref.getKey().equals("setting_pin")) {
            String pin = ((EditTextPreference) pref).getText();
            String stars = pin.length() > 0 ?
                    Util.makeRepeatString("*", pin.length())
                    : "Not set";
            pref.setSummary(stars);
        }

        // health centres
        else if (pref.getKey().equals("setting_health_centres")) {
            String summary = settings.getHealthCentres().size() + " configured health centres";
            pref.setSummary(summary);
        }

        // generic
        else if (pref instanceof EditTextPreference) {
            updateSummary((EditTextPreference) pref);
        } else if (pref instanceof ListPreference) {
            updateSummary((ListPreference) pref);
        } else if (pref instanceof MultiSelectListPreference) {
            updateSummary((MultiSelectListPreference) pref);
        }
    }

    private void updateSummary(MultiSelectListPreference pref) {
        pref.setSummary(Arrays.toString(pref.getValues().toArray()));
    }

    private void updateSummary(ListPreference pref) {
        pref.setSummary(pref.getValue());
    }

    private void updateSummary(EditTextPreference preference) {
        preference.setSummary(preference.getText());
    }
}