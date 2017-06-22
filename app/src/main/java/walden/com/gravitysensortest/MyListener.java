package walden.com.gravitysensortest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class MyListener implements SensorEventListener {
    private PostValueInterface mPost;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        if (mPost != null) {
            mPost.postValue(values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void setPostValue(PostValueInterface post) {
        mPost = post;
    }
}
