package com.example.thorium.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_ID = 0

@Entity
data class Status(
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "longitude")
    var longitude: Double,

    @ColumnInfo(name = "cell_id")
    var cellID: String ?= null,

    @ColumnInfo(name = "net_gen")
    var netGen: String ?= null,

    @ColumnInfo(name = "plmn_id")
    var plmnID: String ?= null,

    @ColumnInfo(name = "ac")
    var ac: String ?= null,

    @ColumnInfo(name = "arfcn")
    var arfcn: String ?= null,

    @ColumnInfo(name = "code")
    var code: String ?= null,
){
    @PrimaryKey(autoGenerate = false)
    var id : Int = CURRENT_ID

    override fun toString(): String {
        return "Cell ID: $cellID \n Network Generation: $netGen \n PLMN ID: $plmnID \n code: $code \n LAC or TAC: $ac \n ARFCN: $arfcn"
    }
}