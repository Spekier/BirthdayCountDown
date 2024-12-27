package com.example.birthdaycountdown;

import android.content.Intent;
import android.os.Bundle;

import com.example.birthdaycountdown.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.view.View;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CountDown mCountDown; // model
    private Snackbar mSnackBar;
    private int mCalculationsDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences,
                false);
        Utils.setNightModeOnOffFromPreferenceValue(getApplicationContext()
                , getString(R.string.night_mode_key));
        setContentView();
        setSupportActionBar(binding.multiIncludeToolbar.toolbar);
        setupSnackbar();
        binding.fab.setOnClickListener(this::handleFABClick);
    }

    private void setContentView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupSnackbar() {
        View rootView = findViewById(android.R.id.content);
        mSnackBar = Snackbar.make(rootView, "", Snackbar.LENGTH_INDEFINITE);
        mSnackBar.setAnchorView(binding.fab);
    }

    private void handleFABClick(View view) {
        String dateInput = binding.contentMain.etBirthday.getText().toString();

        if (isValidDateInput(dateInput)) {
            mCalculationsDone++;
            setModelFieldDateTo(dateInput);
            String msg = generateFormattedStringOfDaysUntilBirthday();
            showSnackbarWithResults(msg);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.error_msg_date_not_valid,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidDateInput(String dateInput) {
        return !dateInput.isEmpty();
    }

    private String generateFormattedStringOfDaysUntilBirthday() {
        final long daysUntilBirthday;
        final String daysString;

        daysUntilBirthday = mCountDown.getDaysUntilNextBirthday();
        daysString = String.format(Locale.US, "%d", daysUntilBirthday);

        return String.format(Locale.getDefault(),
                "%s %s\n%s %d",
                getString(R.string.days_until_birthday), daysString,
                getResources().getQuantityString
                        (R.plurals.calcs_done, mCalculationsDone), mCalculationsDone);
    }

    private void setModelFieldDateTo(String dateInput) {
        if (mCountDown == null) {
            mCountDown = new CountDown(dateInput);
        } else {
            mCountDown.setBirthDate(dateInput);
        }
    }

    private void showSnackbarWithResults(String msg) {
        mSnackBar.setText(msg);
        mSnackBar.setAction(getString(R.string.details), v -> handleSnackbarClick());
        mSnackBar.show();
    }

    public void handleSnackbarClick() {
        // Example of handling Snackbar click, such as opening another activity
        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        intent.putExtra(getString(R.string.key_date), mCountDown.getFormattedBirthDate());
        intent.putExtra(getString(R.string.key_days_until_birthday), ""+mCountDown.getDaysUntilNextBirthday());
        intent.putExtra(getString(R.string.key_calc_count), ""+mCalculationsDone);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_about) {
            mSnackBar.setText(getString(R.string.about_body));
            mSnackBar.setAction("", null);
            mSnackBar.show();
            return true;
        }
        else if(itemId== R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (itemId == R.id.action_reset_calc_count) {
            mSnackBar.dismiss();
            mCalculationsDone = 0;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.key_calc_count), mCalculationsDone);

        outState.putBoolean("COUNTDOWN_NULL", mCountDown == null);

        if (mCountDown != null) {
            outState.putBoolean("SB_VISIBLE", mSnackBar.isShown());
            outState.putString("OBJ", mCountDown.getJSONStringFromThis());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mCalculationsDone = savedInstanceState.getInt(getString(R.string.key_calc_count));

        if (!savedInstanceState.getBoolean("COUNTDOWN_NULL")) {
            String jsonString = savedInstanceState.getString("OBJ");
            mCountDown = CountDown.fromJSONString(jsonString);

            if (savedInstanceState.getBoolean("SB_VISIBLE")) {
                String msg = generateFormattedStringOfDaysUntilBirthday();
                showSnackbarWithResults(msg);
            }
        }
    }
}
