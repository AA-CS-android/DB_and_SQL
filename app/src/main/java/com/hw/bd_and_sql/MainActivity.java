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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    private ArrayList<String> namesList;
    private ArrayList<User> users;
    private ArrayAdapter adp;
    private User tmpUser;
    private int key_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();
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
        namesList = new ArrayList<>();
        namesList.add("Choose a user: ");
        key_id =0;
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
            namesList.add(tmpUser.getName());
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, namesList);
        listViewUsers.setAdapter(adp);
    }


}