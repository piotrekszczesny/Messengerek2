package com.example.messengerek

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ShareActionProvider
import android.widget.Switch
import com.example.messengerek.OstatnieWiadomosci.Companion.currentUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_memy.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_my_profile.view.*
import kotlinx.android.synthetic.main.singlemem.view.*
import kotlinx.android.synthetic.main.singleprofilactivity.view.*
import kotlinx.android.synthetic.main.singleprofilactivity.view.textView_profile

class MyProfileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        fetchProfile()
        supportActionBar?.title = "Mój Profil"


    }



    private fun fetchProfile(){



        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("Memy", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(ProfileItem(user))

                    }

                }
                textView_profile2.text = currentUser?.username

                Picasso.get().load(currentUser?.profileImage).into(imageView_profile2)

            }


            override fun onCancelled(p0: DatabaseError) {
            }

        })

    }

}


class ProfileItem(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return 0
    }
}

