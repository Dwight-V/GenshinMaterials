package com.example.genshinmaterials;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
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
    private DrawerLayout drawer;
    private Toolbar toolbar;
    protected Switch swtEditable;

    private NavigationView navMainMenu;


    private static final String SWITCH_EDITABLE_IS_CHECKED = "switch_editable_is_checked";

    public static String TAB_POSITION = "tab_position";

    public static int currentTabId;
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
        navMainMenu = findViewById(R.id.nav_main_menu);
        navMainMenu.setNavigationItemSelectedListener(this);

        swtEditable = toolbar.findViewById(R.id.switch_editable_full);

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

        // Sets the initial fragment to WeaponFragment. Needs if statement for any runtime update such as rotating the screen, which destroys the activity (and this method is run again).
        if (savedInstanceState == null) {
            // Checks if loadData() has no value for currentTabId.
            if (currentTabId == -1) {
                currentTabId = R.id.nav_weapon;
            }
            navMainMenu.setCheckedItem(currentTabId);
            updateFragmentView();
        }
    }

    // Sets the correct fragment for each item selected from the drawer.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(this, "ItemSelected", Toast.LENGTH_SHORT).show();
        // Checks if loadData() has no value for currentTabId.
        if (currentTabId == -1) {
            Toast.makeText(this, "currentTabId == " + currentTabId, Toast.LENGTH_SHORT).show();
            return false;
        }
        currentTabId = item.getItemId();

        updateFragmentView();

        drawer.closeDrawer(GravityCompat.START);
        saveData();
//        Toast.makeText(this, item + "     " + currentTabId, Toast.LENGTH_SHORT).show();
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

    public void updateFragmentView() {
        if (currentTabId == R.id.nav_weapon) {
            toolbar.setTitle("Weapon");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new WeaponFragment()).commit();
        } else if (currentTabId == R.id.nav_character) {
            toolbar.setTitle("Character");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CharacterFragment()).commit();
        } else if (currentTabId == R.id.nav_talent) {
            toolbar.setTitle("Talent");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TalentFragment()).commit();
        }
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH_EDITABLE_IS_CHECKED, swtEditable.isChecked());
        editor.putInt(TAB_POSITION, currentTabId);

        editor.apply();
//        Toast.makeText(this, sharedPreferences.getAll().get(SWITCH_EDITABLE_IS_CHECKED).toString(), Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        editableIsChecked = sharedPreferences.getBoolean(SWITCH_EDITABLE_IS_CHECKED, false);
        
        currentTabId = sharedPreferences.getInt(TAB_POSITION, -1);
    }
}