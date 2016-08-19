package nl.loemba.fons.zwave2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import java.net.URL;


public class HalActivity extends Activity {
    private static final String TAG = "Zwave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hal);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        UpdateTask myClientTask = new UpdateTask();
        myClientTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnSwitch(View view) {
        SwitchTask myClientTask = new SwitchTask();
        boolean on = ((Switch) view).isChecked();
        int id = ((Switch) view).getId();
        if (on)
            myClientTask.execute(id,1);
        else
            myClientTask.execute(id,0);
    }

    public void OnUpdate(View view) {
        UpdateTask myClientTask = new UpdateTask();
        myClientTask.execute();
    }

    public void OnHome(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    private class UpdateTask extends AsyncTask<Void, Void, Integer>
    {
        //ServiceHandler sh;

        @Override
        protected Integer doInBackground(Void... arg0)
        {
            Integer rsp1 = 0;
            Log.d(TAG, "UpdateTask");
            ZwaveStuff zws = new ZwaveStuff(getApplicationContext());
            rsp1 = zws.GetSwitchState(R.id.HalBtl);
            Log.d(TAG,"Done");
            return (rsp1);
        }

        protected void onPostExecute(Integer result) {
            Switch sw;
            sw = (Switch) findViewById(R.id.HalBtl);
            sw.setChecked((result & 0x1) == 0x1);
        }
    }

    private class SwitchTask extends AsyncTask<Integer, Void, Void>
    {
        @Override
        protected Void doInBackground(Integer... nms)
        {
            // nms[0] is Lamp ID
            // nms[1] is desired Lamp state
            URL url = null;
            String jsonStr = null;
            Log.v(TAG,"SwitchTask");
            ZwaveStuff zws = new ZwaveStuff(getApplicationContext());
            zws.SetSwitchState(nms[0],(nms[1] != 0) );
            Log.v(TAG, "Done");
            return null;
        }

    }

}
