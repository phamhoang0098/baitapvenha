package com.tuanla.myhttpurlconnectionandasynctaskapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tuanla.myhttpurlconnectionandasynctaskapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class ListViewDisplayData extends AppCompatActivity {
    private List<User> users = new ArrayList<User>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_display_data);

        ListView listView = (ListView) findViewById(R.id.list_item);

        ArrayAdapter<User> userArrayAdapter = new ArrayAdapter<User>(ListViewDisplayData.this, android.R.layout.simple_list_item_1, this.users);

        listView.setAdapter(userArrayAdapter);
    }
}
