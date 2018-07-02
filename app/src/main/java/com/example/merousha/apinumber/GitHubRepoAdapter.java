package com.example.merousha.apinumber;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.merousha.apinumber.api.GitHubRepo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by merousha on 2018/06/27.
 */

public class GitHubRepoAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> getRealmData;


    public GitHubRepoAdapter(Context context, ArrayList<String> getRealmData) {
        super(context, R.layout.list_item_pagination, getRealmData );

        this.context = context;
        this.getRealmData = getRealmData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_pagination, parent, false);
        }


        TextView textView = (TextView) row.findViewById(R.id.list_item_pagination_text);

//        GitHubRepo item = values.get(position);
//        String message = item.getMyName();
        textView.setText(getRealmData.get(position));

        return row;
    }
}
