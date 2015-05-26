package com.example.mikhail.santafe.service;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.mikhail.santafe.data.SantafeContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Mikhail on 08.05.2015.
 */
public class SantafeService extends IntentService {
    private final String LOG_TAG = SantafeService.class.getSimpleName();
    public SantafeService(){
        super("Santafe");
    }

    @Override
    protected void onHandleIntent(Intent intent){
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
            // URL url = new URL("http://cafeapi.azurewebsites.net/api/dishes");
            URL url = new URL("http://localhost:32455/api/dishes");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line);
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            forecastJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Menu Json String: " + forecastJsonStr);

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return;
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
            getDataFromJson(forecastJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return;
    }

    private void getDataFromJson(String forecastJsonStr)
            throws JSONException {

        final String TITLE = "DishTitle";
        final String FULL_DESCRIPTION = "Description";
        final String PRICE = "Price";
        final String WEIGHT = "Weight";
        final String CCAL = "Ccal";
        final String IMAGE_ID = "ImageId";

        final String CATEGORY_NAME = "CategoryName";

        // Delete all "\"
        String s = forecastJsonStr.replaceAll("\\\\", "");

        // Delete first and last "", beacuse before it was "JSON" after JSON
        String clearJson = s.substring(1, s.length() - 1);

        try {
            JSONArray menuItems = new JSONArray(clearJson);

            //String[] resultStrs = new String[]{};

            Vector<ContentValues> cVVector = new Vector<ContentValues>(menuItems.length());

            for (int i = 0; i < menuItems.length(); i++) {

                String title;
                String fullDescription;
                int price;
                int weight;
                int ccal;
                String categoryName;
                int imageId;

                // Get the JSON object representing the dish
                JSONObject dishObject = menuItems.getJSONObject(i);

                title = dishObject.getString(TITLE);
                fullDescription = dishObject.getString(FULL_DESCRIPTION);
                price = dishObject.getInt(PRICE);
                weight = dishObject.getInt(WEIGHT);
                ccal = dishObject.getInt(CCAL);

                imageId= dishObject.getInt(IMAGE_ID);

                categoryName = dishObject.getString(CATEGORY_NAME);

                // Adding values about category
                long categoryId = addCategory(categoryName);


                // Adding values about dish

                ContentValues dishValues = new ContentValues();

                dishValues.put(SantafeContract.DishEntry.COLUMN_CAT_KEY, categoryId);
                dishValues.put(SantafeContract.DishEntry.COLUMN_DISH_TITLE, title);
                dishValues.put(SantafeContract.DishEntry.COLUMN_FULL_DESC, fullDescription);
                dishValues.put(SantafeContract.DishEntry.COLUMN_WEIGHT, weight);
                dishValues.put(SantafeContract.DishEntry.COLUMN_PRICE, price);
                dishValues.put(SantafeContract.DishEntry.COLUMN_CCAL, ccal);
                dishValues.put(SantafeContract.DishEntry.COLUMN_IMAGE_ID,imageId);

                cVVector.add(dishValues);
            }

            int inserted = 0;

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = this.getContentResolver().bulkInsert(SantafeContract.DishEntry.CONTENT_URI, cvArray);
            }

//            // Sort order:  Ascending, by date.
//            String sortOrder = WeatherEntry.COLUMN_DATE + " ASC";
//            Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(
//                    locationSetting, System.currentTimeMillis());
//
//            Cursor cur = mContext.getContentResolver().query(weatherForLocationUri,
//                    null, null, null, sortOrder);
//
//            cVVector = new Vector<ContentValues>(cur.getCount());
//            if ( cur.moveToFirst() ) {
//                do {
//                    ContentValues cv = new ContentValues();
//                    DatabaseUtils.cursorRowToContentValues(cur, cv);
//                    cVVector.add(cv);
//                } while (cur.moveToNext());
//            }

            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");

            // Ќужно было когда мы возвращали массив строк в меню, теперь мы просто записываем в бд
            // ј не каждый раз как было до этого
            // String[] resultStrs = convertContentValuesToUXFormat(cVVector);
            // return resultStrs;

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    long addCategory(String categorySetting) {
        long categoryId;


        Cursor categoryCursor = this.getContentResolver().query(
                SantafeContract.CategoryEntry.CONTENT_URI,
                new String[]{SantafeContract.CategoryEntry._ID},
                SantafeContract.CategoryEntry.COLUMN_CAT_TITLE + " = ?",
                new String[]{categorySetting},
                null);

        if (categoryCursor.moveToFirst()) {
            int locationIdIndex = categoryCursor.getColumnIndex(SantafeContract.CategoryEntry._ID);
            categoryId = categoryCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues categoryValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            categoryValues.put(SantafeContract.CategoryEntry.COLUMN_CAT_TITLE, categorySetting);


            // Finally, insert category data into the database.
            Uri insertedUri = this.getContentResolver().insert(
                    SantafeContract.CategoryEntry.CONTENT_URI,
                    categoryValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            categoryId = ContentUris.parseId(insertedUri);
        }

        categoryCursor.close();
        // Wait, that worked?  Yes!
        return categoryId;
    }


}
