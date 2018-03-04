package com.saveurmarche.saveurmarche.ui.launch

import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import com.saveurmarche.saveurmarche.ui.base.BasePresenter
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LaunchPresenter @Inject constructor(private val marketManager: MarketsManager) :
        BasePresenter<LaunchContract.View>(),
        LaunchContract.Presenter {
    /*
    ************************************************************************************************
    ** LaunchContract.Presenter implementation
    ************************************************************************************************
    */
    override fun setupView() {
        registerDisposable(marketManager.fetchMarkets()
                .subscribe(
                        {
                            view?.redirectToMainActivity()
                        },
                        {
                            view?.redirectToMainActivity()
                        }
                )
        )
    }
}
