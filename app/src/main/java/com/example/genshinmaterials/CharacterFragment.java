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

    private String requestUrl1 = "https://genshin.jmp.blue/materials/character-ascension";

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
                setIcons(requestUrl2);
                break;
            case 1:
                setIcons(requestUrl1);
                break;
            default:
                setIcons(requestUrl0);
                break;
        }
    }

    @Override
    public void updateCounterIconsTab2(JSONArray response) {
//        super.updateCounterIconsTab2(response);
    }

    @Override
    public void updateCounterIconsTab1(JSONArray response) {
//        super.updateCounterIconsTab0(response);
    }

    @Override
    public void updateCounterIconsTab0(JSONArray response) {
//        super.updateCounterIconsTab2(response);
//        txtStatic.setText(response.length() + "\n" + response.toString());

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

//        txtStatic.setText(Arrays.toString(typeSortedArr));

        for (int i = 0; i < 4; i++) {
            if (i < response.length()) {
                // uses superclass' method to display the gems. range[i] sets the correct starting point, and the random number does the rest.
                updateCounterIcon(allImgViewIcons[i], requestUrl0 + "/" + typeSortedArr[range[i] + ((int) (Math.random() * separation))]);
            }
        }

    }
}
