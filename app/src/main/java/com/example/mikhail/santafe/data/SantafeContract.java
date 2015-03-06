package com.example.mikhail.santafe.data;

import android.provider.BaseColumns;

/**
 * Defines table and column names for the menu database.
 */
public class SantafeContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    /* TODO Uncomment for
    4b - Adding ContentProvider to our Contract
    https://www.udacity.com/course/viewer#!/c-ud853/l-1576308909/m-1637521471
    public static final String CONTENT_AUTHORITY = "com.example.android.sunshine.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.

    public static final String PATH_WEATHER = "weather";
    public static final String PATH_LOCATION = "location";
    */

    /* TODO Uncomment for
    4b - Finishing the FetchWeatherTask
    https://www.udacity.com/course/viewer#!/c-ud853/l-1576308909/m-1675098569
    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    
    public static final String DATE_FORMAT = "yyyyMMdd";
    */


    /* TODO Uncomment for
    4b - Finishing the FetchWeatherTask
    https://www.udacity.com/course/viewer#!/c-ud853/l-1576308909/m-1675098569
    public static String getDbDateString(Date date){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }
    */


    /* TODO Uncomment for
    4b - Finishing the FetchWeatherTask
    https://www.udacity.com/course/viewer#!/c-ud853/l-1576308909/m-1675098569
    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }
    */

    // Table for categoies of disches (salad,soup etc)
        public static final class CategoryEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "category";
        // The name of category
        public static final String COLUMN_CAT_TITLE = "cat_title";


        /* TODO Uncomment for
        4b - Adding ContentProvider to our Contract
        https://www.udacity.com/course/viewer#!/c-ud853/l-1576308909/m-1637521471

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        */

        /**
         * TODO YOUR CODE BELOW HERE FOR QUIZ
         * QUIZ - 4b - Adding LocationEntry with ID UriBuilder
         * https://www.udacity.com/course/viewer#!/c-ud853/l-1576308909/e-1604969848/m-1604969849
         **/

    }

    /*
    /* Inner class that defines the table contents of the weather table */
    public static final class DishEntry implements BaseColumns {

        public static final String TABLE_NAME = "dish";

        // Dish id as returned by API, to identify the icon to be used
        public static final String COLUMN_DISH_ID = "dish_id";

        // Title of dish
        public static  final String COLUMN_DISH_TITLE = "dish_title";

        // Full description of the dish, as provided by API.
        public static final String COLUMN_FULL_DESC = "full_desc";

        // Price
        public static final String COLUMN_PRICE = "price";

        // Caloric value (ccal)
        public static final String COLUMN_CCAL = "caloric_value";

        // Column with the foreign key into the category table.
        public static final String COLUMN_CAT_KEY = "category_id";

        // Weight
        public static final String COLUMN_WEIGHT = "weight";


        /* TODO Uncomment for
        4b - Adding ContentProvider to our Contract
        https://www.udacity.com/course/viewer#!/c-ud853/l-1576308909/m-1637521471
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWeatherLocation(String locationSetting) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).build();
        }

        public static Uri buildWeatherLocationWithStartDate(
                String locationSetting, String startDate) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendQueryParameter(COLUMN_DATETEXT, startDate).build();
        }

        public static Uri buildWeatherLocationWithDate(String locationSetting, String date) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).appendPath(date).build();
        }

        public static String getLocationSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getStartDateFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DATETEXT);
        }*/
    }
}
