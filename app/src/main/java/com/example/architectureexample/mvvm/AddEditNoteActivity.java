package com.example.architectureexample.mvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

import com.example.architectureexample.R;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID
            ="com.example.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE
            ="com.example.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DISCRIPTION
            ="com.example.architectureexample.EXTRA_DISCRIPTION";
    public static final String EXTRA_PRIORITY
            ="com.example.architectureexample.EXTRA_PRIORITY";
    private EditText editTextTitle;
    private EditText editTextDiscription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDiscription = findViewById(R.id.edit_text_discription);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent=getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Customer");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDiscription.setText(intent.getStringExtra(EXTRA_DISCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }
        else{
            setTitle("Add Customer");
        }
    }

    private void saveNote(){
        String title=editTextTitle.getText().toString();
        String discription=editTextDiscription.getText().toString();
        int priority=numberPickerPriority.getValue();

        if(title.trim().isEmpty()||discription.trim().isEmpty()){
            Toast.makeText(this, "Please Insert Name and Address", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data=new Intent();
        data.putExtra(EXTRA_TITLE,title);
        data.putExtra(EXTRA_DISCRIPTION,discription);
        data.putExtra(EXTRA_PRIORITY,priority);

        int id=getIntent().getIntExtra(EXTRA_ID,-1);
            if (id == -1) {
                data.putExtra(EXTRA_ID,id);
            }


        setResult(RESULT_OK,data);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}