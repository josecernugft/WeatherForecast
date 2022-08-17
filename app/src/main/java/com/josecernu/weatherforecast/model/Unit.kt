package com.josecernu.weatherforecast.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "settings_tbl")
data class Unit(
    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "unit")
    val unit: String
)
