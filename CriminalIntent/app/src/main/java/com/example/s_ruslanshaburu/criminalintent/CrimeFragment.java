package com.example.s_ruslanshaburu.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    public static final String EXTRA_CRIME_ID = "edu.lwtech.radu.criminalintent.crime_id";

    public static final String DIALOG_DATE = "date";
    public static final String DIALOG_TIME = "time";
    public static final String DIALOG_CHOICE = "choice";
    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_TIME = 1;
    public static final int REQUEST_CHOICE = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

    public static CrimeFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, id);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID id = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) rootView.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mDateButton = (Button) rootView.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                final UpdateChoiceFragment updateChoiceDialog = UpdateChoiceFragment.newInstance(mCrime.getDate());
                updateChoiceDialog.setTargetFragment(CrimeFragment.this, REQUEST_CHOICE);
                updateChoiceDialog.show(fm, DIALOG_CHOICE);
            }
        });

        mSolvedCheckBox = (CheckBox) rootView.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return rootView;
    }

    private void updateDate() {
        mDateButton.setText(SIMPLE_DATE_FORMAT.format(mCrime.getDate()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            final Calendar updatedTime = Calendar.getInstance(Locale.getDefault());
            updatedTime.setTime(date);
            final Calendar newDate = Calendar.getInstance(Locale.getDefault());
            newDate.setTime(mCrime.getDate());
            newDate.set(Calendar.DAY_OF_MONTH, updatedTime.get(Calendar.DAY_OF_MONTH));
            newDate.set(Calendar.MONTH, updatedTime.get(Calendar.MONTH));
            newDate.set(Calendar.YEAR, updatedTime.get(Calendar.YEAR));
            mCrime.setDate(newDate.getTime());
            updateDate();
        } else if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            final Calendar updatedTime = Calendar.getInstance(Locale.getDefault());
            updatedTime.setTime(date);
            final Calendar newTime = Calendar.getInstance(Locale.getDefault());
            newTime.setTime(mCrime.getDate());
            newTime.set(Calendar.HOUR_OF_DAY, updatedTime.get(Calendar.HOUR_OF_DAY));
            newTime.set(Calendar.MINUTE, updatedTime.get(Calendar.MINUTE));
            mCrime.setDate(newTime.getTime());
            updateDate();
        }
    }
}