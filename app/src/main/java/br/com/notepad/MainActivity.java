package br.com.notepad;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Teste com Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        helper = new DataBaseHelper(this);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btnSave);
        EditText editText = findViewById(R.id.editText);
        ContentValues values = new ContentValues();

        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT texto FROM notes", null);
        String note = "";
        if(cursor != null) {
            cursor.moveToFirst();
            for(int i = 0; i < cursor.getCount(); i++){
                note = note.isEmpty() ? (cursor.getString(0)) : note + "\n" + (cursor.getString(0));
                cursor.moveToNext();
            }
        }
        editText.setText(note);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText() != null && !editText.getText().equals("")) {
                    values.put("texto", editText.getText().toString());
                    String count = "SELECT count(*) FROM notes";
                    Cursor mcursor = db.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);
                    long resultado;
                    db = helper.getWritableDatabase();
                    if(icount>0) {
                        resultado = db.update("notes", values, "_id = ?", new String[]{"1"});
                    }else{
                        resultado = db.insert("notes", null, values);
                    }

                    if (resultado != -1)
                        Toast.makeText(MainActivity.this, "Registro Salvo", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Falha ao salvar", Toast.LENGTH_LONG).show();

                    DatabaseReference myRef = database.getReference(new Date().toString());
                    myRef.child("texto").setValue("Texto");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            System.out.println(snapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}