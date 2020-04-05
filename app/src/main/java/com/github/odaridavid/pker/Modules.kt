package com.github.odaridavid.pker

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *
 * Copyright 2020 David Odari
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *            http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 **/
val db = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            PkerDatabase::class.java,
            Constants.PKER_DATABASE_NAME
        )
    }

    fun provideColorsDao(db: PkerDatabase): ColorsDao = db.colorsDao()
    single { provideColorsDao(db = get()) }

}

val viewModels = module {

    viewModel { CameraViewModel() }

}
