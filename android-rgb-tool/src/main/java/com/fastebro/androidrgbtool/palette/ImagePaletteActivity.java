package com.fastebro.androidrgbtool.palette;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;

import com.fastebro.androidrgbtool.R;
import com.fastebro.androidrgbtool.commons.EventBaseActivity;
import com.fastebro.androidrgbtool.model.events.PrintPaletteEvent;
import com.fastebro.androidrgbtool.print.PrintJobDialogFragment;
import com.fastebro.androidrgbtool.print.RGBToolPrintPaletteAdapter;
import com.fastebro.androidrgbtool.utils.PaletteUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danielealtomare on 27/12/14.
 * Project: rgb-tool
 */
public class ImagePaletteActivity extends EventBaseActivity {
    public static final String EXTRA_SWATCHES = "com.fastebro.androidrgbtool.EXTRA_SWATCHES";
    public static final String FILENAME = "com.fastebro.androidrgbtool.EXTRA_FILENAME";
    @BindView(R.id.palette_grid)
    GridView paletteGrid;
    private String filename;
    private ArrayList<PaletteSwatch> swatches;
    private ShareActionProvider shareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_palette);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().getExtras().getString(FILENAME) != null) {
            filename = getIntent().getStringExtra(FILENAME);
        }

        if (getIntent().getParcelableArrayListExtra(EXTRA_SWATCHES) != null) {
            swatches = getIntent().getParcelableArrayListExtra(EXTRA_SWATCHES);
            paletteGrid.setAdapter(new ImagePaletteAdapter(this, swatches));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_palette, menu);
        // Set overflow menu icons visible
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        MenuItem item = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        updateSharedPalette();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishAfterTransition();
            return true;
        } else if (item.getItemId() == R.id.action_print) {
            showPrintPaletteDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateSharedPalette() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, PaletteUtils.getPaletteMessage(filename, swatches));
        shareIntent.setType("text/plain");
        setShareIntent(shareIntent);
    }

    private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void showPrintPaletteDialog() {
        DialogFragment dialog = PrintJobDialogFragment.newInstance(PrintJobDialogFragment.PRINT_PALETTE_JOB);
        dialog.show(getSupportFragmentManager(), null);
    }

    @Subscribe
    public void onPrintPaletteEvent(PrintPaletteEvent event) {
        printColor(event.getMessage());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void printColor(String message) {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Set job name, which will be displayed in the print queue
        String jobName = getString(R.string.app_name) + "_Color_Palette";

        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document
        printManager.print(jobName,
                new RGBToolPrintPaletteAdapter(
                        this,
                        message,
                        filename,
                        swatches),
                null);
    }
}
