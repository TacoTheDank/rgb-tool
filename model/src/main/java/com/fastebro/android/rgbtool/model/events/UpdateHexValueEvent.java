package com.fastebro.android.rgbtool.model.events;

/**
 * Created by danielealtomare on 26/12/14.
 */
public class UpdateHexValueEvent {
    public final String hexValue;

    public UpdateHexValueEvent(String hexValue) {
        this.hexValue = hexValue;
    }
}