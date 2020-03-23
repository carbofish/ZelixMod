package zelix.cc.client.security.Shutdown;

import net.minecraft.crash.CrashReport;
import zelix.cc.client.security.InvokeAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MCCrasher implements Shutdown {
    @Override
    public void tryCrash() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> minecraft = CallMethod.class;
        System.out.println(minecraft.getName());
        Method method = null;
        for(Method method1 : minecraft.getDeclaredMethods())
        {
            if(method1.isAnnotationPresent(InvokeAnnotation.class))
            {
                method = method1;
            }
        }
        method.invoke(minecraft.newInstance());
    }
    public static MCCrasher getInstance(){
        return new MCCrasher();
    }
}
