package com.example.room.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.room.R
import com.example.room.database.Poem
import com.example.room.database.PoemDao
import com.example.room.database.PoemRoomDatabase
import com.example.room.databinding.ActivityDetailBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var mPoemDao: PoemDao
    private lateinit var executorService: ExecutorService
    private lateinit var PoemAdapter: TabAdapter
    private var updateId : Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = PoemRoomDatabase.getDatabase(this)
        mPoemDao = db!!.PoemDao()!!

        PoemAdapter = TabAdapter(emptyList()) { selectedNote ->

        }

        with(binding){
            val intentToMainActivity = Intent(this@DetailActivity, MainActivity::class.java)
            savebtn.setOnClickListener(View.OnClickListener {
                insert(
                    Poem(
                        title = titleEdit.text.toString(),
                        poem = poemEdit.text.toString(),
                    )
                )
                resetForm()
                startActivity(intentToMainActivity)
            })
        }
    }


    private fun getNotes(){
        mPoemDao.allNotes.observe(this) { notes ->
            PoemAdapter.setData(notes)
        }
    }

    private fun insert(poem: Poem){
        executorService.execute{mPoemDao.insert(poem)}
    }

    private fun update(poem: Poem){
        executorService.execute{mPoemDao.update(poem)}
    }

    private fun delete(poem: Poem){
        executorService.execute{mPoemDao.delete(poem)}
    }

    override fun onResume() {
        super.onResume()
        getNotes()
    }

    private fun resetForm(){
        with(binding){
            titleEdit.setText("")
            poemEdit.setText("")
        }
    }
}