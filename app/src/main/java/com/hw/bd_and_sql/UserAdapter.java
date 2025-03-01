package com.hw.bd_and_sql;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.list_item_user, users); // Use the item layout and user list
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reuse view if possible
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent, false);
        }

        // Get the current user object
        User user = getItem(position);  // Use getItem() since we're extending ArrayAdapter

        // Populate the row views
        TextView tvId = convertView.findViewById(R.id.userListID);
        TextView tvName = convertView.findViewById(R.id.userListName);
        TextView tvAge = convertView.findViewById(R.id.userListAge);
        TextView tvPass = convertView.findViewById(R.id.userListPass);

        // Set data for each TextView
        tvId.setText(String.valueOf(user.getKey_id()));
        tvName.setText(user.getName());
        tvAge.setText(String.valueOf(user.getAge()));
        tvPass.setText(user.getPassword());

        return convertView;
    }
}
