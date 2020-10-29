package com.fastebro.androidrgbtool.utils;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * Created by danielealtomare on 05/03/14.
 * Project: rgb-tool
 */
abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(@NonNull String albumName);
}
