package com.tobey.nytimessearch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.tobey.nytimessearch.activities.SearchActivity;

/**
 * Created by tobeyyang on 16/12/2016.
 */

public class FilterDialogFragment extends DialogFragment{
    private DatePicker mDatePicker;
    private Spinner mSpinner;
    private CheckBox artCheckBox;
    private CheckBox fashionCheckBox;
    private CheckBox sportCheckBox;
    private EditText dateEditText;
    private SearchActivity mCallback;
    public FilterDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }
    public static FilterDialogFragment newInstance(Filter filter) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        if(filter != null){
            args.putString("beginDate", filter.get_beginDate());
            args.putString("sortingOrder", filter.get_sortingOrder());
            args.putString("newsType", filter.get_newsType());
            frag.setArguments(args);
        }
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        artCheckBox = (CheckBox) view.findViewById(R.id.checkBox_art) ;
        fashionCheckBox = (CheckBox) view.findViewById(R.id.checkBox_fashion) ;
        sportCheckBox = (CheckBox) view.findViewById(R.id.checkBox_sport) ;
        dateEditText = (EditText) view.findViewById(R.id.dateEditText);
        Button mSaveButton = (Button) view.findViewById( R.id.saveButton );
        if(getArguments() != null){
            String beginDate = getArguments().getString("beginDate");
            if(!beginDate.equals("")){
                dateEditText.setText(beginDate.substring(0,4) + "/" + beginDate.substring(4,6) + "/" + beginDate.substring(6));
            }
            Log.d("Debug", getArguments().getString("beginDate"));
            Log.d("Debug", getArguments().getString("sortingOrder"));
            Log.d("Debug", getArguments().getString("newsType"));
            if (getArguments().getString("sortingOrder").equals("oldest")){
                mSpinner.setSelection(0);
            }else{
                mSpinner.setSelection(1);
            }
            if (getArguments().getString("newsType").contains("Sports")){
                Log.d("Debug", "Sports");
                sportCheckBox.setChecked(true);
            }
            if (getArguments().getString("newsType").contains("Arts")){
                Log.d("Debug", "Arts");
                artCheckBox.setChecked(true);
            }
            if(getArguments().getString("newsType").contains("Fashion")){
                Log.d("Debug", "Fashion");
                fashionCheckBox.setChecked(true);
            }
        }

        dateEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mCallback.onCalendarClick();
            }
        });

        mSaveButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FilterItemDialogListener listener = (FilterItemDialogListener) getActivity();

                String order = mSpinner.getSelectedItem().toString();
                String newsType = artCheckBox.isChecked() ? "\"Arts\"" : "";
                newsType += fashionCheckBox.isChecked() ? "\"Fashion\"" : "";
                newsType += sportCheckBox.isChecked() ? "\"Sports\"" : "";
                Filter filter = new Filter(dateEditText.getText().toString().replace("/", ""), order, newsType);
                listener.onFinishFilterDialog( filter );
                dismiss();
            }
        } );
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            if (context instanceof SearchActivity){
                mCallback = (SearchActivity) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public void updateSelectedDate(String selectedDate){
        Log.d("Debug", "updateSelectedDate "+selectedDate);
        dateEditText.setText(selectedDate);
    }

    // Defines the listener interface
    public interface FilterItemDialogListener {
        void onFinishFilterDialog(Filter filter);
    }

}
