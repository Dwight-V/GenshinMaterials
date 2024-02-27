package com.example.genshinmaterials;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edtYellow, edtPurple, edtBlue, edtGreen, edtGrey;
    private TextView txtTemp, txtTemp2;
    private Button btnAdd, btnSubtract;

    private Switch swtEditable;

    private EditText edittextSelected;

    // Is an array of all EditTexts, used in loops for updated generic functions.
    private EditText[] allEditTexts;

    // region For saving the data on app close.
    // Is where all of our data is saved.
    public static final String SHARED_PREFS = "sharedPrefs";
    // Corresponds to each type of rarity. Once it's working, use an array instead.
    public static final String VALUE_YELLOW = "yellow";
    public static final String VALUE_PURPLE = "purple";
    public static final String VALUE_BLUE = "0";
    public static final String VALUE_GREEN = "green";
    public static final String VALUE_GREY = "grey";

    // Used in conjunction with allEditTexts for a clean loop in functions.
    private final String[] allValueConstants = new String[]{VALUE_YELLOW, VALUE_PURPLE, VALUE_BLUE, VALUE_GREEN, VALUE_GREY};

    public static final String SWITCH_EDITABLE_IS_CHECKED = ".";

    // When the app is opened, these are the variables it looks at to set as the values. Length is set in instanceation.
    public String[] rarityVars;
    public boolean editableIsChecked;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DELETE ME WHEN FINISHED
        txtTemp = (TextView) findViewById(R.id.text_temp);
        txtTemp2 = (TextView) findViewById(R.id.text_temp2);

        edtYellow = (EditText) findViewById(R.id.edittext_yellow);
        edtPurple = (EditText) findViewById(R.id.edittext_purple);
        edtBlue = (EditText) findViewById(R.id.edittext_blue);
        edtGreen = (EditText) findViewById(R.id.edittext_green);
        edtGrey = (EditText) findViewById(R.id.edittext_grey);

        btnAdd = (Button) findViewById(R.id.button_add);
        btnSubtract = (Button) findViewById(R.id.button_subtract);

        swtEditable = (Switch) findViewById(R.id.switch_editable);


        allEditTexts = new EditText[]{edtYellow, edtPurple, edtBlue, edtGreen, edtGrey};

        // region btnAdd btnSub OnClickListeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittextSelected != null) {
                    edittextSelected.setText(String.valueOf(Integer.parseInt(edittextSelected.getText().toString()) + 1));
                    checkOverflow(edittextSelected);
                    saveData();
                }
            }
        });

        btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittextSelected != null && Integer.parseInt(edittextSelected.getText().toString()) - 1 >= 0) {
                    edittextSelected.setText(String.valueOf(Integer.parseInt(edittextSelected.getText().toString()) - 1));
                    checkOverflow(edittextSelected);
                    saveData();
                }
            }
        });
        // endregion

        // region  edittextTouchListeners
        edtYellow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edittextSelected = edtYellow;
                if (Integer.parseInt(edittextSelected.getText().toString()) < 0) {
                    edittextSelected.setText("0");

                } else {
                    checkOverflow(edittextSelected);
                }
                return false;
            }
        });

        edtPurple.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edittextSelected = edtPurple;
                if (Integer.parseInt(edittextSelected.getText().toString()) < 0) {
                    edittextSelected.setText("0");
                } else {
                    checkOverflow(edittextSelected);
                }
                return false;
            }
        });

        edtBlue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edittextSelected = edtBlue;
                if (Integer.parseInt(edittextSelected.getText().toString()) < 0) {
                    edittextSelected.setText("0");
                } else {
                    checkOverflow(edittextSelected);
                }
                return false;
            }
        });

        edtGreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edittextSelected = edtGreen;
                if (Integer.parseInt(edittextSelected.getText().toString()) < 0) {
                    edittextSelected.setText("0");
                } else {
                    checkOverflow(edittextSelected);
                }
                return false;
            }
        });

        edtGrey.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edittextSelected = edtGrey;
                if (Integer.parseInt(edittextSelected.getText().toString()) < 0) {
                    edittextSelected.setText("0");
                } else {
                    checkOverflow(edittextSelected);
                }
                return false;
            }
        });

        // endregion

        // region edittextTextChangeListeners

        edtYellow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Toast.makeText(MainActivity.this, "beforeTextChanged!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData();
//                Toast.makeText(MainActivity.this, "onTextChanged!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtYellow.getText().toString().equals("")) {
                    edtYellow.setText("0");
                }
                checkOverflow(edtYellow);
                saveData();
            }
        });

        edtPurple.addTextChangedListener(new TextWatcher() {
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
                if (edtYellow.getText().toString().equals("")) {
                    edtYellow.setText("0");
                }
                checkOverflow(edtYellow);
                saveData();
            }
        });

        edtBlue.addTextChangedListener(new TextWatcher() {
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
                if (edtYellow.getText().toString().equals("")) {
                    edtYellow.setText("0");
                }
                checkOverflow(edtYellow);
                saveData();
            }
        });


        edtGreen.addTextChangedListener(new TextWatcher() {
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
                if (edtGreen.getText().toString().equals("")) {
                    edtGreen.setText("0");
                }
                checkOverflow(edtYellow);
                saveData();
            }
        });

        edtGrey.addTextChangedListener(new TextWatcher() {
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
                if (edtGrey.getText().toString().equals("")) {
                    edtGrey.setText("0");
                }
                checkOverflow(edtYellow);
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
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < rarityVars.length; i++) {
            editor.putString(allValueConstants[i], allEditTexts[i].getText().toString());
        }
//        editor.putString(VALUE_PURPLE, edtPurple.getText().toString());
//        editor.putString(VALUE_BLUE, edtBlue.getText().toString());
//        editor.putString(VALUE_GREEN, edtGreen.getText().toString());
//        editor.putString(VALUE_GREY, edtGrey.getText().toString());

        editor.putBoolean(SWITCH_EDITABLE_IS_CHECKED, swtEditable.isChecked());
//        Toast.makeText(this, VALUE_BLUE + " " + edtBlue.getText().toString(), Toast.LENGTH_SHORT).show();

        editor.apply();
//        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        rarityVars = new String[5];

        for (int i = 0; i < rarityVars.length; i++) {
            rarityVars[i] = sharedPreferences.getString(allValueConstants[i], "0");
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
            if (this.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
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


}