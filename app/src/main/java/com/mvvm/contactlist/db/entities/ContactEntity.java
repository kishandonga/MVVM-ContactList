package com.mvvm.contactlist.db.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "Tbl_Contact", foreignKeys = @ForeignKey(entity = CategoryEntity.class,
        parentColumns = "category_Id",
        childColumns = "category_Id",
        onDelete = RESTRICT), indices = {@Index(value = {"mobile_num"}, unique = true)})
public class ContactEntity implements Comparable<ContactEntity> {

    @PrimaryKey(autoGenerate = true)
    private int contactId;

    @ColumnInfo(name = "category_Id", index = true)
    private int categoryId;

    @ColumnInfo(name = "profile_pic")
    private String profilePic;

    @ColumnInfo(name = "first_name")
    private String firstName = "";

    @ColumnInfo(name = "last_name")
    private String lastName = "";

    @ColumnInfo(name = "mobile_num")
    private String mobileNum = "";

    @ColumnInfo(name = "contact_email")
    private String email = "";

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(ContactEntity o) {
        if (contactId == o.getContactId() &&
                firstName.equals(o.getFirstName()) &&
                lastName.equals(o.getLastName()) &&
                mobileNum.equals(o.getMobileNum()) &&
                email.equals(o.getEmail())) {
            return 1;
        }
        return 0;
    }
}
