package com.iflippie.mychat

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class User (
    val userId :String?,
    val name :String?,
    val email : String?,
    val rooms: List<String>
) : Parcelable {
    constructor() : this("",  "", "", listOf())
}