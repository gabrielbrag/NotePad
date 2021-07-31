package br.com.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private final static String BANCO_DE_DADOS = "notepad";
    private static int VERSAO = 1;

    public DataBaseHelper(Context context) {
        super(context, BANCO_DE_DADOS, null, VERSAO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY, texto TEXT);");
        }catch(Exception e){
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("=================== UPDATE DATABASE");
    }
}
