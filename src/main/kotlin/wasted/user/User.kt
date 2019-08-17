package wasted.user

import java.util.*

data class User(val id: Int,
                val currencies: ArrayList<Currency>,
                val whatsNew: Boolean)
