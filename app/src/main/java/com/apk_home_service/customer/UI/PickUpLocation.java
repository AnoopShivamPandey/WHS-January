package com.apk_home_service.customer.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.controll.PlaceAutocompleteAdapter;
import com.apk_home_service.customer.controll.RecyclerAdpaterForFavouritePickUp;
import com.apk_home_service.customer.utill.CommonUtill;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

public class PickUpLocation extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private TextView mPlaceDetailsText;
    private TextView mPlaceDetailsAttribution;
    int PICKUP = 111;
    int DROPOFF = 222;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    ArrayList<String> favouriteLocation;
    ArrayList<String> favouriteLat;
    ArrayList<String> favouriteLng;
    ArrayList<String> favouriteListId;
    RecyclerView recyclerviewPickUp;
    RecyclerAdpaterForFavouritePickUp adapterForFavourite;
    ProgressDialog pd;
    String iduser;
    LinearLayout llpin_to_pickup;
    ImageView back_pickUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_location);
        favouriteLocation = new ArrayList<>();


      /*  favouriteLocation.add("kolkata railway station");
        favouriteLocation.add("Gwalior railway station");*/
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        iduser = getIntent().getStringExtra("id_user");

        favouriteLng = new ArrayList<>();

       /* favouriteLng.add("11.223344" + "");
        favouriteLng.add("11.223344" + "");
        favouriteLng.add("11.223344" + "");
*/
        favouriteLat = new ArrayList<>();
        /*favouriteLat.add("11.223344" + "");
        favouriteLat.add("11.223344" + "");
        favouriteLat.add("11.223344" + "");*/

        favouriteListId = new ArrayList<>();


        mGoogleApiClient = new GoogleApiClient.Builder(PickUpLocation.this)
                .enableAutoManage(PickUpLocation.this, 0 /* clientId */, PickUpLocation.this)
                .addApi(Places.GEO_DATA_API)
                .build();
        //  setContentView(R.layout.activity_pick_up_location);
// Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autoCompletetextViewPickUp);

        llpin_to_pickup = (LinearLayout) findViewById(R.id.llpin_to_pickup);
        back_pickUp = (ImageView) findViewById(R.id.back_pickUp);

        llpin_to_pickup.setOnClickListener(PickUpLocation.this);
        back_pickUp.setOnClickListener(PickUpLocation.this);

        //setting adaptter for recycler view of favourite location----
        if (favouriteLocation.size() > 0 && favouriteLat.size() > 0 && favouriteLng.size() > 0) {
            recyclerviewPickUp = (RecyclerView) findViewById(R.id.recyclerviewPickUp);
            adapterForFavourite = new RecyclerAdpaterForFavouritePickUp(this, favouriteLocation, favouriteLat, favouriteLng, favouriteListId);
            recyclerviewPickUp.setLayoutManager(new LinearLayoutManager(this));
            recyclerviewPickUp.setAdapter(adapterForFavourite);
        }


// Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

// Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
// the entire world.
        mAdapter = new PlaceAutocompleteAdapter(PickUpLocation.this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        mAutocompleteView.setAdapter(mAdapter);
       /* mAutocompleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i <= favouriteLocation.size(); i++)

                    mAdapter.add((AutocompletePrediction) formatPlaceDetailsTrimmed(getResources(), favouriteLocation.get(i), favouriteLocation.get(i)));
                mAdapter.getFilter().filter(null);
            }
        });*/

// Set up the 'clear text' button that clears the text in the autocomplete view

       /* if (PlaceAutocompleteAdapter.mResultPlaceList != null) {
            for (int i = 0; i <= PlaceAutocompleteAdapter.mResultPlaceList.size(); i++) {
                ResultPlaceList.add(PlaceAutocompleteAdapter.mResultPlaceList.get(i));
            }
            Utils.write("mResultPlaceListPickUpLocation======" + ResultPlaceList.toString());*/
    }



    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(GoogleApiClient,
     * String...)
     */


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
Retrieve the place ID of the selected item from the Adapter.
The adapter stores each Place suggestion in a AutocompletePrediction from which we
read the place ID and title.
*/
            AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
            //Log.i( "Autocomplete item selected: " + primaryText);
/*
Issue a request to the Places Geo Data API to retrieve a Place object with additional
details about the place.
*/
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            //Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,Toast.LENGTH_SHORT).show();
            //  Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };
    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {

        @Override
        public void onResult(PlaceBuffer places) {
            //  ResultPlaceList.add(places.toString());
           /* Utils.write("places===="+places);
            if (!places.getStatus().isSuccess()) {
// Request did not complete successfully
              //  Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }*/
// Get the Place object from the buffer.
            //   Utils.write("place0===="+places.get(0));
            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                final Place place = places.get(0);
                // Format details of the place for display and show it in a TextView.
             /*  mAutocompleteView.setText(formatPlaceDetails(getResources(), place.getName(),
                       place.getId(), place.getAddress(), place.getPhoneNumber(),
                       place.getWebsiteUri()));*/
                // mAutocompleteView.setText(formatPlaceDetailsTrimmed(getResources(), place.getName(), place.getAddress()));
                // mAutocompleteView.setText(place.getName()+","+place.getAddress()+"");
                LatLng selectedLatLng = place.getLatLng();
                Double selectLatitude = selectedLatLng.latitude;
                Double selectLongitude = selectedLatLng.longitude;
                Intent i = new Intent();
                i.putExtra("pickup", mAutocompleteView.getText().toString().trim());
                i.putExtra("lat_pickup", selectLatitude.toString());
                i.putExtra("lng_pickup", selectLongitude.toString());
                i.putExtra("from", "auto_complete");
                setResult(PICKUP, i);
                finish();
          /*String details=formatPlaceDetails(getResources(), place.getName(), place.getAddress())+"";
            mAutocompleteView.setText(formatPlaceDetails(getResources(), place.getName(),
            place.getId(), place.getAddress(), place.getPhoneNumber(),
            place.getWebsiteUri()));
*/


// Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();
            /*if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }*/
                //Log.i(TAG, "Place details received: " + place.getName());
                places.release();
            } else {

                CommonUtill.showTost(PickUpLocation.this,"Place not foung");
            }
        }
    };


    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //  Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
        //         + connectionResult.getErrorCode())
// TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.llpin_to_pickup:
                onBackPressed();
                break;
            case R.id.back_pickUp:
                onBackPressed();
                break;
        }
    }
}
