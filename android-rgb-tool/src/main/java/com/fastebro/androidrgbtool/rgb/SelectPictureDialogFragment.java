package com.fastebro.androidrgbtool.rgb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastebro.androidrgbtool.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by danielealtomare on 21/06/14.
 * Project: rgb-tool
 */
public class SelectPictureDialogFragment extends BottomSheetDialogFragment
        implements SelectPictureListAdapter.ItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private Unbinder unbinder;

    public SelectPictureDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_picture, container);
        unbinder = ButterKnife.bind(this, view);

        SelectPictureListAdapter adapter = new SelectPictureListAdapter(
                getResources().getStringArray(R.array.pick_color_array), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getDialog().setTitle(getString(R.string.pick_color));

        return view;
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        switch (position) {
            case 0:
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).openRGBToolGallery();
                    dismiss();
                }
                break;
            case 1:
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).openDeviceGallery();
                    dismiss();
                }
                break;
            case 2:
                if (getActivity() != null) {
                    try {
                        ((MainActivity) getActivity()).dispatchTakePictureIntent();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
