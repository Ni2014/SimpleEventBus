package ni2014.simpleeventbusimpl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ni2014.libs.bus.EventBus;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1 构造事件
                LoginEvent event = new LoginEvent();
                event.setUid("001");
                event.setUserName("宸笙");
                // 2 发送事件
                EventBus.getInstance().post(event);
            }
        });
    }


}
