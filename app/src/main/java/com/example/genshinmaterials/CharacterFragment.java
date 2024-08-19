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

public class CharacterFragment extends CounterFragment {

    // region For saving the data on app close.

    // Holds each EditText value on app closure. Note that the strings initialized here are to show the order, and are not saved.
    public static final String[] EDITTEXT_VALUES_0 = {"yellow_0_char", "purple_0_char", "blue_0_char", "green_0_char", "grey_0_char"};
    public static final String[] EDITTEXT_VALUES_1 = {"yellow_1_char", "purple_1_char", "blue_1_char", "green_1_char", "grey_1_char"};
    public static final String[] EDITTEXT_VALUES_2 = {"yellow_2_char", "purple_2_char", "blue_2_char", "green_2_char", "grey_2_char"};

    // Programmatically replaces the names of the subtabs.
    private static final String[] tabNamesArr = {"Gem", "Enemy", "Msc."};

    // The hardcoded amount of materials need to fully level up the weapon.
    // Each row is in descending rarity, mirroring allEditTexts.
    // Each column represents the corresponding indexed subtab. Ex: reqMats*Star[0] = "Domain", ...[1] = "Miniboss", ...[2] = "Enemy".
    private static int[][] reqMats = {{6, 9, 9, 1, 0},
            {0, 0, 36, 30, 18},
            {0, 46, 420000, 0, 168}};

    private TextView[] allDrwChecks;

    // Passes the final String array which names all EditTexts save data.
    CharacterFragment() {
        super(EDITTEXT_VALUES_0, EDITTEXT_VALUES_1, EDITTEXT_VALUES_2, tabNamesArr, reqMats);
    }
    // endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_counter, container, false);
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }
}
