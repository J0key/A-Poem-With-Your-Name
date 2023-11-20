package com.example.room.database


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PoemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(poem: Poem)

    @Update
    fun update(poem: Poem)

    @Delete
    fun delete(poem: Poem)

    @get:Query("SELECT* from poem_table ORDER BY id ASC")
    val allNotes: LiveData<List<Poem>>
}