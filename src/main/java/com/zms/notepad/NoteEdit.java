package com.zms.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AlexZhou on 2015/2/3.
 * 11:11
 */
public class NoteEdit extends Activity {
    private EditText etTitle;        //便签标题
    private EditText etContent;    //便签内容
    private Button btnCancel;
    private Button btnSave;
    private Button btnRemove;
    private int _noteId;        //便签ID
    private NoteDbHelper _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnRemove = (Button) findViewById(R.id.btnRemove);

        btnCancel.setOnClickListener(new OnClickListenerImp());
        btnSave.setOnClickListener(new OnClickListenerImp());
        btnRemove.setOnClickListener(new OnClickListenerImp());
        _db = new NoteDbHelper(this);
        Intent intent = getIntent();
        _noteId = intent.getIntExtra(NoteList.EXTRA_NOTE_ID, -1);

        //编辑便签
        if (_noteId > 0) {
            Note note = _db.getNote(_noteId);
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContent());
            //设置显示创建时间格式
            TextView noteTime = (TextView) findViewById(R.id.textView4);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            java.util.Date dt = new Date(note.getCreated());
            String sDateTime = sdf.format(dt);
            noteTime.setText(sDateTime);
        }
    }

    private class OnClickListenerImp implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnCancel) {
                Toast.makeText(NoteEdit.this, "天启提示：放弃修改", Toast.LENGTH_SHORT).show();
                finish();
            } else if (v == btnSave) {
                ToDatabase();    //插入数据库
                Toast.makeText(NoteEdit.this, "天启提示：便签保存成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (v == btnRemove) {
                Dialog dialog = new AlertDialog.Builder(NoteEdit.this)
                        .setTitle("删除对话框")
                        .setIcon(R.drawable.ic_launcher)
                        .setMessage("确认删除该便签吗？")
                                //相当于点击确认按钮
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                RealDelete();
                            }
                        })
                                //相当于点击取消按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .create();
                dialog.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_exit) {
            //退出应用，Kill进程
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void RealDelete() {
        //删除便签函数
        _db.deleteNote(_noteId);
        Toast.makeText(this, "天启提示：便签删除成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    public final int ToDatabase() {
        //插入到数据库函数
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        int newNodeId = -1;
        if (_noteId > 0) {
            Note note = _db.getNote(_noteId);
            note.setTitle(title);
            note.setContent(content);
            note.setModified(Long.valueOf(System.currentTimeMillis()));
            _db.updateNote(note);
        } else {
            Note newNote = new Note(title, content);
            newNodeId = _db.addNote(newNote);
        }
        return newNodeId;
    }
}
