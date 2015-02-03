package com.zms.notepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by AlexZhou on 2015/2/3.
 * 11:14
 */
public class NoteNew extends Activity {
    private EditText etTitle;        //便签标题
    private EditText etContent;    //便签内容
    private Button btnCancel;
    private Button btnSave;
    private int _noteId;        //便签ID
    private NoteDbHelper _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_new);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(new OnClickListenerImp());
        btnSave.setOnClickListener(new OnClickListenerImp());
        _db = new NoteDbHelper(this);
        Intent intent = getIntent();
        _noteId = intent.getIntExtra(NoteList.EXTRA_NOTE_ID, -1);
        if (_noteId > 0) {
            Note note = _db.getNote(_noteId);
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContent());
        }
    }

    private class OnClickListenerImp implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (v == btnCancel) {
                Toast.makeText(NoteNew.this, "天启提示：放弃新建便签", Toast.LENGTH_SHORT).show();
                finish();
            } else if (v == btnSave) {
                String titleVoid = etTitle.getText().toString();
                String contentVoid = etContent.getText().toString();
                if (titleVoid.equals("") || contentVoid.equals("")) {
                    Toast.makeText(NoteNew.this, "天启提示：标题或内容为空", Toast.LENGTH_SHORT).show();
                } else {
                    ToDatabase();    //插入数据库
                    Toast.makeText(NoteNew.this, "天启提示：便签保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public final int ToDatabase() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        int newNoteId = -1;
        if (_noteId > 0) {
            Note note = _db.getNote(_noteId);
            note.setTitle(title);
            note.setContent(content);
            note.setModified(Long.valueOf(System.currentTimeMillis()));
            _db.updateNote(note);
        } else {
            Note newNote = new Note(title, content);
            newNoteId = _db.addNote(newNote);
        }
        return newNoteId;
    }
}
