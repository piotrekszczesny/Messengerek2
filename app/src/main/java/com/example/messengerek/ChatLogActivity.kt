package com.example.messengerek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_from_row.view.textView_ostatnie_wiadomosci
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object {

        val TAG = "Wiadomości"
    }

    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat_log)



        recyclerView_chatLog.adapter = adapter


        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username



        listemForMessages()

        send_button_chat_log.setOnClickListener{

            Log.d(TAG, "Próba wysłania wiadomości")
            performSendMessage()

        }
    }

    private fun listemForMessages(){

        val fromID = FirebaseAuth.getInstance().uid
        val toID = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID")

        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
               val chatMessage =  p0.getValue(ChatMessage::class.java)
                if (chatMessage != null) {

                    Log.d (TAG, chatMessage.text)
                    if (chatMessage.fromID == FirebaseAuth.getInstance().uid) {
                        val currentUser = OstatnieWiadomosci.currentUser ?: return  // elvis operator
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))

                    } else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }

            }
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })


    }

    class ChatMessage(val id: String, val text:String, val fromID: String, val toID: String, val trimestamp: Long) {

        constructor(): this("","","","",-1)

    }

    private fun performSendMessage(){
        val text = editText_chatLog.text.toString()




        val fromID = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toID = user.uid
        if (fromID == null) return
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID").push()
        val chatMessage = ChatMessage(reference.key!!, text, fromID, toID, System.currentTimeMillis() / 1000)

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Zapisano wiadomość: ${reference.key}")
            }


    }

}

class ChatFromItem(val text: String, val user: User): Item<ViewHolder>()
{
    override fun bind(viewHolder: ViewHolder, position: Int) {


        viewHolder.itemView.textView_ostatnie_wiadomosci.text = text
        val uri = user.profileImage
        val targetImageView = viewHolder.itemView.imageViewfrom
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
            return R.layout.chat_from_row
    }
}
class ChatToItem(val text: String, val user: User): Item<ViewHolder>()
{
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = text
        val uri = user.profileImage
        val targetImageView = viewHolder.itemView.imageView2
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}

