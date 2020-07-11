package com.example.messengerek

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuView
import androidx.core.net.toUri
import com.google.api.LabelDescriptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_comment_delete.*

import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_news.view.*
import kotlinx.android.synthetic.main.activity_ostatnie_wiadomosci.*
import kotlinx.android.synthetic.main.comments_add_activity.*
import kotlinx.android.synthetic.main.comments_add_activity.view.*
import kotlinx.android.synthetic.main.single_comment_row.*

import kotlinx.android.synthetic.main.single_comment_row.view.*
import kotlinx.android.synthetic.main.single_comment_row.view.textView_commentLogin
import kotlinx.android.synthetic.main.single_news_row.view.*
import kotlinx.android.synthetic.main.user_row_new_message.*
import org.w3c.dom.Comment
import java.text.SimpleDateFormat
import java.util.*


class NewsActivity : AppCompatActivity() {

    companion object {

        val TAG = "Wiadomości"
        var currentUser: User? = null
        val COMMENT_UID = "COMMENT_UID"
        val COMMENT_DATE = "COMMENT_DATE"
        val COMMENT_TITLE = "COMMENT_TITLE"
        val COMMENT_TEXT = "COMMENT_TEXT"
        val COMMENT_IMG = "COMMENT_IMG"
    }

    private var fontSize = 16f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsTitle = intent.getStringExtra(OstatnieWiadomosci.NEWS_TITLE)


        supportActionBar?.title = newsTitle



        fetchsinglenews()
        fetchcomments()
        textView_tytul_news.fontFeatureSettings
        btn_comment_news.setOnClickListener{



            dodajKomentarz()

            recreate()

            editText_comment_news.text.clear()

        }
        button_txtSize.setOnClickListener{


            fontSize += 2f
            textView__tresc_news.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

        }

        btn_zmniejszTekst.setOnClickListener {

            fontSize -= 2f

            textView__tresc_news.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

        }
        

    }

    private fun fetchsinglenews(){

        val newsimage = intent.getStringExtra(OstatnieWiadomosci.NEWS_IMG)
        val newsTitle = intent.getStringExtra(OstatnieWiadomosci.NEWS_TITLE)
        val newstext = intent.getStringExtra(OstatnieWiadomosci.NEWS_TEXT)



        Picasso.get().load(newsimage).into(imageView_image_news)
        textView__tresc_news.text = newstext
       textView_tytul_news.text = newsTitle

        btn_comment_news.setOnClickListener{

            dodajKomentarz()

            Toast.makeText(this, "Komentarz dodany prawidłowo", Toast.LENGTH_SHORT).show()

        }


    }




    private fun fetchcomments(){
        val uid = FirebaseAuth.getInstance().uid
        val newstitle = intent.getStringExtra(OstatnieWiadomosci.NEWS_TITLE)
        val ref = FirebaseDatabase.getInstance().getReference("/comments/$newstitle/")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("Comments", it.toString())
                    val comment = it.getValue(AddComment::class.java)
                    if (comment != null) {
                        adapter.add(CommentItem(comment))
                    }

                }
                adapter.setOnItemClickListener{item, view->
                    val cItem = item as CommentItem
                    val intent = Intent (view.context, CommentDelete::class.java)
                    val cuid = intent.putExtra(NewsActivity.COMMENT_UID, item.comment.currentUser)
                    val cdate = intent.putExtra(NewsActivity.COMMENT_DATE, item.comment.date)
                    val ctitle = intent.putExtra(NewsActivity.COMMENT_TITLE, item.comment.newstitle)
                    val ctext = intent.putExtra(NewsActivity.COMMENT_TEXT, item.comment.tresc)
                    val cimg = intent.putExtra(NewsActivity.COMMENT_IMG, item.comment.image)
                    startActivity(intent)
                    finish()




                }

                recyclerView_comment_news.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }



    private fun dodajKomentarz() {

        val tresc = editText_comment_news.text.toString()
        val newstitle = intent.getStringExtra(OstatnieWiadomosci.NEWS_TITLE)
        val currentUser = OstatnieWiadomosci.currentUser ?:return
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val formatedDate = formatter.format(date)
        val image = OstatnieWiadomosci.currentUser?.profileImage.toString()
        val uid = OstatnieWiadomosci.currentUser?.uid.toString()



        if (tresc == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/comments/$newstitle/$formatedDate")
        val dodajKomentarze= AddComment(currentUser.username, tresc, newstitle,image,formatedDate)



        reference.setValue(dodajKomentarze)
            .addOnSuccessListener {
                Log.d(NewsActivity.TAG, "Zapisano komentarz: ${reference.key}")
            }


    }

}
@Parcelize
class AddComment ( val currentUser: String, val tresc: String, val newstitle: String, val image: String, val date: String): Parcelable {

    constructor() : this("", "", "", "", "")
}



    class CommentItem( val comment: AddComment): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val adapter = GroupAdapter<ViewHolder>()
        Picasso.get().load(comment.image).into(viewHolder.itemView.imageView_comment)
        viewHolder.itemView.textView_commentText.text = comment.tresc
        viewHolder.itemView.textView_commentLogin.text = comment.currentUser





    }

    override fun getLayout(): Int {
        return R.layout.single_comment_row
    }
}



