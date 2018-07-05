package ni2014.simpleeventbusimpl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ni2014.libs.bus.EventBus;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 注册EventBus
        EventBus.getInstance().register(this);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    public void onEvent(LoginEvent event){
        Log.e(TAG,"reveive event,uid is :" + event.getUid() + " , and userName is :" +
        event.getUserName());

    }
}
