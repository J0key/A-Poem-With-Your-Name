package com.example.room.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    private lateinit var poemAdapter: TabAdapter
    private lateinit var mPoemDao: PoemDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        executorService = Executors.newSingleThreadExecutor()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        poemAdapter = TabAdapter(emptyList()) { selectedPoem ->
        }

        val db = PoemRoomDatabase.getDatabase(this)
        mPoemDao = db!!.PoemDao()!!

        with(binding) {

            val intentToNoteActivity = Intent(this@MainActivity, DetailActivity::class.java)

            addBtn.setOnClickListener(View.OnClickListener {
                startActivity(intentToNoteActivity)
            })

            rvPoem.layoutManager = GridLayoutManager(this@MainActivity,1)
            rvPoem.adapter = poemAdapter
            getNotes()

            poemAdapter.onClickPoem = { clickedPoem ->
                delete(clickedPoem)
            }

        }

    }
    private fun getNotes() {
        mPoemDao.allNotes.observe(this) { newData ->
            poemAdapter.setData(newData)  // Assuming poemAdapter is an instance of TabAdapter
        }
    }

    private fun delete(poem: Poem) {
        executorService.execute { mPoemDao.delete(poem) }
    }

}