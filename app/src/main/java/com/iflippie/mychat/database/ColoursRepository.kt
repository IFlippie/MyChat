package com.iflippie.mychat.database

import android.content.Context
import com.iflippie.mychat.ColorItem

class ColoursRepository(context: Context) {

    private var colorDao: ColorDao

    init {
        val coloursRoomDatabase = ColoursRoomDatabase.getDatabase(context)
        colorDao = coloursRoomDatabase!!.colorDao()
    }

    suspend fun getAllColors(): List<ColorItem> {
        return colorDao.getAllColors()
    }

    suspend fun insertColor(color: ColorItem) {
        colorDao.insertColor(color)
    }

    suspend fun deleteColor(color: ColorItem) {
        colorDao.deleteColor(color)
    }

    suspend fun updateColor(color: ColorItem) {
        colorDao.updateColor(color)
    }

    suspend fun deleteAllColors() {
        return colorDao.deleteAllColors()
    }

}
