package com.codingproject.contactroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingproject.contactroom.adapter.RecyclerViewAdapter;
import com.codingproject.contactroom.model.Contact;
import com.codingproject.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.onContactClickListener {
    public static final String CONTACT_ID = "contact_Id";
    //    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        // There are no request codes
////                        Intent data = result.getData();
//                        String data = result.getData().getStringExtra("msg_back");
//                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
////                        doSomeOperations();
//                    }
                    if (RESULT_OK == result.getResultCode()) {
                        assert result.getData() != null;
                        String name = result.getData().getStringExtra(NewContact.NAME_REPLY);
                        String occupation = result.getData().getStringExtra(NewContact.OCCUPATION_REPLY);

                        Contact contact = new Contact(name, occupation);

                        ContactViewModel.insert(contact);

                        Log.d("REPLY", "onActivityResult: " + result.getData().getStringExtra(NewContact.NAME_REPLY));
                        Log.d("REPLY", "onActivityResult: " + result.getData().getStringExtra(NewContact.OCCUPATION_REPLY));
                    }
                }
            });

    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(ContactViewModel.class);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        textViewContact = findViewById(R.id.textViewContact);

        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {

                // SET THE ADAPTER
                recyclerViewAdapter = new RecyclerViewAdapter(contacts, MainActivity.this, MainActivity.this);
                recyclerView.setAdapter(recyclerViewAdapter);

//                StringBuilder builder = new StringBuilder();
//                for (Contact contact : contacts) {
//                    builder.append(" - ").append(contact.getName()).append(",  ").append(contact.getOccupation());
//                    Log.d("ALL-CONTACT", "onChanged: " + contact.getName() + ",  " + contact.getOccupation());
//                }
////                textViewContact.setText(builder.toString());
            }
        });

        FloatingActionButton fab = findViewById(R.id.add_contact_floating_btn);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
//            startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);  // DEPRECATED... CHANGE IT

            //
            someActivityResultLauncher.launch(intent);
        });

    }

    @Override
    public void onContactClick(int position) {
        Contact contact = Objects.requireNonNull(contactViewModel.getAllContacts().getValue()).get(position);
        Log.d("Clicked", "onContactClick: " + contact.getId() + ",  " +
                contact.getName() + ",  " + contact.getOccupation());

        Intent intent = new Intent(MainActivity.this, NewContact.class);
        intent.putExtra(CONTACT_ID, contact.getId());

        startActivity(intent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(NEW_CONTACT_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
//            assert data != null;
//            String name = data.getStringExtra(NewContact.NAME_REPLY);
//            String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);
//
//            Contact contact = new Contact(name, occupation);
//
//            ContactViewModel.insert(contact);
//
//            Log.d("REPLY", "onActivityResult: " + data.getStringExtra(NewContact.NAME_REPLY));
//            Log.d("REPLY", "onActivityResult: " + data.getStringExtra(NewContact.OCCUPATION_REPLY));
//        }
//    }
}