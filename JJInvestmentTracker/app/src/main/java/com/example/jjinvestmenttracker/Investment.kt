package com.example.jjinvestmenttracker

import android.os.Parcel
import android.os.Parcelable

data class Investment(
    val investmentId: Int,
    val investmentName: String,
    val amountInvested: Double,
    val returnRate: Float,
    val totalGrowth: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readFloat(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(investmentId)
        parcel.writeString(investmentName)
        parcel.writeDouble(amountInvested)
        parcel.writeFloat(returnRate)
        parcel.writeDouble(totalGrowth)
    }

    override fun describeContents(): Int
    {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Investment>
    {
        override fun createFromParcel(parcel: Parcel): Investment {
            return Investment(parcel)
        }

        override fun newArray(size: Int): Array<Investment?> {
            return arrayOfNulls(size)
        }
    }
}