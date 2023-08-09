package my.edu.utar.test;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import my.edu.utar.test.database;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomBreakdownActivity extends AppCompatActivity {

    private LinearLayout personLayout;
    private EditText totalAmountEditText;
    private ArrayList<View> personViews = new ArrayList<>();
    private String[] names;
    private Button saveButton;
    private int currentAttempt = 1; // Initialize with the default attempt number
    private EditText billNameEditText;

    database db = new database(CustomBreakdownActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_breakdown);

        totalAmountEditText = findViewById(R.id.totalAmountEditText);
        personLayout = findViewById(R.id.personLayout);
        Button calculateButton = findViewById(R.id.calculateButton);
        Button undoButton = findViewById(R.id.undoButton);
        saveButton = findViewById(R.id.saveButton);
        billNameEditText = findViewById(R.id.billNameEditText);


        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = parseDouble(totalAmountEditText.getText().toString());
                double[] percentages = getPercentagesFromInputs();
                String[] names = getNameArrayFromInputs(); // Get the names as a string array
                double[] customShares = calculateCustomShares(totalAmount, percentages);
                displayCustomShares(names, customShares);
                String billName = billNameEditText.getText().toString();

                // Show the Save button
                saveButton.setVisibility(View.VISIBLE);
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoAddPerson();
            }
        });

        // Add initial person input field
        addPersonInputField();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = parseDouble(totalAmountEditText.getText().toString());
                String billnameEditText = billNameEditText.getText().toString();
                double[] percentages = getPercentagesFromInputs();
                double[] customShares = calculateCustomShares(totalAmount, percentages);


                String[] names = getNameArrayFromInputs(); // Get the names as a string array
                List<String> personNames = Arrays.asList(names);

                List<Double> customSharesList = new ArrayList<>();
                for (int i = 0; i < customShares.length; i++) {
                    customSharesList.add(customShares[i]);
                }

                long newRowId = db.insert(billnameEditText, totalAmount, names, customShares);

                if (newRowId != -1) {
                    Toast.makeText(CustomBreakdownActivity.this, "Custom shares saved to database", Toast.LENGTH_SHORT).show();
                    saveButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(CustomBreakdownActivity.this, "Error saving custom shares to database", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }




    private double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private double[] getPercentagesFromInputs() {
        int count = personLayout.getChildCount();
        double[] percentages = new double[count];

        for (int i = 0; i < count; i++) {
            EditText percentageEditText = (EditText) personViews.get(i).findViewById(R.id.personPercentageEditText);
            percentages[i] = parseDouble(percentageEditText.getText().toString());
        }

        return percentages;
    }


    private String[] getNameArrayFromInputs() {
        List<String> nameList = new ArrayList<>();
        int count = personLayout.getChildCount();

        for (int i = 0; i < count; i++) {
            EditText percentageEditName = (EditText) personViews.get(i).findViewById(R.id.personNameEditText);
            String name = percentageEditName.getText().toString();
            nameList.add(name);
        }

        return nameList.toArray(new String[0]); // Convert the list to a string array
    }

    private double[] calculateCustomShares(double totalAmount, double[] percentages) {
        double[] customShares = new double[percentages.length];
        double totalPercentage = 0.0;

        for (double percentage : percentages) {
            totalPercentage += percentage;
        }

        if (totalPercentage != 100.0) {
            return new double[0]; // Return an empty array if percentages don't add up to 100%
        }

        for (int i = 0; i < customShares.length; i++) {
            customShares[i] = (totalAmount * percentages[i]) / 100;
        }

        return customShares;
    }


    private void displayCustomShares(String[] names, double[] customShares) {
        TextView resultTextView = findViewById(R.id.resultTextView);

        if (customShares.length == 0) {
            resultTextView.setText("Percentages must add up to 100%");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < customShares.length; i++) {
                sb.append(names[i]).append(" need to pay RM ").append(customShares[i]).append("\n");
            }
            resultTextView.setText(sb.toString());
        }
    }

    private void addPersonInputField() {
        View personView = getLayoutInflater().inflate(R.layout.person_input_layout, personLayout, false);
        personViews.add(personView);
        personLayout.addView(personView);

    }


    public void onAddPersonClick(View view) {
        addPersonInputField();
    }

    private void undoAddPerson() {
        if (!personViews.isEmpty()) {
            View removedPerson = personViews.remove(personViews.size() - 1);
            personLayout.removeView(removedPerson);
        }
    }
}