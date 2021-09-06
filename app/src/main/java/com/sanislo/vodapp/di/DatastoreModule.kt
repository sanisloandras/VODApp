package com.sanislo.vodapp.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.sanislo.vodapp.data.datastore.VodAppDataStore
import com.sanislo.vodapp.data.datastore.VodAppDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferencesStorageModule {

    private val Context.dataStore by preferencesDataStore(
        name = VodAppDataStore.PREFS_NAME,
    )

    @Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): VodAppDataStore =
        VodAppDataStoreImpl(context.dataStore)
}
