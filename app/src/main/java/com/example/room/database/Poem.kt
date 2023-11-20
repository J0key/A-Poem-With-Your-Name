package com.example.room.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poem_table")
data class Poem(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int=0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poem")
    val poem: String,
)