package com.saveurmarche.saveurmarche.ui.base

import android.support.v4.app.Fragment
import java.io.Serializable

abstract class BaseFragment : Fragment(), Serializable {
    protected val TAG: String = javaClass.simpleName
}