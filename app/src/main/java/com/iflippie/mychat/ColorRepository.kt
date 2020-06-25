package com.iflippie.mychat

import com.iflippie.mychat.model.ColorItem

class ColorRepository {
    fun getColorItems(): List<ColorItem> {
        return arrayListOf(
            ColorItem("000000", "Black"),
            ColorItem("FF0000", "Red"),
            ColorItem("0000FF", "Blue"),
            ColorItem("FFFF00", "Yellow"),
            ColorItem("008000", "Green"),
            ColorItem("00ff00", "Lime"),
            ColorItem("ff00ff", "Pink")
        )
    }
}