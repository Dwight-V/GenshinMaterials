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
    private Button btnAdd, btnSubtract,btnClear;

    private Switch swtEditable;

    // TODO: Rename to edittextSelected
    private EditText raritySelected;

    // region For saving the data on app close.
    // Is where all of our data is saved.
    public static final String SHARED_PREFS = "sharedPrefs";
    // Corresponds to each type of rarity. Once it's working, use an array instead.
    public static final String VALUE_YELLOW = "yellow";
    public static final String VALUE_PURPLE = "purple";
    public static final String VALUE_BLUE = "blue";
    public static final String VALUE_GREEN = "green";
    public static final String VALUE_GREY = "grey";

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
        btnClear = (Button) findViewById(R.id.button_clear);

        swtEditable = (Switch) findViewById(R.id.switch_editable);

        // region btnAdd btnSub OnClickListeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (raritySelected != null) {
                    raritySelected.setText(String.valueOf(Integer.parseInt(raritySelected.getText().toString()) + 1));
                    saveData();
                }
            }
        });

        btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (raritySelected != null && Integer.parseInt(raritySelected.getText().toString()) - 1 >= 0) {
                    raritySelected.setText(String.valueOf(Integer.parseInt(raritySelected.getText().toString()) - 1));
                    saveData();
                }
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
                return false;
            }
        });
        // endregion


        // region  edittextTouchListeners
        edtYellow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                raritySelected = edtYellow;
                if (Integer.parseInt(raritySelected.getText().toString()) < 0) {
                    raritySelected.setText("0");
                }
                return false;
            }
        });

        edtPurple.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                raritySelected = edtPurple;
                if (Integer.parseInt(raritySelected.getText().toString()) < 0) {
                    raritySelected.setText("0");
                }
                return false;
            }
        });

        edtBlue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                raritySelected = edtBlue;
                if (Integer.parseInt(raritySelected.getText().toString()) < 0) {
                    raritySelected.setText("0");
                }
                return false;
            }
        });

        edtGreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                raritySelected = edtGreen;
                if (Integer.parseInt(raritySelected.getText().toString()) < 0) {
                    raritySelected.setText("0");
                }
                return false;
            }
        });

        edtGrey.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                raritySelected = edtGrey;
                if (Integer.parseInt(raritySelected.getText().toString()) < 0) {
                    raritySelected.setText("0");
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
                if (edtPurple.getText().toString().equals("")) {
                    edtPurple.setText("0");
                }
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
                saveData();
            }
        });

        // endregion

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

        editor.putString(VALUE_YELLOW, edtYellow.getText().toString());
        editor.putString(VALUE_PURPLE, edtPurple.getText().toString());
        editor.putString(VALUE_BLUE, edtBlue.getText().toString());
        editor.putString(VALUE_GREEN, edtGreen.getText().toString());
        editor.putString(VALUE_GREY, edtGrey.getText().toString());

        editor.putBoolean(SWITCH_EDITABLE_IS_CHECKED, swtEditable.isChecked());
//        Toast.makeText(this, SWITCH_EDITABLE_IS_CHECKED + " " + swtEditable.isChecked(), Toast.LENGTH_SHORT).show();

        editor.apply();
//        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        rarityVars = new String[5];
        rarityVars[0] = sharedPreferences.getString(VALUE_YELLOW, "0");
        rarityVars[1] = sharedPreferences.getString(VALUE_PURPLE, "0");
        rarityVars[2] = sharedPreferences.getString(VALUE_BLUE, "0");
        rarityVars[3] = sharedPreferences.getString(VALUE_GREEN, "0");
        rarityVars[4] = sharedPreferences.getString(VALUE_GREY, "0");

        editableIsChecked = sharedPreferences.getBoolean(SWITCH_EDITABLE_IS_CHECKED, false);

    }

    public void updateViews() {
        edtYellow.setText(rarityVars[0]);
        edtPurple.setText(rarityVars[1]);
        edtBlue.setText(rarityVars[2]);
        edtGreen.setText(rarityVars[3]);
        edtGrey.setText(rarityVars[4]);
        swtEditable.setChecked(editableIsChecked);
        changeEditable();
    }

    // TODO: Make changeEditable() have an EditText as an input, i.e. make it generic, then pass an array of all the buttons through a for loop (if a single instance isn't needed). Do the same for all other functions.
    // TODO: Also, after the initial switch commit, be sure to add the number limit stored in notepad.
    public void changeEditable() {
        if (swtEditable.isChecked()) {
            edtYellow.setInputType(InputType.TYPE_NULL);
//            edtYellow.setInputType(InputType.TYPE_NULL);
            // From: https://stackoverflow.com/questions/1109022/how-can-i-close-hide-the-android-soft-keyboard-programmatically
            if (this.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }

            edtPurple.setInputType(InputType.TYPE_NULL);
            edtBlue.setInputType(InputType.TYPE_NULL);
            edtGreen.setInputType(InputType.TYPE_NULL);
            edtGrey.setInputType(InputType.TYPE_NULL);
        } else {
            edtYellow.setInputType(InputType.TYPE_CLASS_NUMBER);

            edtPurple.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtBlue.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtGreen.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtGrey.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }


}