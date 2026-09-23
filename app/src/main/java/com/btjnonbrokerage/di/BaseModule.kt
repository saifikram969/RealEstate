package com.btjnonbrokerage.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class BaseModule {

    @Provides
    fun provideUserInformation(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("user_info", Context.MODE_PRIVATE)
    }

}