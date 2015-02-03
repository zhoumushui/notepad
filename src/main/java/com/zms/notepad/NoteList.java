package com.zms.notepad;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by AlexZhou on 2015/2/3.
 * 11:08
 */
public class NoteList extends ListActivity {
    public final static String EXTRA_NOTE_ID = "com.tchip.note.NOTEID";

    private NoteDbHelper _db = null;
    private LinearLayout llNoContent;
    private Button btnNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);

        getListView().setOnCreateContextMenuListener(this);
        _db = new NoteDbHelper(this);
        llNoContent = (LinearLayout) findViewById(R.id.llNoContent);
        btnNew = (Button) findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new OnClickListenerImp());

        String[] dataColumns = new String[]{"title", "content"};
        int[] viewIds = new int[]{R.id.title, R.id.summary};

        Cursor cursor = _db.getAllNotesCursor();
        if (cursor.getCount() < 1) {
            llNoContent.setVisibility(View.VISIBLE);
        } else {
            llNoContent.setVisibility(View.GONE);
            // Creates the backing adapter for the ListView.
            SimpleCursorAdapter adapter
                    = new SimpleCursorAdapter(
                    this,                             // The Context for the ListView
                    R.layout.note_list_item,           // Points to the XML for a list item
                    cursor,                           // The cursor to get items from
                    dataColumns,
                    viewIds
            );
            setListAdapter(adapter);
        }
    }

    private class OnClickListenerImp implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnNew) {
                Intent intent = new Intent(NoteList.this, NoteNew.class);
                startActivity(intent);
            }
        }
    }

    private void reloadNote() {
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
        if (adapter != null) {
            Cursor cursor = _db.getAllNotesCursor();
            adapter.swapCursor(cursor);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadNote();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //顶部菜单栏菜单
        switch (item.getItemId()) {
            case R.id.menu_add:        //新建
                Intent intent = new Intent(this, NoteNew.class);
                startActivity(intent);
                return true;
            case R.id.menu_about:    //关于
                Intent intent2 = new Intent(this, About.class);
                startActivity(intent2);
                return true;
            case R.id.menu_exit:    //退出
                Intent intent3 = new Intent(Intent.ACTION_MAIN);
                intent3.addCategory(Intent.CATEGORY_HOME);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, NoteEdit.class);
        intent.putExtra(EXTRA_NOTE_ID, (int) id);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        // Inflate menu from XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lay_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Context上下文菜单，按住便签2s后显示
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e("TChipNote", "Bad MenuInfo", e);
            return false;
        }
        switch (item.getItemId()) {
            case R.id.menu_add:        // 新建按钮
                Intent intent11 = new Intent(this, NoteNew.class);
                startActivity(intent11);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
