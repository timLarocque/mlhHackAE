package edu.uml.android.cityecho;

import android.graphics.Bitmap;

/**
 * Created by Tim on 11/5/2016.
 */
public class Issue {

    // Private member variables
    int mStreetNumber;
    String mStreetName;
    String mCity;
    String mState;
    String mType;
    Bitmap mMapImage;

    Issue(int sNum, String sName, String city, String state, String type, Bitmap map) {
        mStreetNumber = sNum;
        mStreetName = sName;
        mCity = city;
        mState = state;
        mType = type;
        mMapImage = map;
    }

    int getStreetNumber() { return mStreetNumber; }
    String getStreetName() { return mStreetName; }
    String getCity() { return mCity; }
    String getState() { return mState; }
    String getType() { return mType; }
    Bitmap getMapImage() { return mMapImage; }
    String getFullAddress() {
        return Integer.toString(mStreetNumber) + " "
                + mStreetName + ", " + mCity + ", " + mState;
    }

}
