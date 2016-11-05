package edu.uml.android.cityecho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class IssueAdapter extends ArrayAdapter<Issue> {

    public IssueAdapter(Context context, ArrayList<Issue> issues) {
        super(context, 0, issues);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Make sure the view hasn't been used yet, if not, inflate it
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.issue_item, parent, false);
        }

        // Get the current Issue object
        Issue currentIssue = getItem(position);

        // Set the type of issue
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.type_text_view);
        nameTextView.setText(currentIssue.getType());

        // Set the address of the issue
        TextView artistTextView = (TextView) listItemView.findViewById(R.id.address_text_view);
        artistTextView.setText((currentIssue.getFullAddress()));

        // Set the thumbnail of the article
        ImageView mapImageView = (ImageView) listItemView.findViewById(R.id.map);
        mapImageView.setImageBitmap(currentIssue.getMapImage());

        return listItemView;
    }
}

