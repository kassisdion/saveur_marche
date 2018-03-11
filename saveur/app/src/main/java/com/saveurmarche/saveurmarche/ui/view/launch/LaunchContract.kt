package com.saveurmarche.saveurmarche.ui.view.launch

import com.saveurmarche.saveurmarche.ui.view.base.BaseView

interface LaunchContract {
    interface View : BaseView {
        fun redirectToMainActivity()

    }

    interface Presenter {
        fun setupView()
    }
}