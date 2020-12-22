package com.example.architectureexample.mvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.architectureexample.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    public static final int ADD_NOTE_REQUEST=1;
    public static final int EDIT_NOTE_REQUEST=2;
    RecyclerView recyclerView;
    NoteAdapter adapter;
    FloatingActionButton buttonAddNote;
    public boolean mselected =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAddNote = findViewById(R.id.button_add_note);


        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        //Final NoteAdapter
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Customer Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Note note) {
            Intent intent=new Intent(MainActivity.this, AddEditNoteActivity.class);
            intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
            intent.putExtra(AddEditNoteActivity.EXTRA_DISCRIPTION,note.getDiscription());
            intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getDiscription());
            startActivityForResult(intent,EDIT_NOTE_REQUEST);
        }
    });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode==RESULT_OK){
            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String discription=data.getStringExtra(AddEditNoteActivity.EXTRA_DISCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note=new Note(title,discription,priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Customer Data Save", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_NOTE_REQUEST && resultCode==RESULT_OK){
            int id=data.getIntExtra(AddEditNoteActivity.EXTRA_ID,1);
            if(id == -1){
                Toast.makeText(this, "Customer Data Can't be Updated ", Toast.LENGTH_SHORT).show();
                return;
            }
            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String discription=data.getStringExtra(AddEditNoteActivity.EXTRA_DISCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note=new Note(title,discription,priority);
            note.setId(id);
            noteViewModel.upadate(note);
            Toast.makeText(this, "Customer Data Updated", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Not save", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Customers Deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.getselected:
                if (adapter.getSelected().size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < adapter.getSelected().size(); i++) {
                        stringBuilder.append(adapter.getSelected().get(i).getTitle());
                        stringBuilder.append("\n");
                    }
                    Toast.makeText(this, stringBuilder.toString().trim(), Toast.LENGTH_SHORT).show();
                   // showToast(stringBuilder.toString().trim());
                } else {
                    Toast.makeText(this, "No Selection", Toast.LENGTH_SHORT).show();
                }
            case R.id.selectall:

                adapter.setAllChecked(mselected);
                if(mselected){
                    mselected =false;
                } else{
                    mselected =true;
                }
//            case R.id.sendmessage:
//                Intent intent=new Intent(MainActivity.this,Message.class);
//                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }

        }
    }
