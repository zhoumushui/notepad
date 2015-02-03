package com.zms.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexZhou on 2015/2/3.
 * 11:12
 */
public class NoteDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notes_db";

    private static final String NOTE_TABLE_NAME = "note";
    private static final String NOTE_COL_ID = "_id";
    private static final String NOTE_COL_TITLE = "title";
    private static final String NOTE_COL_CONTENT = "content";
    private static final String NOTE_COL_CREATED = "created";
    private static final String NOTE_COL_MODIFIED = "modified";

    private static final String[] NOTE_COL_PROJECTION = new String[] {
            NOTE_COL_ID,
            NOTE_COL_TITLE,
            NOTE_COL_CONTENT,
            NOTE_COL_CREATED,
            NOTE_COL_MODIFIED
    };

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createNoteTableSql = "CREATE TABLE " + NOTE_TABLE_NAME + " ("
                + NOTE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NOTE_COL_TITLE
                + " TEXT," + NOTE_COL_CONTENT + " TEXT," + NOTE_COL_CREATED
                + " INTEGER," + NOTE_COL_MODIFIED + " INTEGER" + ");";
        db.execSQL(createNoteTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // Add new note
    public int addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_COL_TITLE, note.getTitle());
        values.put(NOTE_COL_CONTENT, note.getContent());
        values.put(NOTE_COL_CREATED, note.getCreated());
        values.put(NOTE_COL_MODIFIED, note.getModified());

        // Insert to database
        long rowId = db.insert(NOTE_TABLE_NAME, null, values);

        // Close the database
        db.close();

        return (int) rowId;
    }

    // Get one note
    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NOTE_TABLE_NAME, NOTE_COL_PROJECTION,
                NOTE_COL_ID + "=?", new String[] { String.valueOf(id) }, null,
                null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(cursor.getInt(0), cursor.getString(1),
                cursor.getString(2), cursor.getLong(3), cursor.getLong(4));

        return note;
    }

    // Get all notes
    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<Note>();
        String selectQuery = "SELECT * FROM " + NOTE_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getLong(3),
                        cursor.getLong(4));
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        // return contact list
        return noteList;
    }

    public Cursor getAllNotesCursor() {
        String selectQuery = "SELECT * FROM " + NOTE_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_COL_TITLE, note.getTitle());
        values.put(NOTE_COL_CONTENT, note.getContent());
        values.put(NOTE_COL_CREATED, note.getCreated());
        values.put(NOTE_COL_MODIFIED, note.getModified());

        return db.update(NOTE_TABLE_NAME, values,
                NOTE_COL_ID + "=?", new String[] { String.valueOf(note.getId()) });
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTE_TABLE_NAME, NOTE_COL_ID + "=?",new String[] { String.valueOf(noteId) } );
        db.close();
    }

    public void deleteAllNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTE_TABLE_NAME, null, null);
        db.close();
    }
}
