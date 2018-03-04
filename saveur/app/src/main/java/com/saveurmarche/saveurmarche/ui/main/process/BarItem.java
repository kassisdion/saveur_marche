package com.saveurmarche.saveurmarche.ui.main.process;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public abstract class BarItem {
    /*
    ********************************************************************************************
    ** Method every child will have to implement
    ********************************************************************************************
    */
    @NonNull
    public abstract Fragment getFragment();
}
