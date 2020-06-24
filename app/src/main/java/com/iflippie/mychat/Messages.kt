package com.iflippie.mychat

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Messages (
    val uid :String?,
    val email : String?,
    val name : String?,
    val tekst : String?
) : Parcelable {
    constructor() : this("",  "", "", "")
}