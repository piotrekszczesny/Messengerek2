package com.example.messengerek

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)







        btn_dodaj_register.setOnClickListener{

            auth = FirebaseAuth.getInstance()


            val email= email_edittext_register.text.toString()
            val password = password_editText_register.text.toString()
            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(this, "Proszę uzupełnić wszystkie pola", Toast.LENGTH_SHORT).show()

                return@setOnClickListener

            }


            Log.d("MainActivity", "Email is: "+ email)
            Log.d("MainActivity", "Password: $password")



            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->

                if(task.isSuccessful) {

                    Log.d("Main", "create User with Email and Pass: success")
                    Toast.makeText(baseContext, "Udało się.",
                        Toast.LENGTH_SHORT).show()

                uploadImageToFirebaseStorage()
                } else {

                    Log.w("", "createUser: Failure", task.exception)
                    Toast.makeText(baseContext, "Nie udało się.",
                    Toast.LENGTH_SHORT).show()

                }

            }






        }

        already_have_account_text_view.setOnClickListener{



            // wczytaj layout logowania

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        select_photo.setOnClickListener{

            Log.d("MainAcitvity","Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }

    }
    var seletedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            Log.d("Main", "Wybrano zdjęcie")

            seletedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, seletedPhotoUri)

            selectphoto_register.setImageBitmap(bitmap)
            select_photo.alpha = 0f
  //          val bitmapDrawable = BitmapDrawable(bitmap)
  //         select_photo.setBackgroundDrawable(bitmapDrawable)
        }

    }

    private fun uploadImageToFirebaseStorage() {
        if (seletedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(seletedPhotoUri!!)
            .addOnSuccessListener {

                Log.d ("Main", "Udało się wgrać obraz: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {

                    Log.d("Main","File Location is: $it")

                    saveUserToDataBase(it.toString())
                }
            }
            .addOnFailureListener{

            }

    }
    private fun saveUserToDataBase(profileImageURL: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, editText_login_register.text.toString(),profileImageURL)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Main", "Nareszcie mamy użytkownia w bazie")

                val intent = Intent(this, OstatnieWiadomosci::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
    }
}
@Parcelize
class User(val uid: String, val username: String, val profileImage : String):Parcelable {

    constructor() : this("","","")
}
