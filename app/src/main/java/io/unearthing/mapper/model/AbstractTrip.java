package io.unearthing.mapper.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by billybonks on 27/2/16.
 */
public abstract class AbstractTrip {

    protected long mId = -1;

    @SerializedName("title")
    protected String mTitle;

    @SerializedName("start_time")
    protected long mStartTime = -1;

    @SerializedName("end_time")
    protected long mEndTime = -1;
}
