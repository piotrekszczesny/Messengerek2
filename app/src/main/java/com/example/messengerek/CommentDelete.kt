package com.example.messengerek

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_comment_delete.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.single_comment_row.*
import kotlin.system.exitProcess

class CommentDelete : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_delete)

        val cuid = intent.getStringExtra(NewsActivity.COMMENT_UID)

        val uid = OstatnieWiadomosci.currentUser?.username
        val newstitle = intent.getStringExtra(NewsActivity.COMMENT_TITLE)
        textView_commentLogin2.text = cuid
        textView_commentText2.text = intent.getStringExtra(NewsActivity.COMMENT_TEXT)
        Picasso.get().load(NewsActivity.COMMENT_IMG).into(imageView_comment2)

        if (cuid == uid) {
            btn_usunKomentarz.setOnClickListener {
                showDeleteDialog()
            }


        } else {
            btn_usunKomentarz.setOnClickListener {


                Toast.makeText(this, "Nie możesz tego usunąć.", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun usunKomentarz(){

        val cdate = intent.getStringExtra(NewsActivity.COMMENT_DATE)
        val newstitle = intent.getStringExtra(NewsActivity.COMMENT_TITLE)
        FirebaseDatabase.getInstance().getReference().root.child("/comments/$newstitle/$cdate").setValue(null)
    }

    fun showDeleteDialog(){

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Czy jesteś pewny?")


        builder.setNegativeButton("Anuluj", DialogInterface.OnClickListener{ dialog, which ->
            dialog.dismiss()
        } )

        builder.setNeutralButton("Nie", DialogInterface.OnClickListener{ dialog, which ->
            dialog.dismiss()
        })

        builder.setPositiveButton("Tak", DialogInterface.OnClickListener{ dialog, which ->

            usunKomentarz()
            Toast.makeText(this, "Komentarz usunięty", Toast.LENGTH_SHORT).show()

            finish()







        })


        val alertDialog: android.app.AlertDialog = builder.create()
        alertDialog.show()
    }



}