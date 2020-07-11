package com.example.messengerek

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.messengerek.modules.AddHighLits
import com.example.messengerek.modules.DodajMema
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_highlits.*
import kotlinx.android.synthetic.main.activity_memy.*
import kotlinx.android.synthetic.main.activity_ostatnie_wiadomosci.*
import kotlinx.android.synthetic.main.single_highlit.view.*
import kotlinx.android.synthetic.main.singlemem.view.*

class HighlitsActivity : AppCompatActivity() {

    companion object{

        val HIGH_TITLE = "HIGH_TITLE"
        val VIDEO_URL = "VIDEO"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highlits)

        fetchHighlits()
        bottom_nav_hl.setOnNavigationItemSelectedListener(bottomNav)
    }

    private fun fetchHighlits(){

        val ref = FirebaseDatabase.getInstance().getReference("highlits")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("Skrót", it.toString())
                    val highlite = it.getValue(AddHighLits::class.java)
                    if (highlite != null) {
                        adapter.add(SkrotItem(highlite))
                    }
                }

                adapter.setOnItemClickListener{item, view->
                    val skortItem = item as SkrotItem
                    val intent = Intent (view.context, SingleHighlitActivity::class.java)

                    intent.putExtra(HIGH_TITLE, item.highLits.tytul)
                    intent.putExtra(VIDEO_URL, item.highLits.videoImageURL)

                    startActivity(intent)


                }

                recyclerView_video.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }

        })

    }

    private val bottomNav = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_all -> {

                val intent = Intent (this, OstatnieWiadomosci::class.java)

                finish()

                startActivity(intent)


            }
            R.id.navigation_relacje-> {

                val intent = Intent (this, RelacjeActivity::class.java)
                startActivity(intent)

            }

            R.id.navigation_memy -> {

                val intent = Intent (this, MemyActivity::class.java)
                startActivity(intent)

            }
            R.id.navigation_profil -> {

                val intent = Intent (this, MyProfileActivity::class.java)
                startActivity(intent)

            }

        }
        true
    }


}

class SkrotItem(val highLits: AddHighLits): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.textView_highlit.text = highLits.tytul


    }

    override fun getLayout(): Int {
        return R.layout.single_highlit
    }
}

