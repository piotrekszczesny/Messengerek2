package com.example.messengerek

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.messengerek.modules.DodajRelacje
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
import kotlinx.android.synthetic.main.activity_relacje.*
import kotlinx.android.synthetic.main.single_news_row.view.*
import kotlinx.android.synthetic.main.single_relation_layout.view.*
import kotlinx.android.synthetic.main.singlemem.view.*

class RelacjeActivity : AppCompatActivity() {


    companion object{


        val RELACJA_IMG= "RELACJA_IMG"
        val RELACJA_TEXT = "RELACJA_TEXT"
        val RELACJA_TITLE = "RELACJA_TITLE"
        val RELACJA_DATA = "RELACJA_DATA"
        val RELACJA_GOSP = "RELACJA_GOSP"
        val RELACJA_GOSCIE = "RELACJA_GOSCIE"
        val RELACJA_DRUZYNA_GOSP = "RELACJA_DRUZYNA_GOSP"
        val RELACJA_DRUZYNA_GOSCI = "RELACJA_DRUZYNA_GOSCI"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relacje)

        fetchRelacje()
        bottom_nav_relacje.setOnNavigationItemSelectedListener(bottomNav)
    }
    private fun fetchRelacje(){



        val ref = FirebaseDatabase.getInstance().getReference("relacje")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("Relacje", it.toString())
                    val relacja = it.getValue(DodajRelacje::class.java)
                    if (relacja != null) {
                        adapter.add(RelacjeItem(relacja))
                    }
                }

                adapter.setOnItemClickListener{item, view->
                    val relacjeItem = item as RelacjeItem
                    val intent = Intent (view.context, SzczegolyRelacjiActivity::class.java)
                    val obraz = intent.putExtra(RelacjeActivity.RELACJA_IMG, item.relacje.profileImage)
                    val tresc = intent.putExtra(RelacjeActivity.RELACJA_TEXT, item.relacje.tresc)
                    val tytul = intent.putExtra(RelacjeActivity.RELACJA_TITLE, item.relacje.tytul)
                    val data = intent.putExtra(RelacjeActivity.RELACJA_DATA, item.relacje.dataR)
                    val skladGosp = intent.putExtra(RelacjeActivity.RELACJA_GOSP, item.relacje.skladgosp)
                    val skladGosci = intent.putExtra(RelacjeActivity.RELACJA_GOSCIE, item.relacje.skladgosci)
                    val gosp = intent.putExtra(RelacjeActivity.RELACJA_DRUZYNA_GOSP, item.relacje.gosp)
                    val goscie = intent.putExtra(RelacjeActivity.RELACJA_DRUZYNA_GOSCI, item.relacje.goscie)

                    startActivity(intent)


                }

                RelacjeRV.adapter = adapter
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
class RelacjeItem(val relacje: DodajRelacje): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.relationTitle_textview.text = relacje.tytul
        viewHolder.itemView.relationDate_textview2.text = relacje.dataR


        Picasso.get().load(relacje.profileImage).into(viewHolder.itemView.imageView_RelationRow)


    }

    override fun getLayout(): Int {
        return R.layout.single_relation_layout
    }


}





