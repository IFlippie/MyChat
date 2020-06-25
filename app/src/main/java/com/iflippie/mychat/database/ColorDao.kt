package com.iflippie.mychat.database

import androidx.room.*
import com.iflippie.mychat.ColorItem

@Dao
interface ColorDao {
    @Query("SELECT * FROM colorItemTable")
    suspend fun getAllColors() : List<ColorItem>
    @Insert
    suspend fun insertColor(color: ColorItem)

    @Delete
    suspend fun deleteColor(color: ColorItem)

    @Update
    suspend fun updateColor(color: ColorItem)

    @Query("DELETE FROM colorItemTable")
    suspend fun deleteAllColors()


}