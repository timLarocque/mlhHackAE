package edu.uml.android.cityecho;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tim on 11/5/2016.
 */
public class IssueActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_issue);

        // Set up the spinner
        final Spinner spinner = (Spinner) findViewById(R.id.issue_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.issue_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ImageView spinnerIcon = (ImageView) findViewById(R.id.spinner_icon);
        spinnerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });

        // On click listener for button
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check to see if all entries have been filled.
                EditText a = (EditText) findViewById(R.id.address);
                String address = a.getText().toString();
                EditText s = (EditText) findViewById(R.id.state);
                String state = s.getText().toString();
                EditText c = (EditText) findViewById(R.id.city);
                String city = c.getText().toString();
                EditText e = (EditText) findViewById(R.id.email);
                String email = e.getText().toString();
                if (address.length() != 0 && state.length() != 0 && city.length() != 0 && email.length() != 0) {
                    // Then we want to submit
                    String type = spinner.getSelectedItem().toString();
                    String street_num = address.split("\\s+")[0];
                    String[] temp = address.split("\\s+");
                    String street_name = "";
                    for (int i = 1; i < temp.length; i++)
                        street_name = street_name + temp[i] + " ";
                    JSONObject json = new JSONObject();
                    try {
                        json.put("street_num", street_num);
                        json.put("street_name", street_name);
                        json.put("city", city);
                        json.put("state", state);
                        json.put("type", type);
                        json.put("email", email);
                        new SubmitHttpPost().execute(json);
                    } catch(JSONException j) {
                        Toast.makeText(IssueActivity.this, "Somthing ain't right.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Not enough info was sent
                    Toast.makeText(IssueActivity.this, "Please fill out all information.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
