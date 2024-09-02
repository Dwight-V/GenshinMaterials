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

    private static final String TEXTSTATIC_TEXT = "Mora: x2,000,000\nLocal Specialty: x168\nBoss Drop: ×46\nHero's Wit: x419";

    // Programmatically replaces the names of the subtabs.
    private static final String[] tabNamesArr = {"Gem", "Enemy", "XP"};

    // The hardcoded amount of materials need to fully level up the weapon.
    // Each row is in descending rarity, mirroring allEditTexts.
    // Each column represents the corresponding indexed subtab. Ex: reqMats*Star[0] = "Domain", ...[1] = "Miniboss", ...[2] = "Enemy".
    private static int[][] reqMats = {{6, 9, 9, 1, 0},
            {0, 0, 36, 30, 18},
            {0, 419, 10000, 10000, 0}};


    private String requestUrl2 = "https://genshin.jmp.blue/materials/character-experience";

//    private String requestUrl1 = "https://genshin.jmp.blue/materials/common-ascension";

    private String requestUrl0 = "https://genshin.jmp.blue/materials/character-ascension";

    /*
    Note:
    I created local arrays here, that did all the sorting for the API JSON Arrays once (overriding loadData()), then randomized which
    endpoints (held in the local arrays) to load the images.

    Basically, we call the API to get a list of all the possible images in loadData(), put these endpoints into local arrays, then on subtab switch
    or Fragment creation, we randomly select the endpoint from the local array and set the ImageViews from there.

    I follow this stackoverflow (https://stackoverflow.com/a/39587340), where I made these global local arrays final. Understand that the arrays in the current
    updateCounterIconsTab*() are local to their respective methods, and can not be globalized due to API calls being asynchronous
    (see https://stackoverflow.com/a/70178210). So making them final is the easiest way to pull them out of the Volley.onResponse() method.

    This stops having to sort the image list each time we want to update the ImageViews, as we indeed sort the JSON response each time you switch subtabs
    (or switch main tabs, etc, etc). I wanted the images to load instantly, but they don't. While the images load faster, comparing the times,
    it's too insignificant for the amount of work involved (even though all the work is already done for this Fragment).

    I would save my git stash to a new branch, but as I just said, it's not worth it. The default image (what displays until the API call is completed, and
    which is the app icon as of this moment) still flashes for a split second even with the more efficient calls. But now images don't load for the
    current subtab you're on (I think due to being a synchronous call now).
    */

    // Passes the final String array which names all EditTexts save data.
    CharacterFragment() {
        super(EDITTEXT_VALUES_0, EDITTEXT_VALUES_1, EDITTEXT_VALUES_2, tabNamesArr, reqMats, SUBTAB_POSITION, ITEM_RARITY, TEXTSTATIC_TEXT);
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
    public void checkRequirements() {
        if (tabMaterials.getSelectedTabPosition() != 2) {
            super.checkRequirements();
        } else {
            // Need custom logic for the character XP.

            // These are how much hero's wit, adventurer's experience, and wander's advice are worth respectively.
            int purpleXp = 20000;
            int blueXp = 5000;
            int greenXp = 1000;

            int[] xpArr = {0, purpleXp, blueXp, greenXp, 0};

            // From the devs themselves, 419 hero's wit is needed to go from lvl 1 - lvl 90.
            int totalReqXp = reqMats[2][1] * purpleXp;
            int totalXp = 0;

            for (int i = 0; i < allEditTexts.length; i++) {
                totalXp += Integer.parseInt(allEditTexts[i].getText().toString()) * xpArr[i];
            }

            if (totalXp >= totalReqXp) {
                // https://stackoverflow.com/a/20121975
                for (int i = 0; i < allDrwChecks.length; i++) {
//                allDrwChecks[i].setVisibility(View.VISIBLE);
                    allDrwChecks[i].setColorFilter(ContextCompat.getColor(getActivity(), R.color.check_green));
                }
            } else {
                for (int i = 0; i < allDrwChecks.length; i++) {
//                allDrwChecks[i].setVisibility(View.INVISIBLE);
                    allDrwChecks[i].setColorFilter(ContextCompat.getColor(getActivity(), R.color.check_grey));
                }
            }
        }
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
        // Modified from https://www.geeksforgeeks.org/making-api-calls-using-volley-library-in-android/.
        // These variables are used to call a Genshin API. Source: https://github.com/genshindev/api.
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest mJsonRequest = new JsonArrayRequest(requestUrl2 + "/list", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String[] endUrl = new String[5];

                try {
                    for (int i = 0; i < response.length(); i++) {
                        String curResponseStr = response.getString(i);
                        switch (curResponseStr.charAt(0)) {
                            // Adventure's Experience, so blue.
                            case 'a':
                                endUrl[2] = curResponseStr;
                                break;
                            // Hero's wit, purple.
                            case 'h':
                                endUrl[1] = curResponseStr;
                                break;
                            default:
                                endUrl[3] = curResponseStr;
                                break;
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // By starting and ending an index earlier, we skip iconYellow and iconGrey.
                for (int i = 1; i < allImgViewIcons.length - 1; i++) {
                    updateCounterIcon(allImgViewIcons[i], requestUrl2 + "/" + endUrl[i]);
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

    public void updateCounterIconsTab1() {
        updateEnemyCounterImgViews();
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
