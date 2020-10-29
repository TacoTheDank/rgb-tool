package com.fastebro.androidrgbtool.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.fastebro.androidrgbtool.R;

public class AboutFragment extends PreferenceFragmentCompat {
    public static final String FRAGMENT_TAG = "fragment_about";

    private OnPreferenceSelectedListener onPreferenceSelectedListener;

    public AboutFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onPreferenceSelectedListener = (OnPreferenceSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnPreferenceSelectedListener");
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getIntent() != null && preference.getIntent().getData() != null) {
            if (preference.getIntent().getAction().equals(Intent.ACTION_SENDTO)) {
                onPreferenceSelectedListener.onPreferenceSendEmailSelected(new String[]{preference.getIntent()
                        .getData().toString()}, getString(R.string.app_name));
            } else {
                onPreferenceSelectedListener.onPreferenceWithUriSelected(preference.getIntent().getData());
            }
            return true;
        }

        return super.onPreferenceTreeClick(preference);
    }

    public interface OnPreferenceSelectedListener {
        void onPreferenceWithUriSelected(Uri uri);

        void onPreferenceSendEmailSelected(String[] addresses, String subject);
    }
}
