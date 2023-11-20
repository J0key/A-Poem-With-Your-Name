package com.example.room.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.room.R
import com.example.room.database.Poem
import com.example.room.database.PoemDao
import com.example.room.database.PoemRoomDatabase
import com.example.room.databinding.ActivityDetailBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var mNotesDao: PoemDao
    private lateinit var executorService: ExecutorService
    private var updateId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = PoemRoomDatabase.getDatabase(this)
        mNotesDao = db!!.PoemDao()!!
        var command = intent.getStringExtra("COMMAND")


        with(binding){

            if(command=="UPDATE"){
                binding.updatebtn.isVisible = true
                binding.savebtn.isVisible = false
                updateId = intent.getIntExtra("ID", 0)
                var item_title = intent.getStringExtra("TITTLE")
                var item_poem = intent.getStringExtra("POEM")

                binding.titleEdit.setText(item_title.toString())
                binding.poemEdit.setText(item_poem.toString())
            }else{
                binding.updatebtn.isVisible = false
                binding.savebtn.isVisible = true
            }


            savebtn.setOnClickListener(View.OnClickListener {
                if (validateInput()){
                    insert(
                        Poem(
                            title = titleEdit.text.toString(),
                            poem = poemEdit.text.toString()
                        )
                    )
                    setEmptyField()
                    val IntentToHome = Intent(this@DetailActivity, MainActivity::class.java)
                    Toast.makeText(this@DetailActivity, "Berhasil Menambahkan Data", Toast.LENGTH_SHORT).show()
                    startActivity(IntentToHome)
                }else{
                    Toast.makeText(this@DetailActivity, "Kolom Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show()
                }
            })

            updatebtn.setOnClickListener {
                if(validateInput()){
                    update(
                        Poem(
                            id = updateId,
                            title = titleEdit.text.toString(),
                            poem = poemEdit.text.toString()
                        )
                    )
                    updateId = 0
                    setEmptyField()
                    val IntentToHome = Intent(this@DetailActivity, MainActivity::class.java)
                    Toast.makeText(this@DetailActivity, "Berhasil Mengupdate Data", Toast.LENGTH_SHORT).show()
                    startActivity(IntentToHome)
                }else{
                    Toast.makeText(this@DetailActivity, "Kolom Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show()
                }
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
//        getAllNotes()
    }

    private fun setEmptyField() {
        with(binding) {
            titleEdit.setText("")
            poemEdit.setText("")
        }
    }

    private fun validateInput(): Boolean {
        with(binding) {
            if(titleEdit.text.toString()!="" && poemEdit.text.toString()!="" ){
                return true
            }else{
                return false
            }
        }

    }
}