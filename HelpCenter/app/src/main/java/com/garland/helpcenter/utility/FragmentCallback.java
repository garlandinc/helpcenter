package com.garland.helpcenter.utility;

import java.util.Collection;

/**
 * Created by lemon on 10/28/2017.
 */

public interface FragmentCallback {
    void onCallback(Object obj, int code);
    void addCallback(Callback callback,int code);
}
