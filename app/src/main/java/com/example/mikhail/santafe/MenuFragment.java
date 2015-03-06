package com.example.mikhail.santafe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  class MenuFragment extends Fragment {
    public MenuFragment() {
    }

    private ArrayAdapter<String> mMenuAdapter;

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

            FetchMenuTask menuTask  = new FetchMenuTask();
            menuTask.execute();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);

        String[] listOfCategories = getResources().getStringArray(R.array.categories);

        List<String> list = new ArrayList<String>(Arrays.asList(listOfCategories));

        mMenuAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_main_menu,
                R.id.list_item_main_menu_textview,
                listOfCategories);


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
        });

        return rootView;
    }

    public class FetchMenuTask extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchMenuTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://cafeapi.azurewebsites.net/api/dishes");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                   // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                   // buffer for debugging.
                    buffer.append(line );
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG,"Menu Json String: "+ forecastJsonStr);

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }



        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getDataFromJson(String forecastJsonStr)
                throws JSONException {

            final String TITLE = "DishTitle";
            final String FULL_DESCRIPTION = "Description";
            final String PRICE = "Price";
            final String WEIGHT = "Weight";

            // Delete all "\"
            String s = forecastJsonStr.replaceAll("\\\\", "");

            // Delete first and last "", beacuse before it was "JSON" after JSON
            String clearJson = s.substring(1,s.length()-1);

            JSONArray menuItems = new JSONArray(clearJson);

            String[] resultStrs = new String[]{};
            for(int i = 0; i < menuItems.length(); i++) {

                String title;
                String fullDescription;
                String price;
                String weight;

                // Get the JSON object representing the dish
                JSONObject dishObject = menuItems.getJSONObject(i);

                title = dishObject.getString(TITLE);
                fullDescription = dishObject.getString(FULL_DESCRIPTION);
                price = dishObject.getString(PRICE) ;
                weight = dishObject.getString(WEIGHT);

                resultStrs[i] = title;
            }

            for (String str : resultStrs) {
                Log.v(LOG_TAG, "Forecast entry: " + s);
            }
            return resultStrs;
        }
    }
}

