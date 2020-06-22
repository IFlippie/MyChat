package com.iflippie.mychat

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class ChatRoom (
    val uid :String?,
    val starter :String?,
    val receiver : String?,
    val messages: List<String>
) : Parcelable {
    constructor() : this("",  "", "", listOf())
}