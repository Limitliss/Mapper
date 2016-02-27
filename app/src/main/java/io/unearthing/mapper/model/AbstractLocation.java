package io.unearthing.mapper.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by billybonks on 24/2/16.
 */
public abstract class AbstractLocation {
    long mId;

    @SerializedName("longitude")
    protected double mLongitude;

    @SerializedName("latitude")
    protected double mLatitude;

    @SerializedName("accuracy")
    protected float mAccuracy;

    @SerializedName("bearing")
    protected float mBearing;

    @SerializedName("altitude")
    protected double mAltitude;

    @SerializedName("speed")
    protected double mSpeed;

    @SerializedName("timestamp")
    protected float mTimeStamp;

    @SerializedName("trip_id")
    protected long mTripId;
}
