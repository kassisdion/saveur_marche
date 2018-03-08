package com.saveurmarche.saveurmarche.ui.launch

import android.content.Intent
import android.os.Bundle
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.SaveurApplication
import com.saveurmarche.saveurmarche.ui.base.BaseActivity
import com.saveurmarche.saveurmarche.ui.main.process.MainActivity
import javax.inject.Inject

class LaunchActivity : BaseActivity(), LaunchContract.View {

    /*
    ************************************************************************************************
    ** Injection
    ************************************************************************************************
    */
    @Inject
    lateinit var mPresenter: LaunchPresenter

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
     */
    override fun getLayoutResource(): Int {
        return R.layout.activity_launch
    }

    override fun init(savedInstanceState: Bundle?) {
        setupPresenter()
        mPresenter.setupView()
    }

    /*
    ************************************************************************************************
    ** LaunchContract.View implementation
    ************************************************************************************************
    */
    override fun redirectToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
    */
    private fun setupPresenter() {
        SaveurApplication.dataComponent.presenterComponent().inject(this)
        mPresenter.onAttachView(this)
    }
}