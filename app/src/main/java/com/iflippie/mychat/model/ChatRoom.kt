package com.iflippie.mychat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoom (
    val uid :String?,
    val p1 : String?,
    val p1Email : String?,
    val p1Name : String?,
    val p2 : String?,
    val p2Email : String?,
    val p2Name : String?
) : Parcelable {
    constructor() : this("",  "", "", "",  "", "", "")
}