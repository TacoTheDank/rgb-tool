package com.fastebro.androidrgbtool.utils;

import android.content.Context;

import com.fastebro.androidrgbtool.R;
import com.fastebro.androidrgbtool.model.PaletteSwatch;

import java.util.ArrayList;

/**
 * Created by danielealtomare on 28/12/14.
 * Project: rgb-tool
 */
public class UPalette {
    public static String getSwatchDescription(Context context, PaletteSwatch.SwatchType type) {
        switch (type) {
            case VIBRANT:
                return context.getString(R.string.swatch_vibrant);
            case LIGHT_VIBRANT:
                return context.getString(R.string.swatch_light_vibrant);
            case DARK_VIBRANT:
                return context.getString(R.string.swatch_dark_vibrant);
            case MUTED:
                return context.getString(R.string.swatch_muted);
            case LIGHT_MUTED:
                return context.getString(R.string.swatch_light_muted);
            case DARK_MUTED:
                return context.getString(R.string.swatch_dark_muted);
            default:
                return "";
        }
    }

    public static String getPaletteMessage(Context context, String filename, ArrayList<PaletteSwatch> swatches) {
        StringBuilder message = new StringBuilder();

        message.append("RGB Tool - Image Palette");
        message.append(System.getProperty("line.separator"));

        if(!"".equals(filename)) {
            message.append("File: " + filename);
            message.append(System.getProperty("line.separator"));
        }

        for(PaletteSwatch swatch : swatches) {
            message.append(UPalette.getSwatchDescription(context, swatch.getType()));
            message.append(": ");
            message.append("HEX - ");
            message.append("#" + Integer.toHexString(swatch.getRgb()).toUpperCase());
            message.append(System.getProperty("line.separator"));
        }

        return message.toString();
    }
}
