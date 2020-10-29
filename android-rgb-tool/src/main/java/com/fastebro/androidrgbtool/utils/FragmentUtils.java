package com.fastebro.androidrgbtool.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Created by Snow Volf on 28.07.2017, 16:11
 */

public class FragmentUtils {
    public static void iterate(AppCompatActivity activity, int resId, @NonNull Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(resId, fragment)
                .commit();
    }
}
