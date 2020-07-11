package com.example.messengerek

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_memy.*
import kotlinx.android.synthetic.main.activity_ostatnie_wiadomosci.*
import kotlinx.android.synthetic.main.single_news_row.view.*
import kotlinx.android.synthetic.main.singlemem.view.*

class MemyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memy)
        supportActionBar?.title = "Memy"

        fetchMems()
        bottom_nav_memy.setOnNavigationItemSelectedListener(bottomNav)
    }

    private fun fetchMems(){



        val ref = FirebaseDatabase.getInstance().getReference("memy")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("Memy", it.toString())
                    val mem = it.getValue(DodajMema::class.java)
                    if (mem != null) {
                        adapter.add(MemItem(mem))
                    }
                }
                recyclerView_memy.adapter = adapter
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
            R.id.navigation_god-> {

                val intent = Intent (this, PilkaNozna::class.java)
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
class MemItem(val memy: DodajMema): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.textView_mem.text = memy.tytul


        Picasso.get().load(memy.profileImage).into(viewHolder.itemView.imageView_mem)


    }

    override fun getLayout(): Int {
        return R.layout.singlemem
    }
}

