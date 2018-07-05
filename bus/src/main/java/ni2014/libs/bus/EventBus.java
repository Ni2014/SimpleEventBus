package ni2014.libs.bus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

