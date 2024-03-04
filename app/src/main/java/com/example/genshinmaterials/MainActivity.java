package com.example.genshinmaterials;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private EditText edtYellow, edtPurple, edtBlue, edtGreen, edtGrey;
    private TextView txtTemp, txtTemp2;
  
    private Button btnClear;
    private Button btnAddYellow, btnSubYellow, btnAddPurple, btnSubPurple, btnAddBlue, btnSubBlue, btnAddGreen, btnSubGreen, btnAddGrey, btnSubGrey;


    private Switch swtEditable;

    private EditText edittextSelected;

    // Is an array of all EditTexts, used in loops for updated generic functions.
    private EditText[] allEditTexts;

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
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DELETE ME WHEN FINISHED
//        txtTemp = (TextView) findViewById(R.id.text_temp);
//        txtTemp2 = (TextView) findViewById(R.id.text_temp2);
//
//        edtYellow = (EditText) findViewById(R.id.edittext_yellow);
//        edtPurple = (EditText) findViewById(R.id.edittext_purple);
//        edtBlue = (EditText) findViewById(R.id.edittext_blue);
//        edtGreen = (EditText) findViewById(R.id.edittext_green);
//        edtGrey = (EditText) findViewById(R.id.edittext_grey);
//
//        btnClear = (Button) findViewById(R.id.button_clear);
//        btnAddYellow = (Button) findViewById(R.id.button_add_yellow);
//        btnSubYellow = (Button) findViewById(R.id.button_sub_yellow);
//        btnAddPurple = (Button) findViewById(R.id.button_add_purple);
//        btnSubPurple = (Button) findViewById(R.id.button_sub_purple);
//        btnAddBlue = (Button) findViewById(R.id.button_add_blue);
//        btnSubBlue = (Button) findViewById(R.id.button_sub_blue);
//        btnAddGreen = (Button) findViewById(R.id.button_add_green);
//        btnSubGreen = (Button) findViewById(R.id.button_sub_green);
//        btnAddGrey = (Button) findViewById(R.id.button_add_grey);
//        btnSubGrey = (Button) findViewById(R.id.button_sub_grey);
//
//        swtEditable = (Switch) findViewById(R.id.switch_editable);


        allEditTexts = new EditText[]{edtYellow, edtPurple, edtBlue, edtGreen, edtGrey};

        //          All from series https://www.youtube.com/watch?v=fGcMLu1GJEc
        // Sets toolbar (a stronger ActionBar) to the one we made before. Uses a built in command for ease of use.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Adds drawer to our toolbar, with two required string for handicap readings.
        // The drawer is made up of two things: the NavigationView and the Toolbar. The NavigationView is what holds the menu and headers, and the Toolbar is what sits at the top of the screen.
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Sets the initial fragment to WeaponFragment. Needs if statement for any runtime update such as rotating the screen, which destroys the activity (and this method is run again).
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_weapon);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new WeaponFragment()).commit();
        }

/*
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
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
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
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
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
*/
    }

    // Sets the correct fragment for each item selected from the drawer.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_weapon) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WeaponFragment()).commit();
        } else if (id == R.id.nav_character) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CharacterFragment()).commit();
        } else if (id == R.id.nav_talent) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TalentFragment()).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Stops the app from closing when hitting the back button when the drawer is open.
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}