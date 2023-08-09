package my.edu.utar.test;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BillHistoryActivity extends AppCompatActivity {

    private TextView historyTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_history);

        historyTextView = findViewById(R.id.historyContentTextView);

        database db = new database(this);
        db.openToRead();
        String history = db.retrieveequal();
        db.close();

        historyTextView.setText(history);
    }
}