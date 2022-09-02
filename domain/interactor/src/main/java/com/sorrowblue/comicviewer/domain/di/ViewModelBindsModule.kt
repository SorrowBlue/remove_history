package com.sorrowblue.comicviewer.domain.di

import com.sorrowblue.comicviewer.domain.usecase.GetBookshelfSettingsUseCase
import com.sorrowblue.comicviewer.domain.usecase.LoadFileUseCase
import com.sorrowblue.comicviewer.domain.usecase.LoadLibraryUseCase
import com.sorrowblue.comicviewer.domain.usecase.LoadPageUseCase
import com.sorrowblue.comicviewer.domain.usecase.LoadSettingsUseCase
import com.sorrowblue.comicviewer.domain.usecase.RegisterLibraryUseCase
import com.sorrowblue.comicviewer.domain.usecase.UpdateBookshelfSettingsUseCase
import com.sorrowblue.comicviewer.domain.usecase.UpdateHistoryUseCase
import com.sorrowblue.comicviewer.domain.usecase.UpdateSettingsUseCase
import com.sorrowblue.comicviewer.domain.usecase.interactor.GetBookshelfSettingsInteractor
import com.sorrowblue.comicviewer.domain.usecase.interactor.LoadFileInteractor
import com.sorrowblue.comicviewer.domain.usecase.interactor.LoadLibraryInteractor
import com.sorrowblue.comicviewer.domain.usecase.interactor.LoadPageInteractor
import com.sorrowblue.comicviewer.domain.usecase.interactor.LoadSettingsInteractor
import com.sorrowblue.comicviewer.domain.usecase.interactor.RegisterLibraryInteractor
import com.sorrowblue.comicviewer.domain.usecase.interactor.UpdateBookshelfSettingsInteractor
import com.sorrowblue.comicviewer.domain.usecase.interactor.UpdateHistoryInteractor
import com.sorrowblue.comicviewer.domain.usecase.interactor.UpdateSettingsInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class ViewModelBindsModule {

    @Binds
    abstract fun bindLoadFileUseCase(interactor: LoadFileInteractor): LoadFileUseCase

    @Binds
    abstract fun bindLoadLibraryUseCase(interactor: LoadLibraryInteractor): LoadLibraryUseCase

    @Binds
    abstract fun bindRegisterLibraryUseCase(interactor: RegisterLibraryInteractor): RegisterLibraryUseCase

    @Binds
    abstract fun bindUpdateHistoryUseCase(interactor: UpdateHistoryInteractor): UpdateHistoryUseCase

    @Binds
    abstract fun bindLoadPageUseCase(interactor: LoadPageInteractor): LoadPageUseCase

    @Binds
    abstract fun bindGetBookshelfSettingsUseCase(interactor: GetBookshelfSettingsInteractor): GetBookshelfSettingsUseCase

    @Binds
    abstract fun bindUpdateBookshelfSettings(interactor: UpdateBookshelfSettingsInteractor): UpdateBookshelfSettingsUseCase

    @Binds
    abstract fun bindLoadSettings(interactor: LoadSettingsInteractor): LoadSettingsUseCase

    @Binds
    abstract fun bindUpdateSettings(interactor: UpdateSettingsInteractor): UpdateSettingsUseCase
}
