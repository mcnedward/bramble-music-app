package com.mcnedward.bramble.entity.data;

import java.io.Serializable;

/**
 * Created by Edward on 5/28/2016.
 */
public abstract class Data implements Serializable {

    protected long mId;

    public Data() {}

    public Data(int mId) {
        this.mId = mId;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }
}
