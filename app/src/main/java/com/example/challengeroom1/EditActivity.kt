package com.example.challengeroom1

import android.os.Bundle
import android.util.Log
import android.view.AbsSavedState
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.challengeroom1.room.Constant
import com.example.challengeroom1.room.dbsmksa
import com.example.challengeroom1.room.tbsiswa
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity(){
    val db by lazy { dbsmksa(this) }
    private var tbsisNis: Int =0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupView()
        tombolPerintah()
        tbsisNis = intent.getIntExtra("intent_nis",0)
        Toast.makeText(this,tbsisNis.toString(), Toast.LENGTH_SHORT).show()
    }
    fun setupView(){
        val intentType = intent.getIntExtra("intent_type",0)
        when (intentType) {
            Constant.TYPE_CREATE -> {
                BtnUpdate.visibility = View.GONE

            }
            Constant.TYPE_READ -> {
                BtnUpdate.visibility = View.GONE
                BtnSimpan.visibility = View.GONE
                ETnis.visibility = View.GONE
                tampilsiswa()

            }
            Constant.TYPE_UPDATE -> {
                BtnSimpan.visibility = View.GONE
                ETnis.visibility = View.GONE
                tampilsiswa()

            }
        }
    }


    fun tombolPerintah(){
        BtnSimpan.setOnClickListener{
            CoroutineScope (Dispatchers.IO).launch {
                db.tbsisDao().addtbsiswa(
                    tbsiswa(ETnis.text.toString().toInt(),ETnama.text.toString(),ETkelas.text.toString(),ETalamat.text.toString())
                )
                finish()
            }
        }
        BtnUpdate.setOnClickListener{
            CoroutineScope (Dispatchers.IO).launch {
                db.tbsisDao().updatetbsiswa(
                    tbsiswa(tbsisNis,ETnama.text.toString(),ETkelas.text.toString(),ETalamat.text.toString())
                )
                finish()
            }
        }
    }
    fun tampilsiswa(){
        tbsisNis = intent.getIntExtra("intent_nis",0)
        CoroutineScope(Dispatchers.IO).launch {
            val siswa = db.tbsisDao().tampilid(tbsisNis)[0]
            //ETnis.setText(siswa.nis)
            ETnama.setText(siswa.nama)
            ETkelas.setText(siswa.kelas)
            ETalamat.setText(siswa.alamat)
        }
    }
}