package com.example.mikhail.santafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mikhail on 06.03.2015.
 */
public  class DetailedMenuFragment extends Fragment {

    final String SALAD = "Салаты";
    final String COLD = "Холодные закуски";

    private ArrayAdapter<String> mForecastAdapter;

    public DetailedMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detailed_menu, container, false);

        String str ;
        String[]  saladArray = {
                "Капрезе",
                "Греческий салат по-деревенски",
                "Греческий салат",
                "Триколори",
                "Машук",
                "Белград",
                "Птичье гнездо",
                "Гавайи",
                "Цезарь с курицей",
                "Цезарь с тигровыми креветками",
                "Санта-Фе",
                "Примавера",
                "Бродвей",
                "Тайский салат",
                "Салат из говяжъего языка",
                "Луи XIV",
                "Дары моря",
                "Салат от шефа",
                "Салат из тунца",
                "Средиземноморский"
        };

        String[]  coldArray = {
                "Овощная дощечка",
                "Сырная дощечка",
                "Мясное ассорти",
                "Карпаччо из говдяины",
                "Карпаччо из лосося и тигровой креветки",
                "Рыбное ассорти",
                "Лосось малосоленый",
                "Икра красная, масло, хлеб",
                "Соленья",
                "Маслины и оливки"
        };

        List<String> detailedMenu=  new ArrayList<String>(Arrays.asList(saladArray));


        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            str   = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (str.equals(SALAD))
                detailedMenu = new ArrayList<String>(Arrays.asList(saladArray));
            else if (str.equals(COLD))
                detailedMenu = new ArrayList<String>(Arrays.asList(coldArray));
        }

        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_detiled_menu,
                R.id.list_item_detailed_menu_textview,
                detailedMenu);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_detailed_menu);
        listView.setAdapter(mForecastAdapter);
        return rootView;
    }
}