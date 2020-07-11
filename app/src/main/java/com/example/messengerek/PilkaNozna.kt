package com.example.messengerek

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.messengerek.modules.DodajNewsaPilka
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_ostatnie_wiadomosci.*
import kotlinx.android.synthetic.main.activity_ostatnie_wiadomosci.bottom_nav
import kotlinx.android.synthetic.main.activity_pilka_nozna.*
import kotlinx.android.synthetic.main.single_news_row.view.*

class PilkaNozna : AppCompatActivity() {


    companion object{

        var currentUser: User? = null
        val NEWS_IMG= "NEWS_IMG"
        val NEWS_TEXT = "NEWS_TEXT"
        val NEWS_TITLE = "NEWS_TITLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.title = "Piłka Nożna"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilka_nozna)

        fetchNews()
        bottom_nav.setOnNavigationItemSelectedListener(bottomNav)
    }

    private fun fetchNews(){

        val ref = FirebaseDatabase.getInstance().getReference("pilka_nozna")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("News", it.toString())
                    val pilka = it.getValue(DodajNewsaPilka::class.java)
                    if (pilka != null) {
                        adapter.add(NewsItemPn(pilka))
                    }
                }

                adapter.setOnItemClickListener{item, view->
                    val newsItems = item as NewsItemPn
                    val intent = Intent (view.context, NewsActivity::class.java)
                    val obraz = intent.putExtra(OstatnieWiadomosci.NEWS_IMG, item.pilka_nozna.profileImage)
                    val tresc = intent.putExtra(OstatnieWiadomosci.NEWS_TEXT, item.pilka_nozna.tresc)
                    val tytul = intent.putExtra(OstatnieWiadomosci.NEWS_TITLE, item.pilka_nozna.tytul)
                    startActivity(intent)


                }
                lastNews_rv_PN.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }

        })

    }

  public  val bottomNav = BottomNavigationView.OnNavigationItemSelectedListener {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val uid = FirebaseAuth.getInstance().uid
        when (item.itemId) {


            R.id.menu_hightlits ->{

                val intent = Intent(this, HighlitsActivity::class.java)

                startActivity(intent)
            }



        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_highlits, menu)

        return super.onCreateOptionsMenu(menu)
    }
}

class NewsItemPn(val pilka_nozna: DodajNewsaPilka): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(pilka_nozna.profileImage).into(viewHolder.itemView.imageView_newsRow)
        viewHolder.itemView.textView_data.text = pilka_nozna.formatedDate


        viewHolder.itemView.newstitle_textview.text = pilka_nozna.tytul

    }

    override fun getLayout(): Int {
        return R.layout.single_news_row
    }
}
