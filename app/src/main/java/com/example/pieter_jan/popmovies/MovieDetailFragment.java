package com.example.pieter_jan.popmovies;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by pieter-jan on 11/15/2016.
 */



public class MovieDetailFragment extends Fragment {

    private static final String TAG = "CrimeFragment";

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    // A request code for the FragmentManager
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;

    private static final String DEBUG_TAG = "CrimeFragment";

    private Crime mCrime;
    private EditText mTitleField; // EditText for the title
    private Button mDateButton; // Button that displays the date
    private CheckBox mSolvedCheckBox; // Checkbox that shows if the crime has been solved
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);


    }
    /*
    Required interface for hosting activities
     */
    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);  // inflate the menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false); // second parameter is the view's parent;
        // third parameter tells the layout inflater whether to add the inflated view to the view's parent
        // we pass in flase because the view will be added in the activity's code

        // Also wire up widgets here
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle()); // Set the title with the existing title of the crime
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Left intentionally blank
            }
        });


        // the date button
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate(mCrime.getDateFormatted());
//        mDateButton.setEnabled(false); // the button is not clickable for now; will be changed in Chapter 13
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                // Tell the FragmentManager that the DatePickerFragment is linked to the CrimeFragment
                // This is needed when passing around data
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        // the checkbox
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the crime's solved property
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });

        // the report a crime button
        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ShareCompat.IntentBuilder.from(getActivity()) // Create an intent using the sharecompat.intentbuilder library
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(getString(R.string.send_report))
                        .createChooserIntent();

//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport()); // pass through the report string
//                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i, getString(R.string.send_report)); // Show a chooser for the implicit intent every time; no default app
                startActivity(i);
            }
        });

        // the choose a suspect from contacts button
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); // will be used again later, so outside onClickListener
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }


        // Call the suspect button

        mCallButton = (Button) v.findViewById(R.id.crime_call_suspect);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selectClause = ContactsContract.CommonDataKinds.Phone._ID + " = ?";

                String[] fields = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                String[] selectParams = {Long.toString(mCrime.getContactId())};

                Cursor cursor = getActivity().getContentResolver().query(contentUri, fields, selectClause, selectParams, null);

                if(cursor != null && cursor.getCount() >0)
                {
                    try
                    {
                        cursor.moveToFirst();

                        String number = cursor.getString(0);

                        Uri phoneNumber = Uri.parse("tel:" + number);
                        Log.d(DEBUG_TAG, phoneNumber.toString());

                        Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);

                        startActivity(intent);

                    } finally {
                        cursor.close();
                    }
                }
            }
        });

        if (mCrime.getSuspect() == null) {
            mCallButton.setEnabled(false);
        }


            // Check if there really is an application to carry out this implicit intent for the contacts
        PackageManager packageManager = getActivity().getPackageManager();
//        pickContact.addCategory((Intent.CATEGORY_HOME)); //you can temporarily check if this part works by enabling this code: it adds another functionality to the intent)
        if(packageManager.resolveActivity(pickContact,
                packageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectButton.setEnabled(false); // No such app available so can't use this option
        }


        // the photo button
        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Log.d(TAG, "mPhotoFile: " + mPhotoFile.toString());

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null; // Check if the phone has a camera
        mPhotoButton.setEnabled(canTakePhoto);

        if(canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            Log.d(TAG, "photo uri: " + uri.toString());
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });


        // the photo view
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPhotoView.getDrawable() != null && mPhotoFile!= null && mPhotoFile.exists()) {
                    FragmentManager manager = getFragmentManager();
                    ImageDialog.getImage(mPhotoFile.getPath(), getActivity());
                    new ImageDialog().show(manager, "newDialog");
                }               
            }
        });

        updatePhotoView();

        return v;
    }

    // update the list of crimes
    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    // Get the date from the DatePicker and bind it to the crime
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date); // Set the date on the crime
            updateCrime();
            updateDate(mCrime.getDateFormatted());
        }

        else if (requestCode == REQUEST_CONTACT && data != null){
            Uri contactUri = data.getData();
            // Specify whicch fields you want your query to return values for.
            String[] queryFields = new String[] {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};

            // Perform your query - the contactUri is like a "where" clause here
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try{
                //Double check that you actually got the results
                if(c.getCount() == 0){
                    return; // nothing found
                }

                //Pull out the first column of the first row of data - that is the name of the suspect
                c.moveToFirst();
                String suspect = c.getString(0); // the contact's name
                long contactId = c.getLong(1); // the contact's id

                mCrime.setSuspect(suspect);
                updateCrime();
                mCrime.setContactId(contactId);

                mSuspectButton.setText(suspect);

                mSuspectButton.setEnabled(true); // enables the call suspect button and changes the text

                mCallButton.setEnabled(true);

            } finally {
                c.close(); // again close the cursor to avoid nasty errors
            }
        }

        else if (requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }

    }

    // Set the date to the text button
    private void updateDate(String text) {
        mDateButton.setText(text); // Show in on the DateButton
    }


    // Create a bundle with the arguments that are connected to the CrimeFragment;
    // Has to happen after the Fragment gets created but Before it is added to the activity
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment(); // Create a new fragment using the self implemented abstract class SimpleFragment
        fragment.setArguments(args);
        return fragment;
    }

    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    private void updatePhotoView(){
        if (mPhotoFile == null & !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null); // there is no photo to place
            Log.d(TAG, "There is no fotofile to display");
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
            Log.d(TAG, "There is a fotofile to display");
        }
    }

    public void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

}
