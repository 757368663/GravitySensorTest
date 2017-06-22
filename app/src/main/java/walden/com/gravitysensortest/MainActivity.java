package walden.com.gravitysensortest;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private MyListener listener;
    private PendulumBall pendulumBall;
    private RollBall rollBall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(pendulumBall=new MyView(this,null));
        initView();
        init();
    }

    private void initView() {
        pendulumBall = (PendulumBall) findViewById(R.id.gravityview);
        rollBall = (RollBall) findViewById(R.id.gravityview1);
    }

    private void init() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        int senserType = Sensor.TYPE_GRAVITY;
        listener = new MyListener();
        listener.setPostValue(new PostValueInterface() {
            @Override
            public void postValue(float[] value) {
                pendulumBall.setValue(value);
                rollBall.setValue(value);
            }
        });
        sensorManager.registerListener(
                listener,
                sensorManager.getDefaultSensor(senserType),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(listener);
    }
}
