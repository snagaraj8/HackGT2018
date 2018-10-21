package com.image.hackgt.hackgt2018;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    class LabelCount implements Comparable<LabelCount> {
        String label;
        float probability;
        LabelCount(String label, float probability) {
            this.label = label;
            this.probability = probability;
        }
        @Override
        public int compareTo(@NonNull LabelCount o) {
            return (int)(-100 * (this.probability - o.probability));
        }
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof LabelCount))
                return false;
            LabelCount n = (LabelCount) o;
            return n.label.equals(this.label) && this.probability == n.probability;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        HashMap<String, Integer> preferences = (HashMap<String, Integer>) getIntent()
                .getSerializableExtra("preferences");
        TextView preferenceSummary = findViewById(R.id.textView_Preferences);
        List<LabelCount> labelCounts = new ArrayList<>(preferences.size());
        int sum = 0;
        for (String key : preferences.keySet()) {
            sum += preferences.get(key);
        }
        for (String key : preferences.keySet()) {
            labelCounts.add(new LabelCount(key, (preferences.get(key) * 1.0f) / sum));
        }
        Collections.sort(labelCounts);
        String output = "PREFERENCES\n";
        for (int i = 0; i < labelCounts.size(); i++) {
            output += labelCounts.get(i).label + ": " +
                (labelCounts.get(i).probability * 100) + "\n";
        }
        preferenceSummary.setText(output);
    }
}
