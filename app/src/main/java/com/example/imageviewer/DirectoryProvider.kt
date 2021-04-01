package com.example.imageviewer

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Inject

interface DirectoryProvider {
    var directory: File?
}

class DefaultDirectoryProvider @Inject constructor(@ApplicationContext context: Context): DirectoryProvider {
    override var directory = context.getExternalFilesDir(null)
}

@Module
@InstallIn(SingletonComponent::class, FragmentComponent::class, ViewModelComponent::class)
abstract class DirectoryProviderModule {

    @Binds
    abstract fun getDefaultProvider(provider: DefaultDirectoryProvider): DirectoryProvider
}