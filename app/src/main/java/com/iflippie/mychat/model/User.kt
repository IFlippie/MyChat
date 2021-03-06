package com.iflippie.mychat.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val userId :String?,
    val name :String?,
    val email : String?
) : Parcelable {
    constructor() : this("",  "", "")
}