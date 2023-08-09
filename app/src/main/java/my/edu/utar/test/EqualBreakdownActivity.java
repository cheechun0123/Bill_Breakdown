package my.edu.utar.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EqualBreakdownActivity extends AppCompatActivity {

    private Button saveButton;
    private Button shareButton;

    database db = new database(EqualBreakdownActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_breakdown);

        EditText billname = findViewById(R.id.billNameEditText);
        EditText totalAmountEditText = findViewById(R.id.totalAmountEditText);
        EditText numberOfPeopleEditText = findViewById(R.id.numberOfPeopleEditText);
        TextView resultTextView = findViewById(R.id.resultTextView);
        Button calculateButton = findViewById(R.id.calculateButton);
        saveButton = findViewById(R.id.saveButton);
        shareButton = findViewById(R.id.shareButton);


        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = parseDouble(totalAmountEditText.getText().toString());
                int numberOfPeople = parseInt(numberOfPeopleEditText.getText().toString());
                double equalShare = calculateEqualShare(totalAmount, numberOfPeople);

                String formattedEqualShare = String.format("%.2f", equalShare);

                resultTextView.setText("Each person need to pay RM" + formattedEqualShare);
                saveButton.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.VISIBLE);

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = parseDouble(totalAmountEditText.getText().toString());
                String billnameEditText = billname.getText().toString();
                int numberOfPeople = parseInt(numberOfPeopleEditText.getText().toString());
                double equalShare = calculateEqualShare(totalAmount, numberOfPeople);

                String formattedEqualShare = String.format("%.2f", equalShare);

                long newRowId = db.insertequal(billnameEditText, totalAmount, parseDouble(formattedEqualShare));

                if (newRowId != -1) {
                    Toast.makeText(EqualBreakdownActivity.this, "Bill data saved to database", Toast.LENGTH_SHORT).show();
                    saveButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(EqualBreakdownActivity.this, "Error saving bill data to database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String billnameEditText = billname.getText().toString();
                double totalAmount = parseDouble(totalAmountEditText.getText().toString());
                int numberOfPeople = parseInt(numberOfPeopleEditText.getText().toString());
                double equalShare = calculateEqualShare(totalAmount, numberOfPeople);

                String message = "Bill Name: " + billnameEditText + "\n" +
                        "Total Amount: RM" + totalAmount + "\n" +
                        "Each people need to pay: RM " + equalShare;

                // Create an intent to share via WhatsApp
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp"); // Use WhatsApp package name

                // Add the message to the intent
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);

                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Handle case where WhatsApp is not installed
                    Toast.makeText(EqualBreakdownActivity.this, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
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

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private double calculateEqualShare(double totalAmount, int numberOfPeople) {
        return totalAmount / numberOfPeople;
    }
}
