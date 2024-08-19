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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SWITCH_EDITABLE_IS_CHECKED = "switch_editable_is_checked";
    private DrawerLayout drawer;
    private Toolbar toolbar;
    protected Switch swtEditable;


    boolean editableIsChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //          All from series https://www.youtube.com/watch?v=fGcMLu1GJEc
        // Sets toolbar (a stronger ActionBar) to the one we made before. Uses a built in command for ease of use.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Adds drawer to our toolbar, with two required string for handicap readings.
        // The drawer is made up of two things: the NavigationView and the Toolbar. The NavigationView is what holds the menu and headers, and the Toolbar is what sits at the top of the screen.
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        swtEditable = toolbar.findViewById(R.id.switch_editable_full);

        // Sets the initial fragment to WeaponFragment. Needs if statement for any runtime update such as rotating the screen, which destroys the activity (and this method is run again).
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_weapon);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new WeaponFragment()).commit();
        }

        swtEditable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(MainActivity.this, isChecked + "", Toast.LENGTH_SHORT).show();
//                if (isChecked) {
//
//                } else {
//
//                }
                saveData();
            }
        });

        loadData();
        swtEditable.setChecked(editableIsChecked);
    }

    // Sets the correct fragment for each item selected from the drawer.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_weapon) {
            toolbar.setTitle("Weapon");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WeaponFragment()).commit();
        } else if (id == R.id.nav_character) {
            toolbar.setTitle("Character");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CharacterFragment()).commit();
        } else if (id == R.id.nav_talent) {
            toolbar.setTitle("Talent");
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

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH_EDITABLE_IS_CHECKED, swtEditable.isChecked());

        editor.apply();
//        Toast.makeText(this, sharedPreferences.getAll().get(SWITCH_EDITABLE_IS_CHECKED).toString(), Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        editableIsChecked = sharedPreferences.getBoolean(SWITCH_EDITABLE_IS_CHECKED, false);

    }
}