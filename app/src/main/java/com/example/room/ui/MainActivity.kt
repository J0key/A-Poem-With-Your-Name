package com.example.room.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room.database.Poem
import com.example.room.database.PoemDao
import com.example.room.database.PoemRoomDatabase
import com.example.room.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNotesDao: PoemDao
    private lateinit var executorService: ExecutorService
    private lateinit var ArrayData : LiveData<List<Poem>>
    private var updateId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = PoemRoomDatabase.getDatabase(this)
        mNotesDao  = db!!.PoemDao()!!
        ArrayData = db!!.PoemDao()!!.allNotes
        getAllNotes()

        with(binding) {
            addBtn.setOnClickListener{
                val intentToInput = Intent(this@MainActivity, DetailActivity::class.java)
                intentToInput.putExtra("COMMAND", "ADD")
                startActivity(intentToInput)
            }
        }
    }

    private fun getAllNotes() {
        mNotesDao.allNotes.observe(this) { poems ->
            if (poems.isNotEmpty()) { // Periksa apakah daftar catatan tidak kosong
                binding.rvPoem.isVisible = true
//                binding.textEmpty.isVisible = false
                val recyclerAdapter = TabAdapter(poems)
                binding.rvPoem.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    setHasFixedSize(true)
                    adapter = recyclerAdapter
                }
            }else{
                binding.rvPoem.isVisible = false
//                binding.textEmpty.isVisible = true
            }
        }
    }

    private fun insert(poem: Poem) {
        executorService.execute { mNotesDao.insert(poem) }
    }

    private fun delete(poem: Poem) {
        executorService.execute { mNotesDao.delete(poem) }
    }

    private fun update(poem: Poem) {
        executorService.execute { mNotesDao.update(poem) }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
}