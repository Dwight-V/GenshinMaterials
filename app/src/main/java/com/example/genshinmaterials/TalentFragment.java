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

public class TalentFragment extends CounterFragment {

    // region For saving the data on app close.

    // Holds each EditText value on app closure. Note that the strings initialized here are to show the order, and are not saved.
    public static final String[] EDITTEXT_VALUES_0 = {"yellow_0_talent", "purple_0_talent", "blue_0_talent", "green_0_talent", "grey_0_talent"};
    public static final String[] EDITTEXT_VALUES_1 = {"yellow_1_talent", "purple_1_talent", "blue_1_talent", "green_1_talent", "grey_1_talent"};
    public static final String[] EDITTEXT_VALUES_2 = {"yellow_2_talent", "purple_2_talent", "blue_2_talent", "green_2_talent", "grey_2_talent"};

    // Programmatically replaces the names of the subtabs.
    private static final String[] tabNamesArr = {"Books", "Boss", "Enemy"};

    // The hardcoded amount of materials need to fully level up the weapon.
    // Each row is in descending rarity, mirroring allEditTexts.
    // Each column represents the corresponding indexed subtab. Ex: reqMats*Star[0] = "Domain", ...[1] = "Miniboss", ...[2] = "Enemy".
    private static int[][] reqMats = {{0, 38, 21, 3, 0},
            {6, 0, 0, 0, 0},
            {0, 0, 31, 22, 6}};


    // Passes the final String array which names all EditTexts save data.
    TalentFragment() {
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
