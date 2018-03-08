package com.saveurmarche.saveurmarche.component.data;

import android.support.annotation.NonNull;

import com.saveurmarche.saveurmarche.SaveurApplication;
import com.saveurmarche.saveurmarche.component.application.AppModule;
import com.saveurmarche.saveurmarche.data.resource.ResourcesProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ResourcesModule {

    /**
     * @param context provided by {@link AppModule#provideApplication()}
     */
    @Provides
    @NonNull
    protected ResourcesProvider provideResources(@NonNull final SaveurApplication context) {
        return new ResourcesProvider(context);
    }

}
