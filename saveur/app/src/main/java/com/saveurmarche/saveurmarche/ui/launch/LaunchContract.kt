package com.saveurmarche.saveurmarche.ui.launch

import com.saveurmarche.saveurmarche.ui.base.BaseView

interface LaunchContract {
    interface View : BaseView {
        fun redirectToMainActivity()

    }

    interface Presenter {
        fun setupView()
    }
}