package my.edu.utar.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button equalBreakdownButton = findViewById(R.id.equalBreakdownButton);
        Button customBreakdownButton = findViewById(R.id.customBreakdownButton);
        Button historybutton = findViewById(R.id.historybutton);


        equalBreakdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EqualBreakdownActivity.class);
                startActivity(intent);
            }
        });

        customBreakdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomBreakdownActivity.class);
                startActivity(intent);
            }
        });


        historybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BillHistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
