package com.github.odaridavid.pker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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
@Dao
interface ColorsDao {

    @Query("SELECT * FROM colors")
    fun loadAllColors(): List<Color>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveColor(color: Color)

    @Query("DELETE FROM colors WHERE id = :id")
    fun deleteColor(id: Int)
}