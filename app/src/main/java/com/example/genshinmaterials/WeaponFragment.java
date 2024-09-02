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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class WeaponFragment extends CounterFragment {

    // region For saving the data on app close.

    // Holds each EditText value on app closure. Note that the strings initialized here are to show the order, and are not saved.
    public static final String[] EDITTEXT_VALUES_0 = {"yellow_0_weapon", "purple_0_weapon", "blue_0_weapon", "green_0_weapon", "grey_0_weapon"};
    public static final String[] EDITTEXT_VALUES_1 = {"yellow_1_weapon", "purple_1_weapon", "blue_1_weapon", "green_1_weapon", "grey_1_weapon"};
    public static final String[] EDITTEXT_VALUES_2 = {"yellow_2_weapon", "purple_2_weapon", "blue_2_weapon", "green_2_weapon", "grey_2_weapon"};

    public static String SUBTAB_POSITION = "subtab_position_weapon";

    public static String ITEM_RARITY = "rarity_weapon";

    private static final String TEXTSTATIC_TEXT = "Mora: x1,100,000\nMystic Enhancement Ore: x907";

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


//    private String requestUrl2 = "https://genshin.jmp.blue/materials/common-ascension";

    private String requestUrl1 = "https://genshin.jmp.blue/materials/common-ascension";

    private String requestUrl0 = "https://genshin.jmp.blue/materials/weapon-ascension";


    // Passes the final String array which names all EditTexts save data.
    WeaponFragment() {
        super(EDITTEXT_VALUES_0, EDITTEXT_VALUES_1, EDITTEXT_VALUES_2, tabNamesArr, reqMats5Star, SUBTAB_POSITION, ITEM_RARITY, TEXTSTATIC_TEXT);
    }
    // endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_counter, container, false);
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void updateEdittextVals() {
        // Ensures the correct weapon rarity is being calculated.
        if (itemRarity == 5) {
            reqMats = reqMats5Star;
        } else if (itemRarity == 4) {
            reqMats = reqMats4Star;
        } else {
            reqMats = reqMats3Star;
        }

        super.updateEdittextVals();
    }

    @Override
    public void checkRequirements() {
        // Ensures the correct weapon rarity is being calculated.
        if (itemRarity == 5) {
            reqMats = reqMats5Star;
        } else if (itemRarity == 4) {
            reqMats = reqMats4Star;
        } else {
            reqMats = reqMats3Star;
        }

        super.checkRequirements();
    }

    @Override
    public void updateCounterUi() {
        super.updateCounterUi();

        switch (tabMaterials.getSelectedTabPosition()) {
            case 2:
                updateCounterIconsTab2();
                break;
            case 1:
                updateCounterIconsTab1();
                break;
            default:
                updateCounterIconsTab0();
                break;
        }
    }


    public void updateCounterIconsTab2() {
        updateEnemyCounterImgViews();
    }
    public void updateCounterIconsTab1() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest mJsonRequest = new JsonObjectRequest(requestUrl1, new Response.Listener<JSONObject>() {
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

                            // This means the item isn't a "miniboss" item, as it does have a grey rarity version.
                            if (itemArr.getJSONObject(0).getInt("rarity") == 1) {
                                continue;
                            }

                            // Each item has an attribute called "rarity", and I use that to sort them into the correct ArrayList.
                            for (int j = 0; j < itemArr.length(); j++) {
                                JSONObject curItem = itemArr.getJSONObject(j);
                                // I reversed the order (rarity 3 = blue, but put into possibleIcons[0]) because
                                // the for loop that puts the images into the ImageViews below,
                                // starts with the highest rarity at the lowest index, mirroring allImgViewIcons[].
                                switch (curItem.getInt("rarity")) {
                                    case 4:
                                        possibleIcons[0].add(curItem.getString("id"));
                                        break;
                                    case 3:
                                        possibleIcons[1].add(curItem.getString("id"));
                                        break;
                                    case 2:
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

                // Skips imgViewYellow & imgViewPurple.
                for (int i = 0; (i + 1) < allImgViewIcons.length - 1; i++) {
                    if ((i + 1) < allImgViewIcons.length) {
                        // Uses superclass' method to display the images.
                        updateCounterIcon(allImgViewIcons[i + 1], requestUrl1 + "/" + possibleIcons[i].get((int) (Math.random() * possibleIcons[i].size())));
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

    public void updateCounterIconsTab0() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest mJsonRequest = new JsonObjectRequest(requestUrl0, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // this list of ArrayLists will hold the 3 rarity types of enemy drops, blue, green, and grey.
                ArrayList<String>[] possibleIcons = new ArrayList[4];
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
                            // Each item has an attribute called "rarity", and I use that to sort them into the correct ArrayList.
                            for (int j = 0; j < itemArr.length(); j++) {
                                JSONObject curItem = itemArr.getJSONObject(j);
                                // I reversed the order (rarity 3 = blue, but put into possibleIcons[0]) because
                                // the for loop that puts the images into the ImageViews below,
                                // starts with the highest rarity at the lowest index, mirroring allImgViewIcons[].
                                switch (curItem.getInt("rarity")) {
                                    case 5:
                                        possibleIcons[0].add(curItem.getString("id"));
                                        break;
                                    case 4:
                                        possibleIcons[1].add(curItem.getString("id"));
                                        break;
                                    case 3:
                                        possibleIcons[2].add(curItem.getString("id"));
                                        break;
                                    case 2:
                                        possibleIcons[3].add(curItem.getString("id"));
                                        break;
                                }
                            }

                        }
                    } catch (Exception e) {
                        Log.i("API", "categoriesArr loop failed: " + e.toString());
                    }
                }
//                txtStatic.setText(possibleIcons[0].toString() + "\n" + possibleIcons[1].toString() + "\n" + possibleIcons[2].toString() + "\n" + possibleIcons[3].toString());

                // Skips imgViewGrey
                for (int i = 0; i < (allImgViewIcons.length - 1); i++) {
                    // Uses superclass' method to display the images.
                    updateCounterIcon(allImgViewIcons[i], requestUrl0 + "/" + possibleIcons[i].get((int) (Math.random() * possibleIcons[i].size())));
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
}
