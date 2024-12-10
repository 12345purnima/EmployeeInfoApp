package com.example.employeemanagementapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);



        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


            ListPreference listPreference = findPreference("language");  // Make sure the key matches your ListPreference key in XML
            if (listPreference != null) {
                listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    String value = (String) newValue;

                    updateLocale(value);

                    return true;  // True to update the state of the Preference with the new value.
                });

                // Optionally update the locale to match current preference at fragment creation
                updateLocale(listPreference.getValue());
            }

            SwitchPreferenceCompat themeSwitch = findPreference("theme"); // Get the switch preference

            assert themeSwitch != null;
            themeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    boolean isLightMode = (boolean) newValue;
                    Log.d( "theme: ", String.valueOf(isLightMode));
                    updateTheme(isLightMode); // Update theme based on switch state
                    return true;
                }
            });
            Preference aboutUsPreference = findPreference("about_us");
            if (aboutUsPreference != null) {
                aboutUsPreference.setOnPreferenceClickListener(preference -> {
                    // Show About Us dialog or open a new Activity/Fragment
                    showAboutUsDialog();
                    return true;
                });
            }

        }

        private void updateLocale(String lang) {
            Locale locale;
            if (lang.equals("नेपाली")) {
                locale = new Locale("np");
            } else {
                locale = Locale.ENGLISH; // Default language
            }
            if (!locale.equals(Locale.getDefault())) {
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.setLocale(locale);

                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                // Inside updateLocale() method in SettingsFragment
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                preferences.edit().putString("selected_language", lang).apply();
                requireActivity().recreate(); // Only recreate if locale has actually changed
            }// Refresh the activity to apply changes
        }

        private void updateTheme(boolean isLightMode) {
            int themeId;
            if (isLightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

        }
        private void showAboutUsDialog() {
            // Use a formatted message for better readability
            String message = "Features:\n" +
                    "- Secure login\n" +
                    "- Employee management\n" +
                    "- Multiple languages\n" +
                    "- Dark and light themes\n\n" +
                    "Developed by:\n" +
                    "Purnima Bolakhe and Samjhana Keecy\n\n" +
                    "Contact Us:\n" +
                    "- Purnima: 9843168872\n" +
                    "- Samjhana: 9863599615\n" +
                    "Email: empInfo@gmail.com\n\n" +
                    "Acknowledgments:\n" +
                    "- Room Database for Android\n" +
                    "- Material Design Icons\n\n" +
                    "Privacy Policy:\n" +
                    "Visit: www.employeeapp.com/privacy";

            // Create and show the dialog
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("About Us") // Set the title
                    .setMessage(message) // Set the formatted message
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()) // Add OK button
                    .show();
        }

    }
}