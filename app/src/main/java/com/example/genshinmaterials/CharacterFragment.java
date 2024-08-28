package com.example.genshinmaterials;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.JsonReader;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CharacterFragment extends CounterFragment {

    // region For saving the data on app close.

    // Holds each EditText value on app closure. Note that the strings initialized here are to show the order, and are not saved.
    public static final String[] EDITTEXT_VALUES_0 = {"yellow_0_char", "purple_0_char", "blue_0_char", "green_0_char", "grey_0_char"};
    public static final String[] EDITTEXT_VALUES_1 = {"yellow_1_char", "purple_1_char", "blue_1_char", "green_1_char", "grey_1_char"};
    public static final String[] EDITTEXT_VALUES_2 = {"yellow_2_char", "purple_2_char", "blue_2_char", "green_2_char", "grey_2_char"};

    public static String SUBTAB_POSITION = "subtab_position_char";

    public static String ITEM_RARITY = "rarity_weapon_char";

    // Programmatically replaces the names of the subtabs.
    private static final String[] tabNamesArr = {"Gem", "Enemy", "Msc."};

    // The hardcoded amount of materials need to fully level up the weapon.
    // Each row is in descending rarity, mirroring allEditTexts.
    // Each column represents the corresponding indexed subtab. Ex: reqMats*Star[0] = "Domain", ...[1] = "Miniboss", ...[2] = "Enemy".
    private static int[][] reqMats = {{6, 9, 9, 1, 0},
            {0, 0, 36, 30, 18},
            {0, 46, 420000, 0, 168}};

    private String requestUrl2 = "https://genshin.jmp.blue/materials/character-ascension";

    private String requestUrl1 = "https://genshin.jmp.blue/materials/common-ascension";

    // Is all the character gem images
    private String requestUrl0 = "https://genshin.jmp.blue/materials/character-ascension";


    // Passes the final String array which names all EditTexts save data.
    CharacterFragment() {
        super(EDITTEXT_VALUES_0, EDITTEXT_VALUES_1, EDITTEXT_VALUES_2, tabNamesArr, reqMats, SUBTAB_POSITION, ITEM_RARITY);
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
                        if (curCategory.getString("characters") != null) {
                            JSONArray itemArr = curCategory.getJSONArray("items");
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

                // Skips imgViewYellow & imgViewPurple.
                for (int i = 0; (i + 2) < allImgViewIcons.length; i++) {
                    if ((i + 2) < allImgViewIcons.length) {
                        // Uses superclass' method to display the images.
                        updateCounterIcon(allImgViewIcons[i + 2], requestUrl1 + "/" + possibleIcons[i].get((int) (Math.random() * possibleIcons[i].size())));
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
        // Modified from https://www.geeksforgeeks.org/making-api-calls-using-volley-library-in-android/.
        // These variables are used to call a Genshin API. Source: https://github.com/genshindev/api.
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest mJsonRequest = new JsonArrayRequest(requestUrl0 + "/list", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                super.updateCounterIconsTab0(response);
//                txtStatic.setText(response.length() + "\n" + response.toString());

                String[] typeSortedArr = new String[response.length()];

                // region Sorting typeSortedArr
                // https://stackoverflow.com/questions/15871309/convert-jsonarray-to-string-array
                for (int i = 0; i < typeSortedArr.length; i++) {
                    try {
                        typeSortedArr[i] = response.get(i).toString();
                    } catch (JSONException e) {
                        Log.i("API", e.toString());
                    }
                }

                // https://www.techiedelight.com/sort-array-of-strings-java/
                Arrays.sort(typeSortedArr, new Comparator<String>() {
                    @Override
                    public int compare(String str1, String str2) {
                        String[] str2Arr = str2.split("-");
                        String[] str1Arr = str1.split("-");
                        return str1Arr[str1Arr.length - 1].compareTo(str2Arr[str2Arr.length - 1]);
                    }
                });
                // endregion



                // Was originally 4 when using response unsorted, but now is 8 due to there being 8 types of gems you can get in the game.
                int separation = 8;

                // Since typeSortedArr is still alphabetical, the sorted array looks like
                // {*-chunk, *-fragment, *-gemstone, *-sliver}.
                // The indexes below push the starting point in allImgViewIcons[] to their matching spot in typeSortedArr[].
                int[] range = {separation * 2, 0, separation, separation * 3};

//                txtStatic.setText(Arrays.toString(typeSortedArr));

                for (int i = 0; i < 4; i++) {
                    if (i < response.length()) {
                        // uses superclass' method to display the gems. range[i] sets the correct starting point, and the random number does the rest.
                        updateCounterIcon(allImgViewIcons[i], requestUrl0 + "/" + typeSortedArr[range[i] + ((int) (Math.random() * separation))]);
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
}
