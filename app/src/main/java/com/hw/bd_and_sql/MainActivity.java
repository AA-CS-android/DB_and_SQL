package com.hw.bd_and_sql;

import static com.hw.bd_and_sql.Users.TABLE_USERS;
import static com.hw.bd_and_sql.User.*;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button addPerson;
    Button removePerson;
    Button editPerson;
    EditText etId, etName, etAge, etPass;
    ListView listViewUsers;

    private SQLiteDatabase db;
    private HelperDB hlp;
    private ArrayList<User> users;
    private UserAdapter adp;
    private User tmpUser;
    private int key_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();
        addPerson.setOnClickListener(v -> addUser());
    }

    void init()
    {
        addPerson = findViewById(R.id.btnAddUser);
        removePerson = findViewById(R.id.btnRemove);
        editPerson = findViewById(R.id.btnEdit);
        etId=findViewById(R.id.etId);
        etName=findViewById(R.id.etName);
        etAge=findViewById(R.id.etAge);
        etPass=findViewById(R.id.etPass);
        listViewUsers = findViewById(R.id.usersList);


        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        users = new ArrayList<>();
        key_id = 0;
        db = hlp.getReadableDatabase();

        Cursor crsr = db.query(TABLE_USERS,null,null,null,null,null,null);
        int colKEY_ID = crsr.getColumnIndex(Users.KEY_ID);
        int colName = crsr.getColumnIndex(Users.NAME);
        int colAge = crsr.getColumnIndex(Users.AGE);
        int colPass = crsr.getColumnIndex(Users.PASSWORD);
        crsr.moveToFirst();
        while (!crsr.isAfterLast())
        {
            tmpUser = new User();
            tmpUser.setKey_id(crsr.getInt(colKEY_ID));
            tmpUser.setName(crsr.getString(colName));
            tmpUser.setAge(crsr.getInt(colAge));
            tmpUser.setPassword(crsr.getString(colPass));
            users.add(tmpUser);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp = new UserAdapter(this, users);
        listViewUsers.setAdapter(adp);
        ((UserAdapter) listViewUsers.getAdapter()).notifyDataSetChanged();
    }

    void addUser()
    {
        String name, pass, strAge;
        int age;
        name = etName.getText().toString();
        pass = etPass.getText().toString();
        strAge = etAge.getText().toString();
        if(name.isEmpty() || pass.isEmpty() || strAge.isEmpty()) {
            Toast.makeText(this, "One of the fields is missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        age = Integer.parseInt(strAge);
        tmpUser = new User(name,pass,age);

        ContentValues cv = new ContentValues();
        cv.put(Users.NAME, tmpUser.getName());
        cv.put(Users.PASSWORD, tmpUser.getPassword());
        cv.put(Users.AGE, tmpUser.getAge());

        db = hlp.getWritableDatabase();
        key_id = (int) db.insert(TABLE_USERS, null, cv);
        db.close();

        etPass.setText("");
        etAge.setText("");
        etName.setText("");

        if(key_id != -1)
        {
            Toast.makeText(this, "Data pushed to Users table", Toast.LENGTH_LONG).show();
            tmpUser.setKey_id(key_id);
            users.add(tmpUser);

            ((UserAdapter) listViewUsers.getAdapter()).notifyDataSetChanged();
        }
    }
}