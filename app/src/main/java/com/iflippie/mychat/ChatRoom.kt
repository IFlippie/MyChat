package com.iflippie.mychat

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class ChatRoom (
    val uid :String?,
    val p1 : String?,
    val p1Email : String?,
    val p1Name : String?,
    val p2 : String?,
    val p2Email : String?,
    val p2Name : String?,
    val messagesList: List<Messages>
) : Parcelable {
    constructor() : this("",  "", "", "",  "", "", "",listOf())
}