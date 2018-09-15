package com.mansourappdevelopment.androidapp.dailyquote;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Created by Mansour on 8/27/2018.
 */

public class TimePickerFragment extends android.support.v4.app.DialogFragment {
    View mView;
    Button mBtnOk;
    TimePicker mTimePicker;
    int mChoosenHour;
    int mChoosenMinute;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_time_picker, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTimePicker = mView.findViewById(R.id.timePicker);
        mBtnOk = mView.findViewById(R.id.btnOk);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mMainActivity = (MainActivity) getActivity();
                if(Build.VERSION.SDK_INT < 23){
                    mChoosenHour = mTimePicker.getCurrentHour();
                    mChoosenMinute = mTimePicker.getCurrentMinute();

                }else {
                    mChoosenHour = mTimePicker.getHour();
                    mChoosenMinute = mTimePicker.getMinute();
                }
                mMainActivity.setTime(mChoosenHour, mChoosenMinute);
                dismiss();
            }
        });
    }
}
