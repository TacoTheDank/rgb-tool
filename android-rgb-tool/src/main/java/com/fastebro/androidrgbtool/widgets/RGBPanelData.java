package com.fastebro.androidrgbtool.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fastebro.androidrgbtool.R;
import com.fastebro.androidrgbtool.utils.ClipboardUtils;
import com.fastebro.androidrgbtool.utils.ColorUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RGBPanelData extends LinearLayout {

    @BindView(R.id.rgb_value)
    TextView mRGBValue;
    @BindView(R.id.hsb_value)
    TextView mHSBValue;
    @BindView(R.id.hex_value)
    TextView mHEXValue;
    @BindView(R.id.btn_dismiss_panel)
    ImageButton mDismissPanelButton;
    @BindView(R.id.color_view)
    View mColorView;

    private int alpha;
    private int red;
    private int green;
    private int blue;
    private float[] hsb;

    public RGBPanelData(@NonNull Context context) {
        super(context);
        setupPanel(context);
    }

    public RGBPanelData(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        setupPanel(context);
    }

    public RGBPanelData(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupPanel(context);
    }

    private void setupPanel(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.rgb_data_panel_small, this);
        ButterKnife.bind(this);

        mDismissPanelButton.setOnClickListener(v -> setVisibility(GONE));

        mRGBValue.setOnLongClickListener(new ClipboardLongClickListener(context,
                context.getString(R.string.app_name)));
        mHSBValue.setOnLongClickListener(new ClipboardLongClickListener(context,
                context.getString(R.string.app_name)));
        mHEXValue.setOnLongClickListener(new ClipboardLongClickListener(context,
                context.getString(R.string.app_name)));
    }

    public void updateData(int touchedRGB) {
        alpha = (touchedRGB >> 24) & 0xFF;
        red = (touchedRGB >> 16) & 0xFF;
        green = (touchedRGB >> 8) & 0xFF;
        blue = touchedRGB & 0xFF;
        hsb = ColorUtils.RGBToHSB(red, green, blue);

        setRGBValue();
        setHSBValue();
        setHEXValue(touchedRGB);
        setColorView(touchedRGB);
    }

    private void setRGBValue() {
        if (mRGBValue != null) {
            mRGBValue.setText("(" + alpha + ", " + red + ", " + green + ", " + blue + ")");
        }
    }

    private void setHSBValue() {
        if (mHSBValue != null) {
            mHSBValue.setText("");
            mHSBValue.append("(" + String.format(Locale.ENGLISH, "%.0f", hsb[0]));
            mHSBValue.append(", " + String.format(Locale.ENGLISH, "%.0f%%", (hsb[1] * 100.0f)));
            mHSBValue.append(", " + String.format(Locale.ENGLISH, "%.0f%%", (hsb[2] * 100.0f)) + ")");
        }
    }

    private void setHEXValue(int touchedRGB) {
        if (mHEXValue != null) {
            mHEXValue.setText(("#" + Integer.toHexString(touchedRGB)).toUpperCase());
        }
    }

    private void setColorView(int color) {
        mColorView.setBackgroundColor(color);
    }

    private class ClipboardLongClickListener implements OnLongClickListener {
        final Context context;
        final CharSequence label;

        public ClipboardLongClickListener(Context context, CharSequence label) {
            this.context = context;
            this.label = label;
        }

        @Override
        public boolean onLongClick(View v) {
            CharSequence text = ((TextView) v).getText();
            ClipboardUtils.copyToClipboard(text.toString());
            Snackbar.make(v, text + " " + context.getString(R.string.clipboard), Snackbar.LENGTH_SHORT).show();
            return true;
        }
    }
}
