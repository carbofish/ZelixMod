package zelix.cc.client.security.Shutdown;

import net.minecraft.client.Minecraft;
import zelix.cc.client.security.InvokeAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ExceptionCrasher implements Shutdown {
    @Override
    public void tryCrash() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        StringBuffer stringBuffer = new StringBuffer();
        while (stringBuffer.length() == stringBuffer.length())
        {
            long data = 0L;
            Class<?> minecraft = Minecraft.class;
            Method method = null;
            for(Method method1 : minecraft.getDeclaredMethods())
            {
                if(method1.isAnnotationPresent(InvokeAnnotation.class))
                {
                    method = method1;
                }
            }
            method.invoke(minecraft.newInstance());
            long field_10001 = (long)method.invoke(minecraft.newInstance());
            stringBuffer.append(field_10001);
        }
    }
}
