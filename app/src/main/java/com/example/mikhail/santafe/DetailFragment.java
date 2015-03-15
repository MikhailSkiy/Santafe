package com.example.mikhail.santafe;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mikhail.santafe.data.SantafeContract;

/**
 * Created by Mikhail on 15.03.2015.
 */
public  class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] FORECAST_COLUMNS = {

            SantafeContract.DishEntry.TABLE_NAME + "." + SantafeContract.DishEntry._ID,
            SantafeContract.DishEntry.COLUMN_DISH_TITLE,
            SantafeContract.DishEntry.COLUMN_FULL_DESC,
            SantafeContract.DishEntry.COLUMN_CCAL,
            SantafeContract.DishEntry.COLUMN_PRICE,
            SantafeContract.DishEntry.COLUMN_WEIGHT,
            SantafeContract.DishEntry.COLUMN_CAT_KEY,
            SantafeContract.DishEntry.COLUMN_IMAGE_ID
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_DISH_TITLE = 1;
    static final int COL_FULL_DESC = 2;
    static final int COL_CCAL = 3;
    static final int COL_PRICE = 4;
    static final int COL_WEIGHT = 5;
    static final int COL_CAT_KEY = 6;
    static  final int COL_IMAGE_ID=7;


    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final int DETAIL_LOADER = 0;


    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dish_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) {
            return;
        }


      //  int dishId = data.getInt(COL_WEATHER_ID);

        int idx_imageId = data.getColumnIndex(SantafeContract.DishEntry.COLUMN_IMAGE_ID);
        //int idx_catId=data.getColumnIndex(SantafeContract.DishEntry.COLUMN_CAT_KEY);
        String imageId = data.getString(idx_imageId);
        //String catId = data.getString(idx_catId);


        ImageView dishIcon = (ImageView)getView().findViewById(R.id.detail_dish_icon);
        dishIcon.setImageResource(Utility.getIconResourceForWeatherCondition(Integer.parseInt(imageId)));

        // Set the title of dish
        String title = Utility.convertTitleForUX(data);
        TextView detailTextView = (TextView) getView().findViewById(R.id.dish_detail_textview);
        detailTextView.setText(title);

        // Set the price
        String formattedPrice= Utility.getPriceForUX(data);
        TextView priceTextView = (TextView) getView().findViewById(R.id.detail_dish_price);
        priceTextView.setText(formattedPrice);

        // Set the weight
        String formattedWeight = Utility.getWeightForUX(data);
        TextView weightTextView = (TextView)getView().findViewById(R.id.detail_dish_weight);
        weightTextView.setText(formattedWeight);

        // Set the ccal
        String formattedCcal = Utility.getCcalForUX(data);
        TextView ccalTextView = (TextView)getView().findViewById(R.id.detail_dish_ccal);
        ccalTextView.setText(formattedCcal);

        // Set the description
        String description = Utility.getDescriptionForUX(data);
        TextView descTextView = (TextView)getView().findViewById(R.id.detail_dish_description);
        descTextView.setText(description);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
//            if (mShareActionProvider != null) {
//                mShareActionProvider.setShareIntent(createShareForecastIntent());
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            Intent intent = getActivity().getIntent();
//
//            View rootView = inflater.inflate(R.layout.fragment_dish_details, container, false);
//            String str;
//
//            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//                str = intent.getStringExtra(Intent.EXTRA_TEXT);
//
//
//                TextView detailTextView = (TextView) rootView.findViewById(R.id.dish_detail_textview);
//                detailTextView.setText(str);
//            }
//
//                return rootView;
//        }
//    }
}
