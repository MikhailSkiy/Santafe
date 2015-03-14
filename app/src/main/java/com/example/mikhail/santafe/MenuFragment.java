package com.example.mikhail.santafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mikhail.santafe.data.SantafeContract;

import java.util.ArrayList;

public  class MenuFragment extends Fragment {


    private static final String[] FORECAST_COLUMNS = {
// In this case the id needs to be fully qualified with a table name, since
// the content provider joins the location & weather tables in the background
// (both have an _id column)
// On the one hand, that's annoying. On the other, you can search the weather table
// using the location set by the user, which is only in the Location table.
// So the convenience is worth it.
            SantafeContract.DishEntry.TABLE_NAME + "." + SantafeContract.DishEntry._ID,
            SantafeContract.DishEntry.COLUMN_DISH_TITLE,
            SantafeContract.DishEntry.COLUMN_FULL_DESC,
            SantafeContract.DishEntry.COLUMN_CCAL,
            SantafeContract.DishEntry.COLUMN_PRICE,
            SantafeContract.DishEntry.COLUMN_WEIGHT


    };
    // These indices are tied to FORECAST_COLUMNS. If FORECAST_COLUMNS changes, these
// must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_DISH_TITLE = 1;
    static final int COL_FULL_DESC = 2;
    static final int COL_CCAL = 3;
    static final int COL_PRICE = 4;
    static final int COL_WEIGHT = 5;

   // private MenuAdapter mMenuAdapter;

    private ArrayAdapter<String> mMenuAdapter;

    public MenuFragment() {
    }

     final String SALAD = "Салаты";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setHasOptionsMenu(true);
        }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menufragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        // The CursorAdapter will take data from our cursor and populate the ListView.
//        mMenuAdapter = new MenuAdapter(getActivity(), null, 0);
//
       // View rootView = inflater.inflate(R.layout.fragment_my, container, false);
//
//        // Get a reference to the ListView, and attach this adapter to it.
       // ListView listView = (ListView) rootView.findViewById(R.id.listview_main);
      //  listView.setAdapter(mMenuAdapter);




                // The ArrayAdapter will take data from a source and
                // use it to populate the ListView it's attached to.
                mMenuAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_main_menu, // The name of the layout ID.
                        R.id.list_item_main_menu_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_my, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_main);
        listView.setAdapter(mMenuAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,int position, long l)
            {
                String forecast = mMenuAdapter.getItem(position);

                Intent intent = new Intent(getActivity(),DetailedMenu.class).putExtra(Intent.EXTRA_TEXT,forecast);
                startActivity(intent);

                // Toast toast = Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT);
                // toast.show();
            }
        }
        );

        return rootView;
    }

//    String[] getAllCategories() {
//        List<String > categories= new ArrayList();
//
//
//        Cursor categoryCursor = this.getActivity().getContentResolver().query(
//                SantafeContract.CategoryEntry.CONTENT_URI,
//                new String[]{SantafeContract.CategoryEntry.COLUMN_CAT_TITLE},
//                null,
//                null,
//                null);
//
//        if (categoryCursor.moveToFirst()) {
//            while (categoryCursor.moveToNext()) {
//                int categoryIndex = categoryCursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_DISH_TITLE);
//                categories.add(categoryCursor.getString(categoryIndex));
//            }
//        }
//        String[] array = new String[categories.size()];
//        categories.toArray(array);
//
//        return array;
//    }



    private void updateWeather() {
        FetchMenuTask weatherTask = new FetchMenuTask(getActivity(), mMenuAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String location = prefs.getString(getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default));
        weatherTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
       // updateWeather();
    }

    }


