package com.iflippie.mychat

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class BetterMessage (
    val text : String?,
    val name : String?,
    val photoUrl : String?,
    val imageUrl : String?
) : Parcelable {
    constructor() : this("",  "", "", "")
}