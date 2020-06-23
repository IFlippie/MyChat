package com.iflippie.mychat

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class ChatRoom (
    val uid :String?,
    val p1 : String?,
    val p2 : String?,
    val messagesP1: List<String>,
    val messagesP2: List<String>
) : Parcelable {
    constructor() : this("",  "", "", listOf(), listOf())
}