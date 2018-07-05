# SimpleEventBus
简单的EventBus实现


# 假设使用
```java
class MainActivity{
    @Override
    public void onCreate(){
        EventBus.getInstance().register(this);
    }

    public void onEvent(LoginEvent event){
        // get event info
    }
}

class LoginEvent{
    private String uid;
    private String userName;

    // getters and setters
}

class ProfileActivity{
    private void login(){
        // just mock
        LoginEvent event = new LoginEvent();
        event.setUid("001");
        event.setUserName("宸笙");
        // post event
        EventBus.getInstance().post(event);
    }
}
```
# 使用规约
1. 组件(订阅者)向EventBus注册，有对应的解注册操作；
2. 定义好事件类如上例中的LoginEvent；
3. 定义好接收到事件会被回调的方法，建议是public，参数仅一个，命名为onEvent，这里采用**惯例大于配置或者约束**的方式；
4. 在触发事件的位置创建事件并调用EventBus的post方法，订阅者的onEvent方法就能被调用到；
# 反推实现
```java
class EventBus{
    private Map<Object,List<Method>> methodMap = new HashMap<>();


    // 单例模式提供getInstance方法
    public static EventBus getInstance(){
        // ...
    }

    public void register(Object target){
        // 1 找到target中的方法集合并过滤赋予
        List<Method> wantedMethods = new ArrayList<>();
        // 2 put到methodMap中
        methodMap.put(target,wantedMethods);
    }

    public void unRegister(Object target){
        methodMap.remove(target);
    }

    public void post(Object event){
        // 1 获取事件类型
        // 2 遍历methodMap，找到对应的target和List<Method>
        // 3 再遍历List<Method>，若方法的第一个参数和事件类型匹配，则调用方法
    }


}
```
具体实现：

```java
public class EventBus {

    private Map<Object,List<Method>> methodMap = new HashMap<>();

    public static EventBus instance;

    private EventBus(){}

    public static EventBus getInstance(){
        if (instance == null){
            instance = new EventBus();
        }
        return instance;
    }

    public void register(Object target){
        // findMethods
        List<Method> wantedMethods = new ArrayList<>();
        Method[] declaredMethods = target.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (!Modifier.isPublic(method.getModifiers())){
                continue;
            }
            if (method.getParameterTypes().length != 1){
                continue;
            }
            if (!method.getName().equals("onEvent")){
                continue;
            }
            wantedMethods.add(method);
        }

        if (wantedMethods == null || wantedMethods.isEmpty()){
            return;
        }

        methodMap.put(target,wantedMethods);
    }

    public void unRegister(Object target){
        methodMap.remove(target);
    }

    public void post(Object event){

        Class<?> eventClass = event.getClass();
        for (Map.Entry<Object, List<Method>> entry : methodMap.entrySet()) {
            Object target = entry.getKey();
            List<Method> methods = entry.getValue();
            if (methods == null || methods.isEmpty()){
                return;
            }
            for (Method method : methods) {
                // 匹配事件类型
                if (eventClass.equals(method.getParameterTypes()[0])){
                    try {
                        method.invoke(target,event);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


```
# 末
该版本简单实现了一个EventBus，后续会继续修改和改造，代码移步[这里](https://github.com/Ni2014/SimpleEventBus)。
