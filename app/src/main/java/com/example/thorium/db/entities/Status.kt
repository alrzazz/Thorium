package com.example.thorium.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
const val CURRENT_ID = 0

@Entity
data class Status(
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    var timestamp: String ?= null,
    var latitude: Double ?= null,
    var longitude: Double ?= null,
    var cellID: String ?= null,
    var netGen: String ?= null,
    var plmnID: String ?= null,
    var ac: String ?= null,
    var arfcn: String ?= null,
    var code: String ?= null,
){
    @PrimaryKey(autoGenerate = false)
    var id : Int = CURRENT_ID
}