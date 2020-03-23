package zelix.cc.client.security.Shutdown;

import java.lang.reflect.InvocationTargetException;

public interface Shutdown {
    public void tryCrash() throws IllegalAccessException, InstantiationException, InvocationTargetException;
}
