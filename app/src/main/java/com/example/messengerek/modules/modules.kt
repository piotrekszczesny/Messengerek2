package com.example.messengerek.modules

import android.os.Parcelable


import com.example.messengerek.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.single_news_row.view.*
import kotlinx.android.synthetic.main.singlesupernews.view.*

@Parcelize
class DodajSuperNewsa (val tresc: String, val tytul: String, val profileImage: String): Parcelable {

    constructor() : this("", "", "")
}

@Parcelize
class DodajMema (val tytul: String, val profileImage: String): Parcelable {

    constructor() : this("", "")
}
@Parcelize
class DodajNewsa (val tresc: String, val tytul: String, val profileImage: String, val formatedDate: String):Parcelable {

    constructor() : this("", "", "", "")
}
@Parcelize
class DodajNewsaPilka (val tresc: String, val tytul: String, val profileImage: String, val formatedDate: String): Parcelable {

    constructor() : this("", "", "", "")
}
@Parcelize
class DodajRelacje (val tresc: String, val tytul: String, val profileImage: String, val dataR: String, val skladgosp: String, val skladgosci: String, val gosp: String, val goscie: String): Parcelable {

    constructor() : this("", "", "", "","","", "", "")


}

@Parcelize
class AddHighLits (val tytul: String, val videoImageURL: String): Parcelable {

    constructor() : this("", "")
}