package com.example.s_ruslanshaburu.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RadioGroup;

import java.util.Date;

public class UpdateChoiceFragment extends DialogFragment {

    private Date mDate;
    private RadioGroup mRadioGroup;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date) getArguments().getSerializable(DatePickerFragment.EXTRA_DATE);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_choice, null);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.choiceGroup);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.choice_update_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startUpdateDialog();
                    }
                })
                .create();
    }

    private void startUpdateDialog() {
        int checkedId = mRadioGroup.getCheckedRadioButtonId();
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        switch (checkedId) {
            case R.id.radioUpdateDate:
                // show date dialog
                final DatePickerFragment dateDialog = DatePickerFragment.newInstance(mDate);
                dateDialog.setTargetFragment(getTargetFragment(), CrimeFragment.REQUEST_DATE);
                dateDialog.show(fm, CrimeFragment.DIALOG_DATE);
                break;
            case R.id.radioUpdateTime:
                // show time update dialog
                final TimePickerFragment timeDialog = TimePickerFragment.newInstance(mDate);
                timeDialog.setTargetFragment(getTargetFragment(), CrimeFragment.REQUEST_TIME);
                timeDialog.show(fm, CrimeFragment.DIALOG_TIME);
                break;
            default:
                break;
        }
    }

    public static UpdateChoiceFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(DatePickerFragment.EXTRA_DATE, date);

        UpdateChoiceFragment updateChoiceFragment = new UpdateChoiceFragment();
        updateChoiceFragment.setArguments(args);

        return updateChoiceFragment;
    }
}
