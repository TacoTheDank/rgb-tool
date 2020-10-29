package com.fastebro.androidrgbtool.colors;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.fastebro.androidrgbtool.R;
import com.fastebro.androidrgbtool.commons.EventBaseFragment;
import com.fastebro.androidrgbtool.model.events.ColorDeleteEvent;
import com.fastebro.androidrgbtool.model.events.ColorSelectEvent;
import com.fastebro.androidrgbtool.model.events.ColorShareEvent;
import com.fastebro.androidrgbtool.model.events.UpdateSaveColorUIEvent;
import com.fastebro.androidrgbtool.utils.ColorUtils;
import com.fastebro.androidrgbtool.utils.DatabaseUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by danielealtomare on 15/02/15.
 * Project: rgb-tool
 */
public class ColorListFragment extends EventBaseFragment
        implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int GET_COLORS_REQUEST_ID = 0;
    @BindView(android.R.id.list)
    ListView listView;
    @BindView(R.id.list_empty_progress)
    LinearLayout progressBar;
    @BindView(R.id.list_empty_text)
    TextView emptyListMessage;
    private Unbinder unbinder;
    private ColorListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_color_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ColorListAdapter(getActivity(),
                R.layout.color_list_row, null,
                new String[]{ColorDataContract.ColorEntry.COLUMN_COLOR_HEX},
                new int[]{R.id.hex_value}, 0);

        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        getActivity().getSupportLoaderManager().initLoader(GET_COLORS_REQUEST_ID, null, this);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        getActivity().getSupportLoaderManager().destroyLoader(GET_COLORS_REQUEST_ID);
        EventBus.getDefault().post(new UpdateSaveColorUIEvent());
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) adapter.getItem(position);

        if (cursor != null) {
            int rgbRValue = cursor.getInt(cursor.getColumnIndex(ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_R));
            int rgbGValue = cursor.getInt(cursor.getColumnIndex(ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_G));
            int rgbBValue = cursor.getInt(cursor.getColumnIndex(ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_B));
            int rgbAValue = cursor.getInt(cursor.getColumnIndex(ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_A));

            EventBus.getDefault().post(new ColorSelectEvent(rgbRValue,
                    rgbGValue, rgbBValue, rgbAValue, null));

            getActivity().finishAfterTransition();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri = RGBToolContentProvider.CONTENT_URI;

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = "((" + ColorDataContract.ColorEntry.COLUMN_COLOR_HEX + " NOTNULL))";
        return new CursorLoader(getActivity(), baseUri,
                DatabaseUtils.COLORS_SUMMARY_PROJECTION, select, null,
                ColorDataContract.ColorEntry._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.GONE);

        if (data.getCount() <= 0) {
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
        }

        // Swap the new cursor in. (The framework will take care of closing the
        // old cursor once we return.)
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        adapter.swapCursor(null);
    }

    @Subscribe
    public void onColorDeleteEvent(ColorDeleteEvent event) {
        String mSelectionClause = ColorDataContract.ColorEntry._ID + "=?";
        String[] mSelectionArgs = {String.valueOf(event.getColorId())};

        getActivity().getContentResolver().delete(
                RGBToolContentProvider.CONTENT_URI,
                mSelectionClause,
                mSelectionArgs);
    }

    @Subscribe
    public void onColorShareEvent(ColorShareEvent event) {
        String[] projection = {ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_R,
                ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_G,
                ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_B,
                ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_A
        };
        String selectionClause = ColorDataContract.ColorEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(event.getColorId())};

        Cursor cursor = getActivity().getContentResolver().query(RGBToolContentProvider.CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
            int rgbRValue = cursor.getInt(cursor.getColumnIndex(ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_R));
            int rgbGValue = cursor.getInt(cursor.getColumnIndex(ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_G));
            int rgbBValue = cursor.getInt(cursor.getColumnIndex(ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_B));
            int rgbAValue = cursor.getInt(cursor.getColumnIndex(ColorDataContract.ColorEntry.COLUMN_COLOR_RGB_A));
            cursor.close();

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    ColorUtils.getColorMessage(rgbRValue,
                            rgbGValue,
                            rgbBValue,
                            rgbAValue)
            );
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.action_share)));
        }
    }
}
