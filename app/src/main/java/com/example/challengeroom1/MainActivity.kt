package com.example.challengeroom1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengeroom1.room.Constant
import com.example.challengeroom1.room.dbsmksa
import com.example.challengeroom1.room.tbsiswa
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val db by lazy { dbsmksa(this) }
    private lateinit var siswaAdapter: SiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        halEdit()
        setupRecyclerView()
    }
    override fun onStart() {
        super.onStart()
        loadtbsis()

    }
    fun loadtbsis(){
        CoroutineScope(Dispatchers.IO).launch {
            val siswa = db.tbsisDao().tampilsemua()
            Log.d("MainActivity","dbResponse:$siswa")
            withContext(Dispatchers.Main) {
                siswaAdapter.setData(siswa)
            }

        }
    }
    private fun halEdit() {
        BtnInput.setOnClickListener {
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(tbsisNis: Int,intentType: Int){
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_nis",tbsisNis)
                .putExtra("intent_type",intentType)
        )
    }

    private fun setupRecyclerView () {
        siswaAdapter = SiswaAdapter(arrayListOf(),object : SiswaAdapter.OnAdapterListener{
            override fun onClick(tbsis: tbsiswa) {
               intentEdit(tbsis.nis,0)
            }

            override fun onUpdate(tbsis: tbsiswa) {
               intentEdit(tbsis.nis,Constant.TYPE_UPDATE)
            }

            override fun onDelete(tbsis: tbsiswa) {
                deleteDialog(tbsis)
            }

        })
        listdatasiswa.apply {
           layoutManager = LinearLayoutManager(applicationContext)
            adapter = siswaAdapter
        }
    }
    private fun deleteDialog(tbsis: tbsiswa){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin Hapus ${tbsis.nama}?")
            setNegativeButton("Batal") {dialogInterface,i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus") {dialogInterface,i ->
                dialogInterface.dismiss()
                CoroutineScope (Dispatchers.IO).launch {
                    db.tbsisDao().deltbsiswa(tbsis)
                    loadtbsis()
                }
            }
        }
        alertDialog.show()
    }
}