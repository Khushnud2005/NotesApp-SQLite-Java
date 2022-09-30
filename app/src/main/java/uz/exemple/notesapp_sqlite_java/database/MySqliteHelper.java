package uz.exemple.notesapp_sqlite_java.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import uz.exemple.notesapp_sqlite_java.model.Notes;

public class MySqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "note.db";
    private static final int VERSION = 1;

    public MySqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1="create table "+TableSchema.note.TABLE_NAME+"("+TableSchema.note.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TableSchema.note.DESCRIPTION+" TEXT, "+TableSchema.note.DATE+" TEXT );";
        db.execSQL(table1);
    }

    public boolean saveNote(Notes model){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(TableSchema.note.TITLE,model.getTitle());
        cv.put(TableSchema.note.DESCRIPTION,model.getNote());
        cv.put(TableSchema.note.DATE,model.getDate());
        long id = database.insert(TableSchema.note.TABLE_NAME,null,cv);
        if (id == -1){
            return false;
        }
        return true;
    }
    public boolean updateNote(Notes model){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(TableSchema.note.TITLE,model.getTitle());
        cv.put(TableSchema.note.DESCRIPTION,model.getNote());
        cv.put(TableSchema.note.DATE,model.getDate());

        database.update(TableSchema.note.TABLE_NAME, cv, "id = ?", new String[]{""+model.getId()});
        //long id = database.insert(TableSchema.note.TABLE_NAME,null,cv);
        return model.getId() != -1;
    }
    @SuppressLint("Range")
    public List<Notes> getAllNotes(){
        SQLiteDatabase database= this.getReadableDatabase();
        String[] cols={TableSchema.note.ID,TableSchema.note.DESCRIPTION,TableSchema.note.DATE};
        Cursor cursor=database.query(TableSchema.note.TABLE_NAME,cols,null,null,null,null,TableSchema.note.ID+" DESC");
        ArrayList<Notes> list=new ArrayList<>();
        while (cursor.moveToNext()){
            Notes model = new Notes();
            model.setId(cursor.getInt(cursor.getColumnIndex(TableSchema.note.ID)));
            //model.setTitle(cursor.getString(cursor.getColumnIndex(TableSchema.note.TITLE)));
            model.setNote(cursor.getString(cursor.getColumnIndex(TableSchema.note.DESCRIPTION)));
            model.setDate(cursor.getString(cursor.getColumnIndex(TableSchema.note.DATE)));
            list.add(model);
        }
        cursor.close();
        database.close();
        return list;
    }
    public boolean deleteNote(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TableSchema.note.TABLE_NAME, TableSchema.note.ID + "=" + id, null) > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+TableSchema.note.TABLE_NAME);
        }
    }
}
