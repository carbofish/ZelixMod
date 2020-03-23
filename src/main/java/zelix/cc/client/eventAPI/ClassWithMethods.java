package zelix.cc.client.eventAPI;

import java.lang.reflect.Method;
import java.util.List;

public class ClassWithMethods {
    public Class<?> clazz;
    public List<Method> methods;
    public ClassWithMethods(Class<?> clazz , List<Method> methods)
    {
        this.clazz = clazz;
        this.methods = methods;
    }
    public Class<?> getClazz()
    {
        return clazz;
    }

    public List<Method> getMethods()
    {
        return methods;
    }
}
