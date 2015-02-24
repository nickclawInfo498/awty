package nickclaw.washington.edu.awty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nickclaw on 2/23/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.i("Receiver", "Receiving alarm.");
        String message = intent.getStringExtra("message");
        String number = intent.getStringExtra("number");

        Log.i("Receiver", "message=" + message);
        Log.i("Receiver", "number=" + number);

        Toast.makeText(ctx, (number + ": " + message), Toast.LENGTH_LONG).show();
    }
}
