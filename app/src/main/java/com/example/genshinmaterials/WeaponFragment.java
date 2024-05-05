package com.example.genshinmaterials;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WeaponFragment extends Fragment {

    private EditText edtYellow, edtPurple, edtBlue, edtGreen, edtGrey;
    private TextView txtTemp, txtTemp2;

    private Button btnClear;
    private Button btnAddYellow, btnSubYellow, btnAddPurple, btnSubPurple, btnAddBlue, btnSubBlue, btnAddGreen, btnSubGreen, btnAddGrey, btnSubGrey;


    private Switch swtEditable;

    private EditText edittextSelected;

    // region For saving the data on app close.
    // Is where all of our data is saved.
    public static final String SHARED_PREFS = "sharedPrefs";

    // Holds each EditText value on app closure.
    public static final String[] EDITTEXT_VALUES = {"yellow", "purple", "blue", "green", "grey"};

    // Used in conjunction with allEditTexts for a clean loop in functions.
//    private final String[] allValueConstants = new String[]{VALUE_YELLOW, VALUE_PURPLE, VALUE_BLUE, VALUE_GREEN, VALUE_GREY};

    public static final String SWITCH_EDITABLE_IS_CHECKED = ".";

    // When the app is opened, these are the variables it looks at to set as the values. Length is set in instanceation.
    public String[] rarityVars;
    public boolean editableIsChecked;

    private EditText[] allEditTexts;
    // endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weapon, container, false);
        txtTemp = (TextView) view.findViewById(R.id.text_temp);
        txtTemp2 = (TextView) view.findViewById(R.id.text_temp2);

        edtYellow = (EditText) view.findViewById(R.id.edittext_yellow);
        edtPurple = (EditText) view.findViewById(R.id.edittext_purple);
        edtBlue = (EditText) view.findViewById(R.id.edittext_blue);
        edtGreen = (EditText) view.findViewById(R.id.edittext_green);
        edtGrey = (EditText) view.findViewById(R.id.edittext_grey);

        btnClear = (Button) view.findViewById(R.id.button_clear);
        btnAddYellow = (Button) view.findViewById(R.id.button_add_yellow);
        btnSubYellow = (Button) view.findViewById(R.id.button_sub_yellow);
        btnAddPurple = (Button) view.findViewById(R.id.button_add_purple);
        btnSubPurple = (Button) view.findViewById(R.id.button_sub_purple);
        btnAddBlue = (Button) view.findViewById(R.id.button_add_blue);
        btnSubBlue = (Button) view.findViewById(R.id.button_sub_blue);
        btnAddGreen = (Button) view.findViewById(R.id.button_add_green);
        btnSubGreen = (Button) view.findViewById(R.id.button_sub_green);
        btnAddGrey = (Button) view.findViewById(R.id.button_add_grey);
        btnSubGrey = (Button) view.findViewById(R.id.button_sub_grey);

        swtEditable = (Switch) view.findViewById(R.id.switch_editable);

        allEditTexts = new EditText[]{edtYellow, edtPurple, edtBlue, edtGreen, edtGrey};


        // region  add/sub buttonClickListeners

        btnAddYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(edtYellow);
            }
        });

        btnSubYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(edtYellow);
            }
        });

        btnClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edtBlue.setText("0");
                edtGreen.setText("0");
                edtGrey.setText("0");
                edtPurple.setText("0");
                edtYellow.setText("0");
                saveData();
                return false;
            }
        });

        boolean raytracing = true;

        btnSubYellow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edtYellow.setText("0");
                saveData();
                return false;
            }
        });

        btnAddPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(edtPurple);
            }
        });

        btnSubPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(edtPurple);
            }
        });

        btnSubPurple.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edtPurple.setText("0");
                saveData();
                return false;
            }
        });

        btnAddBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(edtBlue);
            }
        });

        btnSubBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(edtBlue);
            }
        });

        btnSubBlue.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edtBlue.setText("0");
                saveData();
                return false;
            }
        });

        btnAddGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(edtGreen);
            }
        });

        btnSubGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(edtGreen);
            }
        });

        btnSubGreen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edtGreen.setText("0");
                saveData();
                return false;
            }
        });

        btnAddGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(edtGrey);
            }
        });

        btnSubGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(edtGrey);
            }
        });

        btnSubGrey.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edtGrey.setText("0");
                saveData();
                return false;
            }
        });

        // endregion

        // TODO: Having the program check != "" after every change is annoying; should have null checking only after add/subtract, or when it loses focus.
        // TODO: Get rid of leading zero when typing.
        // region edittextTextChangeListeners

        edtYellow.addTextChangedListener(new TextWatcher() {
            EditText thisEditText = edtYellow;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (thisEditText.getText().toString().equals("")) {
                    thisEditText.setText("0");
                }
                checkOverflow(thisEditText);
                saveData();
            }
        });

        edtPurple.addTextChangedListener(new TextWatcher() {
            EditText thisEditText = edtPurple;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                edtPurple.setText(" ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (thisEditText.getText().toString().equals("")) {
                    thisEditText.setText("0");
                }
                checkOverflow(thisEditText);
                saveData();
            }
        });

        edtBlue.addTextChangedListener(new TextWatcher() {
            EditText thisEditText = edtBlue;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (thisEditText.getText().toString().equals("")) {
                    thisEditText.setText("0");
                }
                checkOverflow(thisEditText);
                saveData();
            }
        });


        edtGreen.addTextChangedListener(new TextWatcher() {
            EditText thisEditText = edtGreen;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (thisEditText.getText().toString().equals("")) {
                    thisEditText.setText("0");
                }
                checkOverflow(thisEditText);
                saveData();
            }
        });

        edtGrey.addTextChangedListener(new TextWatcher() {
            EditText thisEditText = edtGrey;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (thisEditText.getText().toString().equals("")) {
                    thisEditText.setText("0");
                }
                checkOverflow(thisEditText);
                saveData();
            }
        });

        // endregion

        // TODO: If swtEditable is checked, app exit, app exit, swtEditable will no longer be checked.
        swtEditable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                disableEditText(edtYellow);
//                Toast.makeText(MainActivity.this, "changed!", Toast.LENGTH_SHORT).show();
                changeEditable();
                saveData();
            }
        });

//        edtYellow.dispatchConfigurationChanged(new Configuration(this, Window(KeyStore.TrustedCertificateEntry(true))));

        // Loads data and displays saved data on app launch.
        loadData();
        updateViews();
        return view;
    }

    public void saveData() {

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        for (int i = 0; i < rarityVars.length; i++) {
//            editor.putString(allValueConstants[i], allEditTexts[i].getText().toString());
//        }

        for (int i = 0; i < EDITTEXT_VALUES.length; i++) {
            editor.putString(EDITTEXT_VALUES[i], allEditTexts[i].getText().toString());
        }

        editor.putBoolean(SWITCH_EDITABLE_IS_CHECKED, swtEditable.isChecked());
//        Toast.makeText(this, VALUE_BLUE + " " + edtBlue.getText().toString(), Toast.LENGTH_SHORT).show();

        editor.apply();
//        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        rarityVars = new String[5];

        for (int i = 0; i < rarityVars.length; i++) {
            rarityVars[i] = sharedPreferences.getString(EDITTEXT_VALUES[i], "0");
        }
        editableIsChecked = sharedPreferences.getBoolean(SWITCH_EDITABLE_IS_CHECKED, false);
    }

    // Changes the values of the EditTexts and Switch to saved values.
    public void updateViews() {
        for (int i = 0; i < allEditTexts.length; i++) {
            allEditTexts[i].setText(rarityVars[i]);
        }
        swtEditable.setChecked(editableIsChecked);
        changeEditable();
    }

    // Reads swtEditable's state, and locks or unlocks editablitiy on all EditTexts depending on the state.
    public void changeEditable() {
        if (swtEditable.isChecked()) {
            // From: https://stackoverflow.com/questions/1109022/how-can-i-close-hide-the-android-soft-keyboard-programmatically
            if (requireActivity().getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
            }

            for (int i = 0; i < allEditTexts.length; i++) {
                allEditTexts[i].setInputType(InputType.TYPE_NULL);
            }
        } else {
            for (int i = 0; i < allEditTexts.length; i++) {
                allEditTexts[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }
    }

    // App crashes when numbers are absurdly large. IMO 10000 of one resource is a plenty high ceiling.
    public void checkOverflow(EditText editText) {
        if (Integer.parseInt(editText.getText().toString()) > 10000) {
            editText.setText("10000");
        }
    }

    public void add(EditText edtText) {
        if (edtText.getText() != null) {
            edtText.setText(String.valueOf(Integer.parseInt(edtText.getText().toString()) + 1));
            saveData();
        }
    }

    public void sub(EditText edtText) {
        if (edtText != null && Integer.parseInt(edtText.getText().toString()) - 1 >= 0) {
            edtText.setText(String.valueOf(Integer.parseInt(edtText.getText().toString()) - 1));
            saveData();
        }
    }
}
