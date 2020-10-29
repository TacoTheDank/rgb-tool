package com.fastebro.androidrgbtool.rgb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import com.fastebro.androidrgbtool.R;
import com.fastebro.androidrgbtool.model.events.RGBAInsertionEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by danielealtomare on 26/03/14.
 * Project: rgb-tool
 */
public class RgbaInsertionFragment extends BottomSheetDialogFragment {
    private static final String ARG_RGBA_VALUES = "RGBA_VALUES";

    @BindView(R.id.numberPickerR)
    NumberPicker pickerR;
    @BindView(R.id.numberPickerG)
    NumberPicker pickerG;
    @BindView(R.id.numberPickerB)
    NumberPicker pickerB;
    @BindView(R.id.numberPickerA)
    NumberPicker pickerA;
    private Unbinder unbinder;

    private short[] rgbaValues;

    public RgbaInsertionFragment() {
    }

    public static RgbaInsertionFragment newInstance(short[] rgbaValues) {
        RgbaInsertionFragment fragment = new RgbaInsertionFragment();
        Bundle args = new Bundle();
        args.putShortArray(ARG_RGBA_VALUES, rgbaValues);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rgba_insertion, null);
        unbinder = ButterKnife.bind(this, view);

        pickerR.setMinValue(0);
        pickerR.setMaxValue(255);
        pickerG.setMinValue(0);
        pickerG.setMaxValue(255);
        pickerB.setMinValue(0);
        pickerB.setMaxValue(255);
        pickerA.setMinValue(0);
        pickerA.setMaxValue(255);

        if (getArguments() != null) {
            rgbaValues = getArguments().getShortArray(ARG_RGBA_VALUES);
        }

        if (rgbaValues != null) {
            pickerR.setValue(rgbaValues[0]);
            pickerG.setValue(rgbaValues[1]);
            pickerB.setValue(rgbaValues[2]);
            pickerA.setValue(rgbaValues[3]);
        } else {
            pickerR.setValue(0);
            pickerG.setValue(0);
            pickerB.setValue(0);
            pickerA.setValue(0);
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle(getString(R.string.rgba_insertion_dialog_title))
                .setPositiveButton(getString(R.string.action_common_set),
                        (dialog, id) -> {
                            pickerR.clearFocus();
                            pickerG.clearFocus();
                            pickerB.clearFocus();
                            pickerA.clearFocus();

                            short[] rgbaValues1 = new short[]{
                                    (short) pickerR.getValue(),
                                    (short) pickerG.getValue(),
                                    (short) pickerB.getValue(),
                                    (short) pickerA.getValue()
                            };

                            EventBus.getDefault().post(new RGBAInsertionEvent(rgbaValues1));
                            RgbaInsertionFragment.this.getDialog().cancel();
                        }
                );

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
