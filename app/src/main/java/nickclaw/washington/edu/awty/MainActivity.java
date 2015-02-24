package nickclaw.washington.edu.awty;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    private EditText message;
    private EditText number;
    private EditText interval;
    private Button button;
    private boolean started;
    AlarmManager alarm;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        // resources
        message = (EditText) findViewById(R.id.message);
        number = (EditText) findViewById(R.id.number);
        interval = (EditText) findViewById(R.id.interval);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        // alarm manager
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (state != null && state.containsKey("started")) {
            Log.i("onCreate", "Restoring instance state.");
            started = state.getBoolean("started");
        } else {
            Log.i("onCreate", "Defaulting instance state.");
            Intent intent = new Intent(this, AlarmReceiver.class);
            started = PendingIntent.getBroadcast(this, 0, intent, 0) != null;
        }
        Log.i("onCreate", "started=" + (started?"true":"false"));
        setButtonText();
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        Log.i("onSaveInstanceState", "savings instance state.");
        Log.i("onSaveInstanceState", "started=" + (started?"true":"false"));
        state.putBoolean("started", started);
        super.onSaveInstanceState(state);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, AlarmReceiver.class);

        Log.i("onClick", "Button clicked");

        if (started) {
            PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 0);
            Log.i("onClick", "Canceling");
            alarm.cancel(pending);
            pending.cancel();
        } else {
            Log.i("onClick", "Starting");
            if (message.getText().toString().equals("") || number.getText().toString().equals("") || interval.getText().toString().equals("")) {
                Log.i("onClick", "Invalid");
                return;
            }
            Log.i("onClick", "Valid");
            Log.i("onClick", "message=" + message.getText().toString());
            Log.i("onClick", "number=" + number.getText().toString());

            intent.putExtra("message", message.getText().toString());
            intent.putExtra("number", number.getText().toString());

            PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 0);
            int i = 5;
            try {
                i = Integer.parseInt(interval.getText().toString());
            } catch(Exception e) {}
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, 0, i * 1000 * 60, pending);
            Toast.makeText(this, number.getText() + ": " + message.getText(), Toast.LENGTH_LONG).show();
        }

        started = !started;
        setButtonText();
    }

    private void setButtonText() {
        button.setText(started ? "Stop" : "Start");
    }
}
