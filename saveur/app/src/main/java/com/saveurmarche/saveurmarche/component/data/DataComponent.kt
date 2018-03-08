package com.saveurmarche.saveurmarche.component.data

import com.saveurmarche.saveurmarche.component.application.AppComponent
import com.saveurmarche.saveurmarche.component.presenter.PresenterComponent
import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import dagger.Component

@DataScope
@Component(
        dependencies = [AppComponent::class],
        modules = [
            DatabaseModule::class,
            NetworkModule::class,
            ResourcesModule::class
        ]
)
interface DataComponent {
    /*
    ************************************************************************************************
    ** Sub module
    ************************************************************************************************
     */
    fun presenterComponent(): PresenterComponent

    /*
    ************************************************************************************************
    ** Public field (accessible for child)
    ************************************************************************************************
     */
    fun marketManager(): MarketsManager
}