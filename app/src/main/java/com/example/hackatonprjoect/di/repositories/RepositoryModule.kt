package com.example.hackatonprjoect.di.repositories


import android.content.Context
import com.example.hackatonprjoect.core.network.ApiService
import com.example.hackatonprjoect.data.repositories.MainRepository
import com.example.hackatonprjoect.data.repositories.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
         apiService: ApiService
    ): MainRepository {
        return MainRepository(apiService)
    }


    @Singleton
    @Provides
    fun provideNotificationRepository(
        apiService: ApiService,
        @ApplicationContext context: Context
    ): NotificationRepository {
        return NotificationRepository(context, apiService)
    }


//    fun provideAppObserver(
//    ): AppObserver {
//        return AppObserver()
//    }


}
