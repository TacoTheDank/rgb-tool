package com.fastebro.androidrgbtool.utils;

import android.graphics.Color;

import androidx.annotation.IntRange;

import com.fastebro.androidrgbtool.R;
import com.fastebro.androidrgbtool.RGBToolApplication;

import java.util.Locale;

public final class ColorUtils {

    private ColorUtils() {
    }

    public static String getRGB(float n) {
        return String.format(Locale.ENGLISH, "%.0f", n).replaceAll("\\.0*$", "");
    }

    public static String RGBToHex(int n) {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(n));
        // Add '0' character at first index if the string length < 2.
        if (sb.length() < 2) {
            sb.insert(0, '0');
        }

        return sb.toString().toUpperCase();
    }

    public static float[] RGBToHSB(@IntRange(from = 0, to = 255) int r,
                                   @IntRange(from = 0, to = 255) int g,
                                   @IntRange(from = 0, to = 255) int b) {
        float[] hsb = new float[3];
        Color.RGBToHSV(r, g, b, hsb);
        return hsb;
    }

    public static float[] RGBToHSL(@IntRange(from = 0, to = 255) int red,
                                   @IntRange(from = 0, to = 255) int green,
                                   @IntRange(from = 0, to = 255) int blue,
                                   float[] hsl) {
        //  Get RGB values in the range 0 - 1
        float r = red / 255f;
        float g = green / 255f;
        float b = blue / 255f;

        //	Minimum and Maximum RGB values are used in the HSL calculations
        float min = Math.min(r, Math.min(g, b));
        float max = Math.max(r, Math.max(g, b));

        //  Calculate the Hue
        float h = 0;

        if (max == min) {
            h = 0;
        } else if (max == r) {
            h = (((g - b) / (max - min) / 6f) + 1) % 1;
        } else if (max == g) {
            h = ((b - r) / (max - min) / 6f) + 1f / 3f;
        } else if (max == b) {
            h = ((r - g) / (max - min) / 6f) + 2f / 3f;
        }

        //  Calculate the Luminance
        float l = (max + min) / 2;

        //  Calculate the Saturation
        float s;

        if (max == min) {
            s = 0;
        } else if (l <= .5f) {
            s = (max - min) / (max + min);
        } else {
            s = (max - min) / (2 - max - min);
        }

        if (hsl == null) {
            hsl = new float[3];
        }

        hsl[0] = h;
        hsl[1] = s;
        hsl[2] = l;

        return hsl;
    }

    public static String getColorMessage(@IntRange(from = 0, to = 255) int rgbRColor,
                                         @IntRange(from = 0, to = 255) int rgbGColor,
                                         @IntRange(from = 0, to = 255) int rgbBColor,
                                         @IntRange(from = 0, to = 255) int rgbOpacity) {
        StringBuilder message = new StringBuilder();

        message.append(RGBToolApplication.getCtx().getString(R.string.app_name));
        message.append(System.getProperty("line.separator"));

        message.append("RGB - ");
        message.append("R: ");
        message.append(ColorUtils.getRGB(rgbRColor));
        message.append("  G: ");
        message.append(ColorUtils.getRGB(rgbGColor));
        message.append("  B: ");
        message.append(ColorUtils.getRGB(rgbBColor));
        message.append(System.getProperty("line.separator"));

        message.append("Opacity: ");
        message.append(ColorUtils.getRGB(rgbOpacity));
        message.append(System.getProperty("line.separator"));

        message.append("HSB - ");
        float[] hsb = ColorUtils.RGBToHSB(rgbRColor, rgbGColor, rgbBColor);
        message.append("H: ");
        message.append(String.format(Locale.ENGLISH, "%.0f", hsb[0]));
        message.append("  S: ");
        message.append(String.format(Locale.ENGLISH, "%.0f%%", (hsb[1] * 100.0f)));
        message.append("  B: ");
        message.append(String.format(Locale.ENGLISH, "%.0f%%", (hsb[2] * 100.0f)));
        message.append(System.getProperty("line.separator"));

        message.append("HEX - ");
        message.append(String.format("#%s%s%s%s",
                ColorUtils.RGBToHex(rgbOpacity),
                ColorUtils.RGBToHex(rgbRColor),
                ColorUtils.RGBToHex(rgbGColor),
                ColorUtils.RGBToHex(rgbBColor)));
        message.append(System.getProperty("line.separator"));

        return message.toString();
    }

    public static String getComplementaryColorMessage(short[] argbColorValues,
                                                      short[] argbComplementaryColorValues,
                                                      short[] argbContrastColorValues) {
        // Opacity is fixed as FF.

        return RGBToolApplication.getCtx().getString(R.string.app_name) +
                System.getProperty("line.separator") +
                "Color - " +
                String.format("#%s%s%s%s",
                        ColorUtils.RGBToHex(argbColorValues[0]),
                        ColorUtils.RGBToHex(argbColorValues[1]),
                        ColorUtils.RGBToHex(argbColorValues[2]),
                        ColorUtils.RGBToHex(argbColorValues[3])) +
                System.getProperty("line.separator") +
                "Complementary - " +
                String.format("#FF%s%s%s",
                        ColorUtils.RGBToHex(argbComplementaryColorValues[1]),
                        ColorUtils.RGBToHex(argbComplementaryColorValues[2]),
                        ColorUtils.RGBToHex(argbComplementaryColorValues[3])) +
                System.getProperty("line.separator") +
                "Contrast - " +
                String.format("#FF%s%s%s",
                        ColorUtils.RGBToHex(argbContrastColorValues[1]),
                        ColorUtils.RGBToHex(argbContrastColorValues[2]),
                        ColorUtils.RGBToHex(argbContrastColorValues[3])) +
                System.getProperty("line.separator");
    }

    public static int[] hexToRGB(String hexValue) {
        int[] rgb = new int[3];

        if (!"".equals(hexValue)) {
            int rgbValue = Color.parseColor("#" + hexValue);
            rgb[0] = (rgbValue & 0xFF0000) >> 16;
            rgb[1] = (rgbValue & 0xFF00) >> 8;
            rgb[2] = (rgbValue & 0xFF);
        }

        return rgb;
    }

    public static int[] hexToARGB(String hexValue) {
        int[] argb = new int[4];

        if (!"".equals(hexValue) && hexValue.length() == 8) {
            argb[0] = Integer.parseInt(hexValue.substring(0, 2), 16);
            argb[1] = Integer.parseInt(hexValue.substring(2, 4), 16);
            argb[2] = Integer.parseInt(hexValue.substring(4, 6), 16);
            argb[3] = Integer.parseInt(hexValue.substring(6), 16);
        }

        return argb;
    }

    public static int getComplementaryColor(@IntRange(from = 0, to = 255) int rgbRColor,
                                            @IntRange(from = 0, to = 255) int rgbGColor,
                                            @IntRange(from = 0, to = 255) int rgbBColor) {
        float[] hsv = new float[3];
        Color.RGBToHSV(rgbRColor, rgbGColor, rgbBColor, hsv);
        hsv[0] = (hsv[0] + 180) % 360;

        return Color.HSVToColor(hsv);
    }

    public static int getContrastColor(@IntRange(from = 0, to = 255) int rgbRColor,
                                       @IntRange(from = 0, to = 255) int rgbGColor,
                                       @IntRange(from = 0, to = 255) int rgbBColor) {
        float[] hsv = new float[3];
        Color.RGBToHSV(rgbRColor, rgbGColor, rgbBColor, hsv);
        if (hsv[2] < 0.5) {
            hsv[2] = 0.7f;
        } else {
            hsv[2] = 0.3f;
        }
        hsv[1] = hsv[1] * 0.2f;

        return Color.HSVToColor(hsv);
    }
}
