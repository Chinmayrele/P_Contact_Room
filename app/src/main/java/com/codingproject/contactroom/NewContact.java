package com.codingproject.contactroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codingproject.contactroom.model.Contact;
import com.codingproject.contactroom.model.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NewContact extends AppCompatActivity {
    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION_REPLY = "occupation_reply";
    private EditText enterName;
    private EditText enterOccupation;
    private Button saveButton;
    private ContactViewModel contactViewModel;
    private int contactId = 0;
    private Boolean isEdit = false;
    private ImageButton updateButton;
    private Button deleteButton;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        enterName = findViewById(R.id.enter_name);
        enterOccupation = findViewById(R.id.enter_occupation);
        saveButton = findViewById(R.id.save_button);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(
                NewContact.this.getApplication())
                .create(ContactViewModel.class);

        if(getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            contactViewModel.getOneContact(contactId).observe(this, new Observer<Contact>() {
                @Override
                public void onChanged(Contact contact) {
                    if(contact != null) {
                        enterName.setText(contact.getName());
                        enterOccupation.setText(contact.getOccupation());
                    }
                }
            });
            isEdit = true;
        }


        saveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if(!TextUtils.isEmpty(enterName.getText())
                    && !TextUtils.isEmpty(enterOccupation.getText())) {

                String name = enterName.getText().toString();
                String occupation = enterOccupation.getText().toString();

                replyIntent.putExtra(NAME_REPLY, name);
                replyIntent.putExtra(OCCUPATION_REPLY, occupation);

                setResult(RESULT_OK, replyIntent);

//                Contact contact = new Contact(enterName.getText().toString(),
//                        enterOccupation.getText().toString());
//                ContactViewModel.insert(contact);
            } else {
                Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED, replyIntent);
            }
            finish();
        });

        // UPDATE BUTTON
        updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(view -> {
            int id = contactId;
            String name = enterName.getText().toString().trim();
            String occupation = enterOccupation.getText().toString().trim();

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)) {
                Snackbar.make(enterName, R.string.empty, Snackbar.LENGTH_SHORT).show();
            } else {
                Contact contact = new Contact();
                contact.setId(id);
                contact.setName(name);
                contact.setOccupation(occupation);

                ContactViewModel.updateContact(contact);
                finish();
            }
        });

        // DELETE BUTTON
        deleteButton = findViewById(R.id.delete_button);




        // HIDING THE BUTTONS
        if(isEdit) {
//            saveButton.setVisibility(View.GONE);
            saveButton.setBackgroundColor(R.color.grey);
            saveButton.setTextColor(R.color.grey);
            saveButton.setEnabled(false);
        } else {
            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }


//        Bundle data = getIntent().getExtras();
//        if(data != null) {
//            int id = data.getInt(MainActivity.CONTACT_ID);
//            contactViewModel.getOneContact(id).observe(this, new Observer<Contact>() {
//                @Override
//                public void onChanged(Contact contact) {
//                    enterName.setText(contact.getName());
//                    enterOccupation.setText(contact.getOccupation());
//                }
//            });
//        }
    }
}