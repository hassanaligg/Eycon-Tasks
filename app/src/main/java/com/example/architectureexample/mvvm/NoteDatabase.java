package com.example.architectureexample.mvvm;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.architectureexample.service.LocationDao;
import com.example.architectureexample.service.LocationModel;

@Database(entities = {Note.class, LocationModel.class}, version = 4)
public abstract class NoteDatabase extends androidx.room.RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();
    public abstract LocationDao locationDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        public PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Discription 1", 1));
            noteDao.insert(new Note("Title 2", "Discription 2", 2));
            noteDao.insert(new Note("Title 3", "Discription 3", 3));
            return null;
        }
    }
}
