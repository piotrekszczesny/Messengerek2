package com.example.messengerek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.example.messengerek.OstatnieWiadomosci.Companion.currentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_szczegoly_relacji.*
import kotlinx.android.synthetic.main.activity_szczegoly_relacji.view.*
import kotlinx.android.synthetic.main.single_comment_row.view.*
import kotlinx.android.synthetic.main.single_relation.view.*
import java.text.SimpleDateFormat
import java.util.*

class SzczegolyRelacjiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_szczegoly_relacji)


        fetchSingleRelation()
        fetchRelationLive()
        fetchcomments()
        btn_dodajKomentarz.setOnClickListener{

            dodajKomentarz()
            recreate()

        }

    }


    private fun fetchSingleRelation(){



        val relationImage = intent.getStringExtra(RelacjeActivity.RELACJA_IMG)
        val relationTitle = intent.getStringExtra(RelacjeActivity.RELACJA_TITLE)
        val relationText = intent.getStringExtra(RelacjeActivity.RELACJA_TEXT)
        val dataR = intent.getStringExtra(RelacjeActivity.RELACJA_DATA)
        val skladGosp = intent.getStringExtra(RelacjeActivity.RELACJA_GOSP)
        val skladGosci = intent.getStringExtra(RelacjeActivity.RELACJA_GOSCIE)
        val gosp = intent.getStringExtra(RelacjeActivity.RELACJA_DRUZYNA_GOSP)
        val goscie = intent.getStringExtra(RelacjeActivity.RELACJA_DRUZYNA_GOSCI)

        Picasso.get().load(relationImage).into(imageView_image_relation)
        textView__tresc_relation.text = relationText
        textView_tytul_relation.text = relationTitle
        textView_data_relation.text = dataR
        textView_sklad_gospodarze.text = skladGosp
        textView_sklad_goscie.text =skladGosci
        textView_relation_gospodarz.text = gosp
        textView_relation_goscie.text = goscie


    }




    private fun fetchRelationLive(){

        val relacjaTitle = intent.getStringExtra(RelacjeActivity.RELACJA_TITLE)
        val ref = FirebaseDatabase.getInstance().getReference("/relacje-live/$relacjaTitle/")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("Relacje", it.toString())
                    val relation = it.getValue(AddRelationLive::class.java)
                    if (relation != null) {
                        adapter.add(RelacjaLiveItem(relation))
                    }


                }


                recyclerView_comment_relation.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }

    private fun dodajKomentarz() {

        val tresc = dodajKomentarz_editText.text.toString()
        val relacjaTitle = intent.getStringExtra(RelacjeActivity.RELACJA_TITLE)
        val currentUser = OstatnieWiadomosci.currentUser ?:return
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val formatedDate = formatter.format(date)
        val image = OstatnieWiadomosci.currentUser?.profileImage.toString()
        val uid = OstatnieWiadomosci.currentUser?.uid.toString()



        if (tresc == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/comments/$relacjaTitle/$formatedDate")
        val dodajKomentarze= AddComment(currentUser.username, tresc, relacjaTitle,image, formatedDate)



        reference.setValue(dodajKomentarze)
            .addOnSuccessListener {
                Log.d(NewsActivity.TAG, "Zapisano komentarz: ${reference.key}")
            }


    }

    private fun fetchcomments(){
        val uid = FirebaseAuth.getInstance().uid
        val relacjaTitle = intent.getStringExtra(RelacjeActivity.RELACJA_TITLE)
        val ref = FirebaseDatabase.getInstance().getReference("/comments/$relacjaTitle/")
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


                recyclerView_comment.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }

}

@Parcelize
class AddRelationLive (val tresc: String, val rdate:String):
    Parcelable {

    constructor() : this("", "")
}



class RelacjaLiveItem(val relation: AddRelationLive): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {


        viewHolder.itemView.relation_liveTextx.text = relation.tresc
        viewHolder.itemView.relation_liveData.text = relation.rdate




    }

    override fun getLayout(): Int {
        return R.layout.single_relation
    }
}
