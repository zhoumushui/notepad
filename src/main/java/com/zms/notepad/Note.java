package com.zms.notepad;

/**
 * Created by AlexZhou on 2015/2/3.
 * 11:10
 */
public class Note {


    int	_id;			//便签ID
    String	_title;		//便签标题
    String	_content;	//便签内容
    long	_created;	//便签创建时间
    long	_modified;	//便签修改时间

    public Note() {
        _created = System.currentTimeMillis();
        _modified = _created;
    }

    public Note(String title, String content) {
        _title = title;
        _content = content;
        _created = System.currentTimeMillis();
        _modified = _created;
    }
    public Note(String title, String content, long created) {
        _title = title;
        _content = content;
        _created = System.currentTimeMillis();
        //_modified = _created;
    }

    public Note(int id, String title, String content) {
        _id = id;
        _title = title;
        _content = content;
        _created = System.currentTimeMillis();
        _modified = _created;
    }

    public Note(int id, String title, String content, long created, long modified) {
        _id = id;
        _title = title;
        _content = content;
        _created = created;
        _modified = modified;
    }

    public int getId() {
        return _id; //获取便签ID
    }

    public String getTitle() {
        return _title;	//获取便签标题
    }

    public void setTitle(String title) {
        _title = title;	//设置便签标题
    }

    public String getContent() {
        return _content;	//获取便签内容
    }

    public void setContent(String content) {
        _content = content;	//设置便签内容
    }

    public long getCreated() {
        return _created;	//获取便签创建时间
    }

    public void setCreated(long created) {
        _created = created;
    }

    public long getModified() {
        return _modified;
    }

    public void setModified(long modified) {
        _modified = modified;
    }
}
