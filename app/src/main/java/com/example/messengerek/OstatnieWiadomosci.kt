package com.example.messengerek

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.messengerek.modules.DodajNewsa
import com.example.messengerek.modules.DodajSuperNewsa
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_ostatnie_wiadomosci.*
import kotlinx.android.synthetic.main.activity_ostatnie_wiadomosci.view.*
import kotlinx.android.synthetic.main.activity_supernews.view.*
import kotlinx.android.synthetic.main.single_news_row.view.*
import kotlinx.android.synthetic.main.singlesupernews.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

open class OstatnieWiadomosci : AppCompatActivity() {





    companion object{

        var currentUser: User? = null
        val NEWS_IMG= "NEWS_IMG"
        val NEWS_TEXT = "NEWS_TEXT"
        val NEWS_TITLE = "NEWS_TITLE"
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ostatnie_wiadomosci)

        supportActionBar?.title = "SportAPP"

        fetchCurrentUser()
        czyzalogowany()
        fetchSuperNews()
        fetchNews()
        bottom_nav.setOnNavigationItemSelectedListener(bottomNav)

    }



    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref. addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("Ostatniewiadomości", "Zalogowany user: ${currentUser?.username}")

            }

        })
    }



    private fun czyzalogowany() {

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {

            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

    }

    private fun fetchNews(){


        val ref = FirebaseDatabase.getInstance().getReference("news")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("News", it.toString())
                    val news = it.getValue(DodajNewsa::class.java)
                    if (news != null) {
                        adapter.add(NewsItem(news))
                    }
                }

                adapter.setOnItemClickListener{item, view->
                    val newsItems = item as NewsItem
                    val intent = Intent (view.context, NewsActivity::class.java)
                    val obraz = intent.putExtra(NEWS_IMG, item.news.profileImage)
                    val tresc = intent.putExtra(NEWS_TEXT, item.news.tresc)
                    val tytul = intent.putExtra(NEWS_TITLE, item.news.tytul)
                    startActivity(intent)


                }
                lastNews_rv.adapter = adapter

            }
            override fun onCancelled(p0: DatabaseError) {
            }

        })

    }


    private fun fetchSuperNews(){

        val ref = FirebaseDatabase.getInstance().getReference("supernews")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("News", it.toString())
                    val supernews = it.getValue(DodajSuperNewsa::class.java)
                    if (supernews != null) {
                        adapter.add(SuperNewsItem(supernews))
                    }
                }

                adapter.setOnItemClickListener{item, view->
                    val supernewsItems = item as SuperNewsItem
                    val intent = Intent (view.context, NewsActivity::class.java)
                    val obraz = intent.putExtra(NEWS_IMG, item.ciach.profileImage)
                    val tresc = intent.putExtra(NEWS_TEXT, item.ciach.tresc)
                    val tytul = intent.putExtra(NEWS_TITLE, item.ciach.tytul)
                    startActivity(intent)


                }

                recyclerView_SuperNews.adapter = adapter

            }
            override fun onCancelled(p0: DatabaseError) {


            }



        })

    }

    fun registerPushToken(){
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                task ->
            val token = task.result?.token
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val map = mutableMapOf<String,Any>()
            map["pushToken"] = token!!

            FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val uid = FirebaseAuth.getInstance().uid
        when (item.itemId) {


            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

        return super.onCreateOptionsMenu(menu)
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

class NewsItem(val news: DodajNewsa): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(news.profileImage).into(viewHolder.itemView.imageView_newsRow)

        viewHolder.itemView.textView_data.text = news.formatedDate
        viewHolder.itemView.newstitle_textview.text = news.tytul

    }

    override fun getLayout(): Int {
        return R.layout.single_news_row
    }
}

class SuperNewsItem (val ciach: DodajSuperNewsa): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {

        Picasso.get().load(ciach.profileImage).into(viewHolder.itemView.imageView_SuperNews)
        viewHolder.itemView.textView_SuperNews.text = ciach.tytul

    }

    override fun getLayout(): Int {
        return R.layout.singlesupernews
    }

}




