package com.hw.bd_and_sql;

import static com.hw.bd_and_sql.Users.KEY_ID;
import static com.hw.bd_and_sql.Users.TABLE_USERS;
import static com.hw.bd_and_sql.User.*;

import android.app.Activity;
import android.app.usage.UsageEventsQuery;
import android.content.ContentValues;
import android.content.Context;
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
    Button btnAddPerson;
    Button btnRemovePerson;
    Button btnEditPerson;
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
        btnAddPerson.setOnClickListener(v -> addUser());
        btnRemovePerson.setOnClickListener(v -> removeUser());
        btnEditPerson.setOnClickListener(v -> editUser());
    }


    void init()
    {
        btnAddPerson = findViewById(R.id.btnAddUser);
        btnRemovePerson = findViewById(R.id.btnRemove);
        btnEditPerson= findViewById(R.id.btnEdit);
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
    void removeUser()
    {
        db = hlp.getWritableDatabase();
        String str = etId.getText().toString();
        if(str.isEmpty())
        {
            return;
        }
        int rows = db.delete(TABLE_USERS, KEY_ID+"=?", new String[] {str});
        if(rows == 0)
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        else {
            users.remove(Integer.parseInt(str) - 1);
        }
        db.close();
        updateDB();

    }
    void editUser()
    {
        String str = etId.getText().toString();
        if(str.isEmpty())
        {
            return;
        }

        int
                userId = Integer.parseInt(str),
                newAge;
        String
                newName = etName.getText().toString(),
                newPass = etAge.getText().toString(),
                newAgeStr = etAge.getText().toString();

        if (newName.isEmpty() || newPass.isEmpty() || newAgeStr.isEmpty()) {
            Toast.makeText(this, "One of the fields is missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        newAge = Integer.parseInt(newAgeStr);

        ContentValues cv = new ContentValues();
        cv.put(Users.NAME, newName);
        cv.put(Users.AGE, newAge);
        cv.put(Users.PASSWORD, newPass);

        db = hlp.getWritableDatabase();
        int rowsUpdated = db.update(TABLE_USERS, cv, Users.KEY_ID+"=?", new String[]{String.valueOf(userId)});
        db.close();
        if (rowsUpdated > 0)
        {
            Toast.makeText(this, "User updated successfully", Toast.LENGTH_LONG).show();
            updateDB();
        } else {
            Toast.makeText(this, "No user found with that ID", Toast.LENGTH_SHORT).show();
        }
    }

    void updateDB()
    {
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
        ((UserAdapter) listViewUsers.getAdapter()).notifyDataSetChanged();
    }
}
