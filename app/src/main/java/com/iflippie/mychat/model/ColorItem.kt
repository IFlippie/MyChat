package com.iflippie.mychat.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "colorItemTable")
data class ColorItem(
    @PrimaryKey()
    @ColumnInfo(name = "color")
    var hex: String,

    var name: String
) : Parcelable {
    fun getImageUrl() = "http://singlecolorimage.com/get/$hex/1080x1080"
}