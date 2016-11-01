package com.android.elabcare.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.elabcare.criminalintent.dialog.DatePickerFragment;

import java.util.Date;
import java.util.UUID;

/**
 * Created by runningwater on 2016/10/26.
 */
// CrimeFragment 类是与模型及视图对象交互的控制器
public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    private static final boolean DEBUG = true;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int RQUEUEST_DATE = 0;

    private static String DateFormatPattern = "yyy年MM月dd日hh时mm分";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 留空白
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        final Date date = mCrime.getDate();
        mDateButton.setText(DateFormat.format(DateFormatPattern, date).toString());
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                //DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(CrimeFragment.this, RQUEUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG) Log.d(TAG, "返回CrimeFragment时调用方法.........onActivityResult");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == RQUEUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(DateFormat.format(DateFormatPattern, mCrime.getDate()).toString());
        }
    }
}
