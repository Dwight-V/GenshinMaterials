package com.example.genshinmaterials;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.Arrays;

public class WeaponFragment extends CounterFragment {

    // region For saving the data on app close.

    // Holds each EditText value on app closure. Note that the strings initialized here are to show the order, and are not saved.
    public static final String[] EDITTEXT_VALUES_0 = {"yellow_0_weapon", "purple_0_weapon", "blue_0_weapon", "green_0_weapon", "grey_0_weapon"};
    public static final String[] EDITTEXT_VALUES_1 = {"yellow_1_weapon", "purple_1_weapon", "blue_1_weapon", "green_1_weapon", "grey_1_weapon"};
    public static final String[] EDITTEXT_VALUES_2 = {"yellow_2_weapon", "purple_2_weapon", "blue_2_weapon", "green_2_weapon", "grey_2_weapon"};

    public static final String WEAPON_RARITY = "weapon_rarity";

    private int weaponRarity = 3;
    // Programmatically replaces the names of the subtabs.
    private static final String[] tabNamesArr = {"Domain", "Miniboss", "Enemy"};

    // The hardcoded amount of materials need to fully level up the weapon.
    // Each row is in descending rarity, mirroring allEditTexts.
    // Each column represents the corresponding indexed subtab. Ex: reqMats*Star[0] = "Domain", ...[1] = "Miniboss", ...[2] = "Enemy".
    private static int[][] reqMats5Star = {{6, 14, 14, 5, 0},
            {0, 41, 45, 5, 0},
            {0, 0, 27, 23, 15}};
    private static int[][] reqMats4Star = {{4, 9, 9, 3, 0},
            {0, 27, 30, 3, 0},
            {0, 0, 18, 15, 10}};
    private static int[][] reqMats3Star = {{3, 6, 6, 2, 0},
            {0, 18, 12, 10, 0},
            {0, 0, 12, 10, 6}};


    // Passes the final String array which names all EditTexts save data.
    WeaponFragment() {
        super(EDITTEXT_VALUES_0, EDITTEXT_VALUES_1, EDITTEXT_VALUES_2, tabNamesArr, reqMats5Star);
    }
    // endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_counter, container, false);
        View view = super.onCreateView(inflater, container, savedInstanceState);

        txtTypeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cycles which rarity of weapon to calc for.
                switch (weaponRarity) {
                    case 3:
                        txtTypeTitle.setText("4-Star Weapon");
                        weaponRarity = 4;
                        break;
                    case 4:
                        txtTypeTitle.setText("5-Star Weapon");
                        weaponRarity = 5;
                        break;
                    default:
                        txtTypeTitle.setText("3-Star Weapon");
                        weaponRarity = 3;
                        break;
                }
                saveData();
                checkRequirements();
            }
        });

        return view;
    }

    @Override
    public void saveData() {
        super.saveData();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WEAPON_RARITY, weaponRarity);
        editor.apply();
    }

    @Override
    public void loadData() {
        super.loadData();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        weaponRarity = sharedPreferences.getInt(WEAPON_RARITY, 3);
        editor.apply();
    }

    @Override
    public void updateViews() {
        super.updateViews();

        // Sets the title text to the save value.
        txtTypeTitle.setText(weaponRarity + "-Star Weapon");
    }

    @Override
    public void checkRequirements() {
//        int[][] reqMats;

        // Ensures the correct weapon rarity is being calculated.
        if (weaponRarity == 5) {
            reqMats = reqMats5Star;
        } else if (weaponRarity == 4) {
            reqMats = reqMats4Star;
        } else {
            reqMats = reqMats3Star;
        }

        super.checkRequirements();
    }
}
