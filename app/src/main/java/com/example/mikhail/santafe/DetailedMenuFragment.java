package com.example.mikhail.santafe;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mikhail.santafe.data.SantafeContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail on 06.03.2015.
 */
public  class DetailedMenuFragment extends Fragment {

    final String SALAD = "Салаты";
    final String COLD = "Холодные закуски";

    final int SALAD_KEY = 2;
    final int COLD_DISH_KEY = 3;


    private ArrayAdapter<String> mForecastAdapter;


    public DetailedMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
 //       View rootView = inflater.inflate(R.layout.fragment_detailed_menu, container, false);

//        String str ;
//        String[]  saladArray = {
//                "Капрезе",
//                "Греческий салат по-деревенски",
//                "Греческий салат",
//                "Триколори",
//                "Машук",
//                "Белград",
//                "Птичье гнездо",
//                "Гавайи",
//                "Цезарь с курицей",
//                "Цезарь с тигровыми креветками",
//                "Санта-Фе",
//                "Примавера",
//                "Бродвей",
//                "Тайский салат",
//                "Салат из говяжъего языка",
//                "Луи XIV",
//                "Дары моря",
//                "Салат от шефа",
//                "Салат из тунца",
//                "Средиземноморский"
//        };
//
//        String[]  coldArray = {
//                "Овощная дощечка",
//                "Сырная дощечка",
//                "Мясное ассорти",
//                "Карпаччо из говдяины",
//                "Карпаччо из лосося и тигровой креветки",
//                "Рыбное ассорти",
//                "Лосось малосоленый",
//                "Икра красная, масло, хлеб",
//                "Соленья",
//                "Маслины и оливки"
//        };
//
//        List<String> detailedMenu=  new ArrayList<String>(Arrays.asList(saladArray));
//
//        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//            str   = intent.getStringExtra(Intent.EXTRA_TEXT);
//            if (str.equals(SALAD))
//                detailedMenu = new ArrayList<String>(Arrays.asList(saladArray));
//            else if (str.equals(COLD))
//                detailedMenu = new ArrayList<String>(Arrays.asList(coldArray));
//        }
//
//        mForecastAdapter = new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.list_item_detiled_menu,
//                R.id.list_item_detailed_menu_textview,
//                detailedMenu);
//
//        ListView listView = (ListView) rootView.findViewById(R.id.listview_detailed_menu);
//        listView.setAdapter(mForecastAdapter);
//        return rootView;

       String[] arr= new String[]{};
        String[] arr2= new String[]{};
        String str;
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            str   = intent.getStringExtra(Intent.EXTRA_TEXT);

            // User chose salads
            if (str.equals(SALAD))
                arr = getDishByCategoryId(SALAD_KEY);

            // Users chose cold dishes
            else if (str.equals(COLD))
                arr = getDishByCategoryId(COLD_DISH_KEY);
            // ПРОСТО ПО
          // arr2 = getAllCategories();
        }

        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_detiled_menu, // The name of the layout ID.
                        R.id.list_item_detailed_menu_textview, // The ID of the textview to populate.
                        arr);

        View rootView = inflater.inflate(R.layout.fragment_detailed_menu, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_detailed_menu);
        listView.setAdapter(mForecastAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view,int position, long l)
                    {
                        String forecast = mForecastAdapter.getItem(position);

                        Intent intent = new Intent(getActivity(),DishDetails.class).putExtra(Intent.EXTRA_TEXT,forecast);
                        startActivity(intent);

                        // Toast toast = Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT);
                        // toast.show();
                    }
                }
        );

        return rootView;
    }



    String[] getAllCategories() {
        List<String > categories= new ArrayList();


        Cursor categoryCursor = this.getActivity().getContentResolver().query(
                SantafeContract.CategoryEntry.CONTENT_URI,
                new String[]{SantafeContract.CategoryEntry.COLUMN_CAT_TITLE},
                null,
                null,
                null);

        if (categoryCursor.moveToFirst()) {
            while (categoryCursor.moveToNext()) {
                int categoryIndex = categoryCursor.getColumnIndex(SantafeContract.CategoryEntry.COLUMN_CAT_TITLE);
                categories.add(categoryCursor.getString(categoryIndex));
            }
        }
        String[] array = new String[categories.size()];
        categories.toArray(array);

        return array;
    }


    /*
     Return array of string (dishes) by category
     */
    String[] getDishByCategoryId(long id) {
        List<String > category= new ArrayList();

//        int categoryCursor2 = this.getActivity().getContentResolver().delete(
//                SantafeContract.DishEntry.CONTENT_URI,
//                null,
//                null
//        );

        Cursor categoryCursor = this.getActivity().getContentResolver().query(
                SantafeContract.DishEntry.CONTENT_URI,
                new String[]{SantafeContract.DishEntry.COLUMN_DISH_TITLE},
                SantafeContract.DishEntry.COLUMN_CAT_KEY + " = ?",
                new String[]{Long.toString(id)},
                null);

        if (categoryCursor.moveToFirst()) {
            while (categoryCursor.moveToNext()) {
                int categoryIndex = categoryCursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_DISH_TITLE);
                category.add(categoryCursor.getString(categoryIndex));
            }
        }
        String[] array = new String[category.size()];
        category.toArray(array);

        return array;
    }
}