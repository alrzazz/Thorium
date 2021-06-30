package com.example.thorium.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
const val CURRENT_ID = 0

@Entity
data class Status(
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP", name = "timestamp")
    var timestamp: String,

    @ColumnInfo(name = "latitude")
    var latitude: Double ?= null,

    @ColumnInfo(name = "longitude")
    var longitude: Double ?= null,

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
}