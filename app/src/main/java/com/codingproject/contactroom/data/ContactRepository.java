package com.codingproject.contactroom.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.codingproject.contactroom.model.Contact;
import com.codingproject.contactroom.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {

    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {

        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();

        allContacts = contactDao.getAllContacts();

    }
    public LiveData<List<Contact>> getAllData() { return allContacts; }
    public void insert(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.insert(contact);
        });
    }

    public LiveData<Contact> getOneContact(int id) {
        return contactDao.getOneContact(id);
    }
    public void updateContact(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.updateContact(contact);
        });
    }
    public void deleteOneContact(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.deleteOneContact(contact);
        });
    }
}
