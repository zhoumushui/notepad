package com.zms.notepad;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class Main extends ActionBarActivity {

    private ImageView ivLogo;
    private Button btnView;
    private Button btnNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        btnView = (Button) findViewById(R.id.btnView);
        btnNew = (Button) findViewById(R.id.btnNew);

        btnView.setOnClickListener(new OnClickListenerImp());
        btnNew.setOnClickListener(new OnClickListenerImp());
        ivLogo.setAlpha(50);     //Alpha 0-255,设置主页Logo的透明度
    }

    private class OnClickListenerImp implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnView) {
                Intent intent = new Intent(Main.this, NoteList.class);
                startActivity(intent);
            } else if (v == btnNew) {
                Intent intent = new Intent(Main.this, NoteNew.class);
                startActivity(intent);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_about) {
            //点击菜单兰“关于”按钮后触发
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_exit) {
            //点击菜单兰“退出”按钮后触发
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
