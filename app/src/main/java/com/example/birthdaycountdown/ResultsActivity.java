package com.example.birthdaycountdown;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.birthdaycountdown.databinding.ActivityResultsBinding;

import android.view.MenuItem;

import androidx.annotation.NonNull;

public class ResultsActivity extends AppCompatActivity {

    private ActivityResultsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Inflate the layout using data binding

        setupToolbar(); // Set up the toolbar as the action bar
        setupFloatingActionButton(); // Configure the Floating Action Button
        processIncomingData();
    }

    private void processIncomingData() {
        Intent intent = getIntent();

        String birthDate = intent.getStringExtra(getString(R.string.key_date));
        String daysUntilBirthday = intent.getStringExtra(getString(R.string.key_days_until_birthday));
        String calculationsDone = intent.getStringExtra(getString(R.string.key_calc_count));

        binding.contentResults.tvBirthDate.setText("Birthdate: " + birthDate);
        binding.contentResults.tvDaysUntilBirthday.setText("Days until birthday: " +  daysUntilBirthday);
        binding.contentResults.tvCalculationsDone.setText("Calculations done: " +  calculationsDone);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set the toolbar as the action bar
        if (getSupportActionBar() !=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription("Back");
        }
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> finish());
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

}
