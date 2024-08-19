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
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class CounterFragment extends Fragment {

    private EditText edtYellow, edtPurple, edtBlue, edtGreen, edtGrey;
    private TextView txtTemp;
    private TextView txtTemp2;
    protected TextView txtTypeTitle;
    private ImageButton btnClear;
    private Button btnAddYellow, btnSubYellow, btnAddPurple, btnSubPurple, btnAddBlue, btnSubBlue, btnAddGreen, btnSubGreen, btnAddGrey, btnSubGrey;

    private Switch swtEditable;

    protected TabLayout tabMaterials;






    // region For saving the data on app close.

    // Holds each EditText value on app closure. Note that the strings initialized here are to show the order, and are not saved.
//    public static final String[] EDITTEXT_VALUES_0 = new String[5];
//    public static final String[] EDITTEXT_VALUES_1 = new String[5];
//    public static final String[] EDITTEXT_VALUES_2 = new String[5];
    public static String[] EDITTEXT_VALUES_0;
    public static String[] EDITTEXT_VALUES_1;
    public static String[] EDITTEXT_VALUES_2;

    // When the user exits the fragment, saves which subtab was last selected.
    public static final String SUBTAB_POSITION = "subtab_pos_int";

    // TODO: Understand why loadData() and updateViews() are two different functions, when you usually call them one after another. If we combine them, we  don't need either of these local variables.
    // A 'local' variable, which on app load, is set to the values of EDITTEXT_VALUES, then used in updateViews to set the data.
    public String[] tabValArray0;
    public String[] tabValArray1;
    public String[] tabValArray2;

    // A 'local' variable, which on app load, is set to the value of SUBTAB_POSITION, then used in updateViews to set the data.
    public int prevSubtabPos;

    // Programmatically replaces the names of the subtabs.
    private static String[] tabNamesArr;

    // The hardcoded amount of materials need to fully level up the weapon.
    // Each row is in descending rarity, mirroring allEditTexts.
    // Each column represents the corresponding indexed subtab. Ex: reqMats*Star[0] = "Domain", ...[1] = "Miniboss", ...[2] = "Enemy".
    protected int[][] reqMats;

    // Represents whether or not the EditTexts can be edited by the user.
    // What I had previously was that I saved everytime any edit text was changed, but this interferes with using the subtabs to change the values of the edittexts
    // (as changing the values progamatically like that calls the onTextChange event, which would then save the local array values to the switched-from tab's values).
    // Basically, tab 1 is selected, then when you click tab 2, the code starts changing the values of the EditTexts to match tab 2's, but then because I changed edtYellow's values,
    // afterTextChanged() is called, saving tab 2's values to the rest of tab 1's values (since it didn't have time to change all of them).
    // So edtYellow always works (because it's the only call that triggers before the saveData()), But all others don't.
    private boolean edittextsAreReady = true;

    // Holds shallow copies (pointers) to all EditTexts. Used in loops instead of calling all EditTexts.
    private EditText[] allEditTexts;
    private TextView[] allDrwChecks;
    private View[] allViews;
    // endregion

    CounterFragment (String[] edittextValuesArray0, String[] edittextValuesArray1, String[] edittextValuesArray2, String[] tabsName, int[][] req) {
        EDITTEXT_VALUES_0 = edittextValuesArray0;
        EDITTEXT_VALUES_1 = edittextValuesArray1;
        EDITTEXT_VALUES_2 = edittextValuesArray2;
        tabNamesArr = tabsName;
        reqMats = req;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);
        txtTemp = (TextView) view.findViewById(R.id.text_temp);
        txtTemp2 = (TextView) view.findViewById(R.id.text_temp2);
        txtTypeTitle = (TextView) view.findViewById(R.id.text_type_title);

        View counter_yellow = view.findViewById(R.id.counter_yellow);
        View counter_purple = view.findViewById(R.id.counter_purple);
        View counter_blue = view.findViewById(R.id.counter_blue);
        View counter_green = view.findViewById(R.id.counter_green);
        View counter_grey = view.findViewById(R.id.counter_grey);

        edtYellow = (EditText) counter_yellow.findViewById(R.id.edittext_main);
        edtPurple = (EditText) counter_purple.findViewById(R.id.edittext_main);
        edtBlue = (EditText) counter_blue.findViewById(R.id.edittext_main);
        edtGreen = (EditText) counter_green.findViewById(R.id.edittext_main);
        edtGrey = (EditText) counter_grey.findViewById(R.id.edittext_main);

        btnClear = (ImageButton) view.findViewById(R.id.button_clear);
        btnAddYellow = (Button) counter_yellow.findViewById(R.id.button_add);
        btnSubYellow = (Button) counter_yellow.findViewById(R.id.button_sub);
        btnAddPurple = (Button) counter_purple.findViewById(R.id.button_add);
        btnSubPurple = (Button) counter_purple.findViewById(R.id.button_sub);
        btnAddBlue = (Button) counter_blue.findViewById(R.id.button_add);
        btnSubBlue = (Button) counter_blue.findViewById(R.id.button_sub);
        btnAddGreen = (Button) counter_green.findViewById(R.id.button_add);
        btnSubGreen = (Button) counter_green.findViewById(R.id.button_sub);
        btnAddGrey = (Button) counter_grey.findViewById(R.id.button_add);
        btnSubGrey = (Button) counter_grey.findViewById(R.id.button_sub);

        // https://stackoverflow.com/a/36139523
        swtEditable = (Switch) getActivity().findViewById(R.id.switch_editable_full);

        tabMaterials = (TabLayout) view.findViewById(R.id.tab_layout_materials);

        allEditTexts = new EditText[] {edtYellow, edtPurple, edtBlue, edtGreen, edtGrey};
        allDrwChecks = new TextView[] {counter_yellow.findViewById(R.id.drawable_check),
                counter_purple.findViewById(R.id.drawable_check),
                counter_blue.findViewById(R.id.drawable_check),
                counter_green.findViewById(R.id.drawable_check),
                counter_grey.findViewById(R.id.drawable_check)};
        allViews = new View[] {counter_yellow, counter_purple, counter_blue, counter_green, counter_grey};




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
//                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittextsAreReady) {
                    if (thisEditText.getText().toString().equals("")) {
                        thisEditText.setText("0");
                    }
                    checkOverflow(thisEditText);
                    saveData();
                    checkRequirements();
                }
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
//                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittextsAreReady) {
                    if (thisEditText.getText().toString().equals("")) {
                        thisEditText.setText("0");
                    }
                    checkOverflow(thisEditText);
                    saveData();
                    checkRequirements();
                }
            }
        });

        edtBlue.addTextChangedListener(new TextWatcher() {
            EditText thisEditText = edtBlue;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittextsAreReady) {
                    if (thisEditText.getText().toString().equals("")) {
                        thisEditText.setText("0");
                    }
                    checkOverflow(thisEditText);
                    saveData();
                    checkRequirements();
                }
            }
        });


        edtGreen.addTextChangedListener(new TextWatcher() {
            EditText thisEditText = edtGreen;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittextsAreReady) {
                    if (thisEditText.getText().toString().equals("")) {
                        thisEditText.setText("0");
                    }
                    checkOverflow(thisEditText);
                    saveData();
                    checkRequirements();
                }
            }
        });

        edtGrey.addTextChangedListener(new TextWatcher() {
            EditText thisEditText = edtGrey;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                saveData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edittextsAreReady) {
                    if (thisEditText.getText().toString().equals("")) {
                        thisEditText.setText("0");
                    }
                    checkOverflow(thisEditText);
                    saveData();
                    checkRequirements();
                }
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

        tabMaterials.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
                updateEdittextVals();
                checkRequirements();
                disableLayouts();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Loads data and displays saved data on app launch.
        loadData();
        updateViews();
        checkRequirements();
        disableLayouts();
        return view;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        Toast.makeText(getActivity(), "saveData() " + tabMaterials.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();
        switch (tabMaterials.getSelectedTabPosition()) {
            // Saves all editText UI inputs into their respective array
            case 2:
                for (int i = 0; i < EDITTEXT_VALUES_2.length; i++) {
                    // Saves the values for the long-term (on app restart)
                    editor.putString(EDITTEXT_VALUES_2[i], allEditTexts[i].getText().toString());
                    // Saves the values for the short-term (switching subtabs)
                    tabValArray2[i] = allEditTexts[i].getText().toString();
                }
                break;
            case 1:
                for (int i = 0; i < EDITTEXT_VALUES_1.length; i++) {
                    // Saves the values for the long-term (on app restart)
                    editor.putString(EDITTEXT_VALUES_1[i], allEditTexts[i].getText().toString());
                    // Saves the values for the short-term (switching subtabs)
                    tabValArray1[i] = allEditTexts[i].getText().toString();
                }
                break;
            default:
                for (int i = 0; i < EDITTEXT_VALUES_0.length; i++) {
                    // Saves the values for the long-term (on app restart)
                    editor.putString(EDITTEXT_VALUES_0[i], allEditTexts[i].getText().toString());
                    // Saves the values for the short-term (switching subtabs)
                    tabValArray0[i] = allEditTexts[i].getText().toString();
                }
                break;
        }

//        editor.putBoolean(SWITCH_EDITABLE_IS_CHECKED, swtEditable.isChecked());
//        Toast.makeText(this, VALUE_BLUE + " " + edtBlue.getText().toString(), Toast.LENGTH_SHORT).show();
        editor.putInt(SUBTAB_POSITION, tabMaterials.getSelectedTabPosition());

        editor.apply();
//        txtTemp.setText(sharedPreferences.getAll().toString());
//        txtTemp2.setText("0:" + Arrays.toString(tabValArray0) + "\n" + "1:" + Arrays.toString(tabValArray1) + "\n" + "2:" + Arrays.toString(tabValArray2));
    }

    public void loadData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        tabValArray0 = new String[5];
        tabValArray1 = new String[5];
        tabValArray2 = new String[5];

        prevSubtabPos = sharedPreferences.getInt(SUBTAB_POSITION, 0);

        // Sets the new initialized local arrays to the saved instance of the arrays.
        for (int i = 0; i < EDITTEXT_VALUES_2.length; i++) {
            tabValArray2[i] = sharedPreferences.getString(EDITTEXT_VALUES_2[i], "0");
        }
        for (int i = 0; i < EDITTEXT_VALUES_1.length; i++) {
            tabValArray1[i] = sharedPreferences.getString(EDITTEXT_VALUES_1[i], "0");
        }
        for (int i = 0; i < EDITTEXT_VALUES_0.length; i++) {
            tabValArray0[i] = sharedPreferences.getString(EDITTEXT_VALUES_0[i], "0");
        }
    }

    // Changes the values of the EditTexts and Switch to saved values.
    public void updateViews() {
        // Sets the last used subtab.
        tabMaterials.selectTab(tabMaterials.getTabAt(prevSubtabPos));
        // Sets the editable switch to last used position.
//        swtEditable.setChecked(editableIsChecked);
        changeEditable();
        // Updates the EditTexts (Yellow - Grey) to display the last used tab data before shutdown.
        updateEdittextVals();
      
        for (int i = 0; i < tabMaterials.getTabCount(); i++) {
            tabMaterials.getTabAt(i).setText(tabNamesArr[i]);
        }

        edtYellow.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_yellow));
        edtPurple.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_purple));
        edtBlue.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_blue));
        edtGreen.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_green));
        edtGrey.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_grey));
//        getView().findViewById(R.id.linearlayout_icons).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_yellow));
//        edtPurple.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_purple));
//        edtBlue.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_blue));
//        edtGreen.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_green));
//        edtGrey.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.genshin_grey));
    }

    // Reads swtEditable's state, and locks or unlocks editablitiy on all EditTexts depending on the state.
    public void changeEditable() {
//        Toast.makeText(getContext(), swtEditable.isChecked() + "", Toast.LENGTH_SHORT).show();
        if (swtEditable.isChecked()) {
            for (int i = 0; i < allEditTexts.length; i++) {
                allEditTexts[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        } else {
            // From: https://stackoverflow.com/questions/1109022/how-can-i-close-hide-the-android-soft-keyboard-programmatically
            if (requireActivity().getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
            }

            for (int i = 0; i < allEditTexts.length; i++) {
                allEditTexts[i].setInputType(InputType.TYPE_NULL);
            }
        }
    }

    // Sets the correct values of the EditTexts (based on current subtab position).
    public void updateEdittextVals() {
        // Even though afterTextChanged() is called, this flag ensures that no saveData() call is made before all the EditTexts are changed programmatically here.
        edittextsAreReady = false;

//        Toast.makeText(getActivity(), "updateEdittextVals() " + tabMaterials.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();
        switch (tabMaterials.getSelectedTabPosition()) {
            case 2:
                for (int i = 0; i < allEditTexts.length; i++) {
                    allEditTexts[i].setText(tabValArray2[i]);
                }
                break;
            case 1:
                for (int i = 0; i < allEditTexts.length; i++) {
                    allEditTexts[i].setText(tabValArray1[i]);
                }
                break;
            default:
                for (int i = 0; i < allEditTexts.length; i++) {
                    allEditTexts[i].setText(tabValArray0[i]);
                }
                break;
        }

        edittextsAreReady = true;
        saveData();
    }

    // App crashes when numbers are absurdly large. IMO 10000 of one resource is a plenty high ceiling.
    public void checkOverflow(EditText editText) {
        if (Integer.parseInt(editText.getText().toString()) > 10000) {
            editText.setText("10000");
        }
    }

    // Houses the 'brain' of the code. Compares the current amounts to the pre-set amount, and displays a checkmark of met/exceeds.
    // TODO: Maybe make it modular? As in, take an argument (the EditText which it's called from) and check if it's value meets the requirements.
    public void checkRequirements() {
        // Holds the total amount of mats the user input. If there are excess materials in a lower-rarity, will convert them to the next rarity higher.
        // Is essentially a deep-copy of allEditTexts[].
        int[] netTotalMats = new int[5];
        // subtabIndex should never be < 0 since logic is handled by TabLayout itself. Otherwise should check.
        int subtabIndex = tabMaterials.getSelectedTabPosition();
        int extraMats = 0;

//        txtTemp.setText("reqMats[" + subtabIndex + "]: " + Arrays.toString(reqMats[subtabIndex]));
        // Compares each EditText with it's specific amount. Displays the check if >=, otherwise removes it.
        // Descends since we can add unused materials to the next slot.
        for (int i = allEditTexts.length - 1; i >= 0; i--) {
            int reqAmount = reqMats[subtabIndex][i];

            netTotalMats[i] = Integer.parseInt(allEditTexts[i].getText().toString()) + extraMats;

            if (netTotalMats[i] >= reqAmount) {
                // Integer division rounds down.
                // If not inside this if statement, extraMats can go negative.
                extraMats = (netTotalMats[i] - reqAmount) / 3;
                allDrwChecks[i].setVisibility(View.VISIBLE);
            } else {
                allDrwChecks[i].setVisibility(View.INVISIBLE);
                extraMats = 0;
            }
        }
    }

    public void disableLayouts() {
        int subtabIndex = tabMaterials.getSelectedTabPosition();

        for (int i = 0; i < allEditTexts.length; i++) {
            if (reqMats[subtabIndex][i] <= 0) {
                allViews[i].setVisibility(View.GONE);
            } else {
                allViews[i].setVisibility(View.VISIBLE);
            }
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