package nl.loemba.fons.zwave2;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by nly98945 on 26/10/2014.
 */
public class ZwaveStuff {

    Long updateTime;
    private static final String TAG = "Zwave";
    Context cntxt;

    public ZwaveStuff(Context cntxt) {
        this.cntxt = cntxt;
        Date now = new Date();
        updateTime = now.getTime()/1000;
    }

    public void SetSwitchState(int id, boolean on)
    {
        ServiceHandler sh = new ServiceHandler();
        String jsonStr = null;
        String[] getString = cntxt.getResources().getStringArray(R.array.getString);
        String urlStr = cntxt.getString(R.string.url);
        String swString[] = null;
        int idx = 0;
        switch (id) {
            case R.id.KmrLv:
                idx = 0;
                break;
            case R.id.KmrRv:
                idx = 1;
                break;
            case R.id.KmrLa:
                idx = 2;
                break;
            case R.id.KmrRa:
                idx = 3;
                break;
            case R.id.HalBtl:
                idx = 4;
                break;
        }
        if (on)
            swString = cntxt.getResources().getStringArray(R.array.setOnString);
        else
            swString = cntxt.getResources().getStringArray(R.array.setOffString);
        jsonStr = sh.makeServiceCall(urlStr.concat(swString[idx]),ServiceHandler.POST);
    }


    public Integer GetSwitchState(int id)
    {
        ServiceHandler sh = new ServiceHandler();
        String jsonStr = null;
        Integer response = 0;
        JSONObject jso;
        int idx = 0;

        String[] getString = cntxt.getResources().getStringArray(R.array.getString);
        String urlStr = cntxt.getString(R.string.url);
        switch (id) {
            case R.id.KmrLv:
                idx = 0;
                break;
            case R.id.KmrRv:
                idx = 1;
                break;
            case R.id.KmrLa:
                idx = 2;
                break;
            case R.id.KmrRa:
                idx = 3;
                break;
            case R.id.HalBtl:
                idx = 4;
                break;

        }
        // for a reason unknown to me, sometimes the Get call fails
        int j = 0; boolean fail = true;
        while (fail & (j < 3)) {
            jsonStr = sh.makeServiceCall(urlStr.concat(getString[idx]), ServiceHandler.POST);
            fail = jsonStr.equals("fail");
            j++;
        }
        jsonStr = sh.makeServiceCall(urlStr.concat("Data/").concat(Long.toString(updateTime) ),ServiceHandler.POST);
        Log.d(TAG,jsonStr);
        try {
            jso = new JSONObject(jsonStr);
            updateTime = jso.getLong("updateTime");
            Log.d(TAG, Long.toString(updateTime));
            String[] lvlString = cntxt.getResources().getStringArray(R.array.levelString);
            jso = jso.getJSONObject(lvlString[idx]);
            String type = jso.getString("type");
            if (type.equals("bool")) {
                Boolean state = jso.getBoolean("value");
                Log.d(TAG, Boolean.toString(state));
                response = state ? 1 : 0;
            }
            else if (type.equals("int")) {
                int state = jso.getInt("value");
                Log.d(TAG, Integer.toString(state));
                response = state;
            }
        } catch (JSONException e) {
            // do nothing
        }
        return response;
    }



}
