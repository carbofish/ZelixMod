package zelix.cc.client.security.Shutdown;

import net.minecraft.client.Minecraft;
import zelix.cc.client.security.InvokeAnnotation;

import java.lang.reflect.Method;

public class CallMethod {
    @InvokeAnnotation
    public void shutDown(){
        try{
            Class<?> minecraft = Minecraft.class;
            Method method = null;
            for(Method method1 : minecraft.getDeclaredMethods()) {
                if(method1.isAnnotationPresent(InvokeAnnotation.class)) {
                    method = method1;
                }
            }
            method.invoke(minecraft.newInstance());
        }catch (Exception e){

        }
    }
}
