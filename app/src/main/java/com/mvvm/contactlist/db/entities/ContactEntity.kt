package com.mvvm.contactlist.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.RESTRICT
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Tbl_Contact",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["category_Id"],
        childColumns = ["category_Id"],
        onDelete = RESTRICT
    )],
    indices = [Index(value = ["mobile_num"], unique = true)]
)
class ContactEntity : Comparable<ContactEntity> {

    @PrimaryKey(autoGenerate = true)
    var contactId = 0

    @ColumnInfo(name = "category_Id", index = true)
    var categoryId = 0

    @ColumnInfo(name = "profile_pic")
    var profilePic: String? = null

    @ColumnInfo(name = "first_name")
    var firstName = ""

    @ColumnInfo(name = "last_name")
    var lastName = ""

    @ColumnInfo(name = "mobile_num")
    var mobileNum = ""

    @ColumnInfo(name = "contact_email")
    var email = ""
    override fun compareTo(other: ContactEntity): Int {
        return if (contactId == other.contactId && firstName == other.firstName && lastName == other.lastName && mobileNum == other.mobileNum && email == other.email) {
            1
        } else 0
    }
}