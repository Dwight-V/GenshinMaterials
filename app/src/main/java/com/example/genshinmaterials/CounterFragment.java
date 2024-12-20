package com.example.genshinmaterials;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class CounterFragment extends Fragment {
    // region Views
    private EditText edtYellow, edtPurple, edtBlue, edtGreen, edtGrey;
    protected TextView txtStatic, txtDynamic;

    protected EditText edtTitle;
    private ImageButton btnClear;
    private Button btnAddYellow, btnSubYellow, btnAddPurple, btnSubPurple, btnAddBlue, btnSubBlue, btnAddGreen, btnSubGreen, btnAddGrey, btnSubGrey;

    private Switch swtEditable;

    protected TabLayout tabMaterials;

    private LinearLayout linLayStars;

    private Spinner spnStartLvl, spnEndLvl;


    protected View counterObjYellow, counterObjPurple, counterObjBlue, counterObjGreen, counterObjGrey;

    protected ImageView imgViewIconYellow, imgViewIconPurple, imgViewIconBlue, imgViewIconGreen, imgViewIconGrey;
    protected ProgressBar prgssbarMaterial;
    // endregion


    // region For saving the data on app close.

    // Holds each EditText value on app closure. Note that the strings initialized here are to show the order, and are not saved.
    public static String[][] EDITTEXT_VALUES = new String[3][];

    // When the user exits the fragment, saves which subtab was last selected.
    public static String SUBTAB_POSITION;

    public static String ITEM_RARITY;

    private static String TEXTVIEW_STATIC;

    private static String EDITTEXT_TITLE;

    // Represents the saved values of spnStartLvl and spnEndLvl respectively.
    private static String START_LEVEL_INDEX;
    private static String END_LEVEL_INDEX;
    // endregion


    // region Global variables that hold the above variable's saved data while the app is open.

    public int[][] tabValArray = new int[3][];

    public int prevSubtabPos;

    protected int itemRarity = 3;

    // Used because I can't figure out how to only trigger edtTitle's onChangeListener after instantiating.
    private boolean afterOnCreate = false;

    private String titleText;
    private String staticText;

    // Assigned from the constructor:
    // Defines the min amount the spinner can show
    private static int INT_LEVEL_MIN;
    // Defines the max amount the spinner can show
    private static int INT_LEVEL_MAX;
    // Defines how createLevelSpinnerAdapter() steps for each item in the spinner.
    private static int INT_LEVEL_STEP;

    private int levelStartIndex;
    private int levelEndIndex;
    // endregion


    // region Variables that are instantiated locally for each subclass.

    // Programmatically replaces the names of the subtabs.
    private static String[] tabNamesArr;

    // The hardcoded amount of materials need to fully level up the weapon.
    // Each row is in descending rarity, mirroring allEditTexts.
    // Each column represents the corresponding indexed subtab. Ex: reqMats[0] = "Domain", ...[1] = "Miniboss", ...[2] = "Enemy".
    protected int[][] reqMats;
    protected int[][][] allMats;
    // endregion

    // Represents whether or not the EditTexts can be edited by the user.
    // What I had previously was that I saved everytime any edit text was changed, but this interferes with using the subtabs to change the values of the edittexts
    // (as changing the values progamatically like that calls the onTextChange event, which would then save the local array values to the switched-from tab's values).
    // Basically, tab 1 is selected, then when you click tab 2, the code starts changing the values of the EditTexts to match tab 2's, but then because I changed edtYellow's values,
    // afterTextChanged() is called, saving tab 2's values to the rest of tab 1's values (since it didn't have time to change all of them).
    // So edtYellow always works (because it's the only call that triggers before the saveData()), But all others don't.
    private boolean edittextsAreReady = true;

    // Holds shallow copies (pointers) to their respective data types. Used in loops for easy indexing.
    protected EditText[] allEditTexts;
    protected ImageView[] allDrwChecks;
    private View[] allCounterObjs;
    protected ImageView[] allImgViewIcons;

    private ImageView[] allStars;


    CounterFragment (String[] edittextValuesArray0, String[] edittextValuesArray1, String[] edittextValuesArray2, String[] tabsName,
                     int[][][] mats, String subtabPos, String itemRare, String txtStaticText, String title, int levelMin, int levelMax, int levelStep, String startLevel, String endLevel) {
        EDITTEXT_VALUES[0] = edittextValuesArray0;
        EDITTEXT_VALUES[1] = edittextValuesArray1;
        EDITTEXT_VALUES[2] = edittextValuesArray2;
        tabNamesArr = tabsName;
        allMats = mats;
        SUBTAB_POSITION = subtabPos;
        ITEM_RARITY = itemRare;
        TEXTVIEW_STATIC = txtStaticText;
        EDITTEXT_TITLE = title;
        INT_LEVEL_MIN = levelMin;
        INT_LEVEL_MAX = levelMax;
        INT_LEVEL_STEP = levelStep;
        START_LEVEL_INDEX = startLevel;
        END_LEVEL_INDEX = endLevel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);
        txtDynamic = (TextView) view.findViewById(R.id.textview_dynamic_text);
        txtStatic = (TextView) view.findViewById(R.id.textview_static_text);
        edtTitle = (EditText) view.findViewById(R.id.edittext_title);

        btnClear = (ImageButton) view.findViewById(R.id.button_clear);

        counterObjYellow = view.findViewById(R.id.counter_yellow);
        counterObjPurple = view.findViewById(R.id.counter_purple);
        counterObjBlue = view.findViewById(R.id.counter_blue);
        counterObjGreen = view.findViewById(R.id.counter_green);
        counterObjGrey = view.findViewById(R.id.counter_grey);

        edtYellow = (EditText) counterObjYellow.findViewById(R.id.edittext_main);
        edtPurple = (EditText) counterObjPurple.findViewById(R.id.edittext_main);
        edtBlue = (EditText) counterObjBlue.findViewById(R.id.edittext_main);
        edtGreen = (EditText) counterObjGreen.findViewById(R.id.edittext_main);
        edtGrey = (EditText) counterObjGrey.findViewById(R.id.edittext_main);

        btnAddYellow = (Button) counterObjYellow.findViewById(R.id.button_add);
        btnSubYellow = (Button) counterObjYellow.findViewById(R.id.button_sub);
        btnAddPurple = (Button) counterObjPurple.findViewById(R.id.button_add);
        btnSubPurple = (Button) counterObjPurple.findViewById(R.id.button_sub);
        btnAddBlue = (Button) counterObjBlue.findViewById(R.id.button_add);
        btnSubBlue = (Button) counterObjBlue.findViewById(R.id.button_sub);
        btnAddGreen = (Button) counterObjGreen.findViewById(R.id.button_add);
        btnSubGreen = (Button) counterObjGreen.findViewById(R.id.button_sub);
        btnAddGrey = (Button) counterObjGrey.findViewById(R.id.button_add);
        btnSubGrey = (Button) counterObjGrey.findViewById(R.id.button_sub);

        imgViewIconYellow = counterObjYellow.findViewById(R.id.imageview_counter_icon);
        imgViewIconPurple = counterObjPurple.findViewById(R.id.imageview_counter_icon);
        imgViewIconBlue = counterObjBlue.findViewById(R.id.imageview_counter_icon);
        imgViewIconGreen = counterObjGreen.findViewById(R.id.imageview_counter_icon);
        imgViewIconGrey = counterObjGrey.findViewById(R.id.imageview_counter_icon);

        prgssbarMaterial = (ProgressBar) view.findViewById(R.id.progressbar_materials);

        // https://stackoverflow.com/a/36139523
        swtEditable = (Switch) getActivity().findViewById(R.id.switch_editable_full);

        spnStartLvl = (Spinner) view.findViewById(R.id.spinner_start_level);
        spnEndLvl = (Spinner) view.findViewById(R.id.spinner_end_level);

        tabMaterials = (TabLayout) view.findViewById(R.id.tab_layout_materials);

        linLayStars = (LinearLayout) view.findViewById(R.id.linearlayout_stars);

        allEditTexts = new EditText[] {edtYellow, edtPurple, edtBlue, edtGreen, edtGrey};
        allDrwChecks = new ImageView[] {counterObjYellow.findViewById(R.id.drawable_check),
                counterObjPurple.findViewById(R.id.drawable_check),
                counterObjBlue.findViewById(R.id.drawable_check),
                counterObjGreen.findViewById(R.id.drawable_check),
                counterObjGrey.findViewById(R.id.drawable_check)};
        allCounterObjs = new View[] {counterObjYellow, counterObjPurple, counterObjBlue, counterObjGreen, counterObjGrey};
        allStars = new ImageView[] {
                linLayStars.findViewById(R.id.imageview_star_0),
                linLayStars.findViewById(R.id.imageview_star_1),
                linLayStars.findViewById(R.id.imageview_star_2),
                linLayStars.findViewById(R.id.imageview_star_3),
                linLayStars.findViewById(R.id.imageview_star_4)};

        allImgViewIcons = new ImageView[] {imgViewIconYellow, imgViewIconPurple, imgViewIconBlue, imgViewIconGreen, imgViewIconGrey};




        // region  add/sub buttonClickListeners

        btnAddYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(0);
            }
        });

        btnSubYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(0);
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

                edtTitle.setText("");
                spnStartLvl.setSelection(0);
                spnEndLvl.setSelection(spnEndLvl.getCount() - 1);
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
                add(1);
            }
        });

        btnSubPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(1);
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
                add(2);
            }
        });

        btnSubBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(2);
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
                add(3);
            }
        });

        btnSubGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(3);
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
                add(4);
            }
        });

        btnSubGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(4);
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

        edtYellow.addTextChangedListener(new TextWatcherWithEditText(0));

        edtPurple.addTextChangedListener(new TextWatcherWithEditText(1));

        edtBlue.addTextChangedListener(new TextWatcherWithEditText(2));

        edtGreen.addTextChangedListener(new TextWatcherWithEditText(3));

        edtGrey.addTextChangedListener(new TextWatcherWithEditText(4));

        // endregion


        // region Miscellaneous Listeners

        // TODO: If swtEditable is checked, app exit, app exit, swtEditable will no longer be checked.
        swtEditable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                disableEditText(edtYellow);
//                Toast.makeText(MainActivity.this, "changed!", Toast.LENGTH_SHORT).show();
                updateEditability();
                saveData();
            }
        });

        tabMaterials.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
                updateCounters();
                updateCounterUi();
                checkRequirements();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        linLayStars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swtEditable.isChecked()) {
//                Toast.makeText(getContext(), "Stars clicked!", Toast.LENGTH_SHORT).show();
                    // Cycles which rarity of weapon to calc for.
                    switch (itemRarity) {
                        case 3:
//                        txtTitle.setText("4-Star Weapon");
                            itemRarity = 4;
                            break;
                        case 4:
//                        txtTitle.setText("5-Star Weapon");
                            itemRarity = 5;
                            break;
                        default:
//                        txtTitle.setText("3-Star Weapon");
                            itemRarity = 3;
                            break;
                    }
                    saveData();
                    updateCounters();
                    updateStarRarity();
                    checkRequirements();
                }
            }
        });

        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (afterOnCreate) {
                    saveData();
//                    Toast.makeText(CounterFragment.this.getContext(), TEXTVIEW_STATIC, Toast.LENGTH_SHORT).show();
                }
            }
        });

        spnStartLvl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Compares the would-be selected value of spnStartLvl with the selected level of spnEndLvl. If spnEndLvl is lower, change the spnEndLvl value to one higher than spnEndLvl.
                if (Integer.parseInt((String) spnStartLvl.getAdapter().getItem(position)) >= Integer.parseInt((String) spnEndLvl.getSelectedItem())) {
                    spnEndLvl.setSelection(spnStartLvl.getSelectedItemPosition()); // Doesn't need (getSelectedItemPosition - 1) since spnStartLvl is offset by 1 leveStep.
                }
                updateCounters();
                checkRequirements();
                saveData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnEndLvl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Compares the would-be selected value of spnEndLvl with the selected level of spnStartLvl. If spnEndLvl is lower, change the spnStartLvl value to one lower than spnEndLvl.
                if (Integer.parseInt((String) spnStartLvl.getSelectedItem()) >= Integer.parseInt((String) spnEndLvl.getAdapter().getItem(position))) {
                    spnStartLvl.setSelection(spnEndLvl.getSelectedItemPosition()); // Doesn't need (getSelectedItemPosition - 1) since spnStartLvl is offset by 1 leveStep.
                }
                updateCounters();
                checkRequirements();
                saveData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // endregion



        // main
        spnStartLvl.setAdapter(createLevelSpinnerAdapter(INT_LEVEL_MIN, INT_LEVEL_MAX - INT_LEVEL_STEP));
        spnEndLvl.setAdapter(createLevelSpinnerAdapter(INT_LEVEL_MIN + INT_LEVEL_STEP, INT_LEVEL_MAX));

        loadData(); // Loads data on app launch.
        updateMainTabScreen(); // Sets up the screen with the loaded data.
        checkRequirements(); // Runs the check to see if there's enough materials.

        afterOnCreate = true;
        return view;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int indexTab = tabMaterials.getSelectedTabPosition();

        for (int i = 0; i < EDITTEXT_VALUES[indexTab].length; i++) {
            // Saves the values for the long-term (on app restart)
            editor.putInt(EDITTEXT_VALUES[indexTab][i], tabValArray[indexTab][i]);
        }

        editor.putInt(ITEM_RARITY, itemRarity);
        editor.putInt(SUBTAB_POSITION, tabMaterials.getSelectedTabPosition());
        editor.putString(EDITTEXT_TITLE, edtTitle.getText().toString());
        editor.putInt(START_LEVEL_INDEX, spnStartLvl.getSelectedItemPosition());
        editor.putInt(END_LEVEL_INDEX, spnEndLvl.getSelectedItemPosition());


        editor.apply();
//        Log.i("MSG Save", sharedPreferences.getAll().toString());
    }

    private void loadData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        tabValArray[0] = new int[5];
        tabValArray[1] = new int[5];
        tabValArray[2] = new int[5];

        prevSubtabPos = sharedPreferences.getInt(SUBTAB_POSITION, 0);
        itemRarity = sharedPreferences.getInt(ITEM_RARITY, 3);

        // Sets the new initialized local arrays to the saved instance of the arrays.
        for (int i = 0; i < EDITTEXT_VALUES.length; i++) {
            for (int j = 0; j < EDITTEXT_VALUES[i].length; j++) {
                tabValArray[i][j] = sharedPreferences.getInt(EDITTEXT_VALUES[i][j], 0);
            }
        }

        staticText = TEXTVIEW_STATIC;
        titleText = sharedPreferences.getString(EDITTEXT_TITLE, "");
        levelStartIndex = sharedPreferences.getInt(START_LEVEL_INDEX, 0);
        levelEndIndex = sharedPreferences.getInt(END_LEVEL_INDEX, spnEndLvl.getCount() - 1);

//        Log.i("MSG Load", sharedPreferences.getAll().toString());
    }

    // Changes the values of the EditTexts and Switch to saved values.
    private void updateMainTabScreen() {
        tabMaterials.selectTab(tabMaterials.getTabAt(prevSubtabPos)); // Sets the last used subtab.
        updateEditability(); // Sets the editable switch to last used position.
        updateCounters(); // Updates the EditTexts (Yellow - Grey) to display the last used tab data before shutdown.

        // Sets the display for the tab names at the bottom.
        for (int i = 0; i < tabMaterials.getTabCount(); i++) {
            tabMaterials.getTabAt(i).setText(tabNamesArr[i]);
        }

        edtTitle.setText(titleText);
        txtStatic.setText(staticText);
        spnStartLvl.setSelection(levelStartIndex);
        spnEndLvl.setSelection(levelEndIndex);
        
        updateCounterUi();
        updateStarRarity();
    }

    // Reads swtEditable's state, and locks or unlocks editablitiy on all EditTexts depending on the state.
    private void updateEditability() {
//        Toast.makeText(getContext(), swtEditable.isChecked() + "", Toast.LENGTH_SHORT).show();
        if (swtEditable.isChecked()) {
            for (int i = 0; i < allEditTexts.length; i++) {
                allEditTexts[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            }

//            edtTitle.setInputType(InputType.TYPE_CLASS_TEXT);
            edtTitle.setEnabled(true);
            spnStartLvl.setEnabled(true);
            spnEndLvl.setEnabled(true);
        } else {
            // From: https://stackoverflow.com/questions/1109022/how-can-i-close-hide-the-android-soft-keyboard-programmatically
            if (requireActivity().getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
            }

            for (int i = 0; i < allEditTexts.length; i++) {
                allEditTexts[i].setInputType(InputType.TYPE_NULL);
            }

            edtTitle.setInputType(InputType.TYPE_NULL);
            spnStartLvl.setEnabled(false);
            spnEndLvl.setEnabled(false);
            edtTitle.setEnabled(false);
        }
    }

    // Sets the correct values of the counters (based on current subtab position).
    protected void updateCounters() {
        int tabPos = tabMaterials.getSelectedTabPosition();
        // Even though afterTextChanged() is called, this flag ensures that no saveData() call is made before all the EditTexts are changed programmatically here.
        edittextsAreReady = false;
        
        updateReqMatsToLevel(); // updates reqMats to the desired start -> end level.

        for (int i = 0; i < allEditTexts.length; i++) {
            allEditTexts[i].setText(String.valueOf(tabValArray[tabPos][i])); // Sets the numerator of the counter.
            TextView temp = allCounterObjs[i].findViewById(R.id.txtDenominator);
            temp.setText(String.valueOf(reqMats[tabPos][i])); // Sets the denominator of the counter.
        }

        updateStaticMaterials(); // Changes the description below the counters to match the start -> end level amounts.
        updateCounterVisibility();

        edittextsAreReady = true;
        saveData();
    }

    // Only used in updateCounters(). Updates reqMats to match the levels.
    private void updateReqMatsToLevel() {
        int j = spnStartLvl.getSelectedItemPosition();

        reqMats = new int[allMats[j].length][];

        // Re-initializes reqMats to the first level selected
//        reqMats = allMats[j]; Can't do this, as it's a shallow copy.
        for (int k = 0; k < allMats[j].length; k++) {
            reqMats[k] = new int[allMats[j][k].length];
            Log.i("updateReqMats instance", "Row " + k);
            for (int l = 0; l < allMats[j][k].length; l++) {
                Log.i("updateReqMats instance", String.format("reqMats[%2$d][%3$d]: %4$d allMats[%1$d][%2$d][%3$d]: %5$d", j, k, l, reqMats[k][l], allMats[j][k][l]));
                reqMats[k][l] = allMats[j][k][l];
            }
        }

        // Note: getSelectedItemPos < spnEndLvl.getCount()
        // This loop will iterate through the first level of allMats, i.e. which levels are being added.
        for (int i = j; i < spnEndLvl.getSelectedItemPosition(); i++) {
            // This loop will iterate through the second level, i.e. which materials.
            for (int k = 0; k < allMats[i + 1].length; k++) {
                // This loop will iterate through the last level, i.e. the individual values.
                for (int l = 0; l < allMats[i + 1][k].length; l++) {
                    Log.i("updateReqMats add", String.format("reqMats[%2$d][%3$d]: %4$d allMats[%1$d][%2$d][%3$d]: %5$d", i + 1, k, l, reqMats[k][l], allMats[i][k][l]));
                    reqMats[k][l] += allMats[i + 1][k][l];
                }
            }
        }
        Log.i("updateReqMatsToLevel", Arrays.toString(reqMats[0]));
//        Toast.makeText(getContext(), String.format("spnStart: %d spnEnd: %d", spnStartLvl.getSelectedItemPosition(), spnEndLvl.getSelectedItemPosition()), Toast.LENGTH_SHORT).show();
    }

    // Only used in updateCounters(). Updates the static text on the screen.
    private void updateStaticMaterials() {
        String ret = "";

        for (int val : reqMats[reqMats.length - 1]) {
            ret += String.format("x%,d \n", val);
        }

        txtDynamic.setText(ret);
    }

    // Only used in updateCounters(). If any counter on screen has 0 as a denominator, remove it from view.
    private void updateCounterVisibility() {
        int subtabIndex = tabMaterials.getSelectedTabPosition();

        for (int i = 0; i < allEditTexts.length; i++) {
            if (reqMats[subtabIndex][i] <= 0) {
                allCounterObjs[i].setVisibility(View.GONE);
            } else {
                allCounterObjs[i].setVisibility(View.VISIBLE);
            }
        }

    }
    
    // Sets the color of each counter. Is overridden to update the icon as well.
    protected void updateCounterUi() {
        int[] colors = {R.color.genshin_yellow, R.color.genshin_purple,  R.color.genshin_blue, R.color.genshin_green, R.color.genshin_grey};

        for (int i = 0; i < allCounterObjs.length; i++) {
            allCounterObjs[i].findViewById(R.id.linearlayout_icons).setBackgroundColor(ContextCompat.getColor(getActivity(), colors[i]));
        }
    }

    // Houses the 'brain' of the code. Compares the current amounts to the pre-set amount, and displays a checkmark of met/exceeds.
    // TODO: Maybe make it modular? As in, take an argument (the EditText which it's called from) and check if it's value meets the requirements.
    protected void checkRequirements() {
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

            netTotalMats[i] = tabValArray[subtabIndex][i] + extraMats;

            if (netTotalMats[i] >= reqAmount) {
                // Integer division rounds down.
                // If not inside this if statement, extraMats can go negative.
                extraMats = (netTotalMats[i] - reqAmount) / 3;
                // This prevents netTotalMats[i] from being >1, so the progressbar doesn't go over 100%.
                netTotalMats[i] = reqAmount;
//                allDrwChecks[i].setVisibility(View.VISIBLE);
                // TODO: Make this a method that toggles between the two, or give it an argument for on/off.
                // https://stackoverflow.com/a/20121975
                allDrwChecks[i].setColorFilter(ContextCompat.getColor(getActivity(), R.color.check_green));
            } else {
//                allDrwChecks[i].setVisibility(View.INVISIBLE);
                allDrwChecks[i].setColorFilter(ContextCompat.getColor(getActivity(), R.color.check_grey));
                extraMats = 0;
            }
        }

        // Updates the progression bar
        prgssbarMaterial.setMax(Arrays.stream(reqMats[subtabIndex]).sum());
        prgssbarMaterial.setProgress(Arrays.stream(netTotalMats).sum(), true);
//        txtStatic.setText(prgssbarMaterial.getProgress() + "/" + prgssbarMaterial.getMax());
    }
    
    // region *** Miscellaneous functions
    protected void updateStarRarity() {
        // Resets the number of stars initially
        for (int i = 0; i < allStars.length; i++) {
            allStars[i].setImageResource(R.drawable.round_star_border_24);
        }

        // Then adds the correct number back.
        for (int i = 0; i < itemRarity; i++) {
            // Just in case itemRarity is out of bounds.
            if (i < allStars.length) {
                allStars[i].setImageResource(R.drawable.round_star_24);
            }
        }
    }

    // Creates an ArrayAdapter with String values of [levelStart, levelEnd] with a step of INT_LEVEL_STEP.
    private ArrayAdapter createLevelSpinnerAdapter(int levelStart, int levelEnd) {
        levelStart = Math.max(levelStart, 0); // ensures >= 0.

        String[] levels = new String[((levelEnd - levelStart) / INT_LEVEL_STEP) + 1];

        for (int i = 0; i < levels.length; i++) {
            levels[i] = String.valueOf(levelStart);
            levelStart += INT_LEVEL_STEP;
        }

        // from: https://stackoverflow.com/a/28848747
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    // endregion Miscellaneous
    
    // region *** Logical operators
    private void add(int editTextIndex) {
        if (editTextIndex >= allEditTexts.length || editTextIndex < 0) {
            Toast.makeText(getContext(), "editTextIndex out of bounds!",  Toast.LENGTH_SHORT).show();
            return;
        }

        EditText edtCur = allEditTexts[editTextIndex];
        int tabIndex = tabMaterials.getSelectedTabPosition();

        edtCur.setText(String.valueOf(++tabValArray[tabIndex][editTextIndex]));
        // Don't need to call saveData() as changing the EditText's text does already
    }

    private void sub(int editTextIndex) {
        if (editTextIndex >= allEditTexts.length || editTextIndex < 0) {
            Toast.makeText(getContext(), "editTextIndex out of bounds!",  Toast.LENGTH_SHORT).show();
            return;
        }

        EditText edtCur = allEditTexts[editTextIndex];
        int tabIndex = tabMaterials.getSelectedTabPosition();

        if (tabValArray[tabIndex][editTextIndex] - 1 >= 0) {
            edtCur.setText(String.valueOf(--tabValArray[tabIndex][editTextIndex]));
        }
        // Don't need to call saveData() as changing the EditText's text does already
    }

    // App crashes when numbers are absurdly large. IMO 10000 of one resource is a plenty high ceiling.
    private void checkOverflow(EditText editText) {
        if (!editText.getText().toString().isEmpty() && Integer.parseInt(editText.getText().toString()) > 10000) {
            editText.setText("10000");
        }
    }
    // endregion Logical operators

    // region *** API calling for images
    protected void updateCounterIcon(ImageView imgView, String imageUrl) {
//            txtStatic.setText(imageUrl);

        // Use Glide to replace image (https://github.com/bumptech/glide?tab=readme-ov-file#how-do-i-use-glide)
        Glide
                .with(getContext())
                .load(imageUrl)
                .centerCrop()
                .optionalFitCenter()
                .placeholder(R.mipmap.ic_launcher_custom)
                .into(imgView);
    }

    protected void updateEnemyCounterImgViews() {
        String requestUrl = "https://genshin.jmp.blue/materials/common-ascension";

        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest mJsonRequest = new JsonObjectRequest(requestUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // this list of ArrayLists will hold the 3 rarity types of enemy drops, blue, green, and grey.
                ArrayList<String>[] possibleIcons = new ArrayList[3];
                JSONArray categoriesArr;

                // Initializes possibleIcons.
                for (int k = 0; k < possibleIcons.length; k++) {
                    possibleIcons[k] = new ArrayList<String>();
                }

//                txtStatic.setText(response.names().toString());
                // Turns the JSONObject response into a JSONArray called categoriesArr.
                try {
                    categoriesArr = response.toJSONArray(response.names());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // Go through all the categories in response.
                for (int i = 0; i < categoriesArr.length(); i++) {
                    try {
                        JSONObject curCategory = categoriesArr.getJSONObject(i);

                        // First check if this material type is for upgrading characters (there were some weapon only materials when I looked through the response).
                        if (curCategory.getString("weapons") != null) {
                            JSONArray itemArr = curCategory.getJSONArray("items");
                            // This means the item is a "miniboss" item, as it doesn't have a grey rarity version.
                            if (itemArr.getJSONObject(0).getInt("rarity") != 1) {
                                continue;
                            }
                            // Each item has an attribute called "rarity", and I use that to sort them into the correct ArrayList.
                            for (int j = 0; j < itemArr.length(); j++) {
                                JSONObject curItem = itemArr.getJSONObject(j);
                                // I reversed the order (rarity 3 = blue, but put into possibleIcons[0]) because
                                // the for loop that puts the images into the ImageViews below,
                                // starts with the highest rarity at the lowest index, mirroring allImgViewIcons[].
                                switch (curItem.getInt("rarity")) {
                                    case 3:
                                        possibleIcons[0].add(curItem.getString("id"));
                                        break;
                                    case 2:
                                        possibleIcons[1].add(curItem.getString("id"));
                                        break;
                                    case 1:
                                        possibleIcons[2].add(curItem.getString("id"));
                                        break;
                                }
                            }

                        }
                    } catch (Exception e) {
                        Log.i("API", "categoriesArr loop failed: " + e.toString());
                    }
                }
//                txtStatic.setText(possibleIcons[0].toString() + "\n" + possibleIcons[1].toString() + "\n" + possibleIcons[2].toString());

//                Toast.makeText(getContext(), possibleIcons[0].size() + " " + possibleIcons[1].size() + " " + possibleIcons[2].size(), Toast.LENGTH_SHORT).show();
                // Skips imgViewYellow & imgViewPurple.
                for (int i = 0; (i + 2) < allImgViewIcons.length; i++) {
                    if ((i + 2) < allImgViewIcons.length) {
                        // Uses superclass' method to display the images.
                        updateCounterIcon(allImgViewIcons[i + 2], requestUrl + "/" + possibleIcons[i].get((int) (Math.random() * possibleIcons[i].size())));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "API request for image failed.", Toast.LENGTH_SHORT).show();
                Log.i("API", "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mJsonRequest);
    }
    // endregion


    private class TextWatcherWithEditText implements TextWatcher {
        private int edtIndex;

        public TextWatcherWithEditText(int i) {
            edtIndex = i;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (edittextsAreReady) {
                int tabIndex = tabMaterials.getSelectedTabPosition();

                if (allEditTexts[edtIndex].getText().toString().isEmpty()) {
                    tabValArray[tabIndex][edtIndex] = 0;
                } else {
                    tabValArray[tabIndex][edtIndex] = Integer.parseInt(allEditTexts[edtIndex].getText().toString());
                }

                checkOverflow(allEditTexts[edtIndex]);
                saveData();
                checkRequirements();
            }
        }
    }
}
