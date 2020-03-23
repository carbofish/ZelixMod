package zelix.cc.client.security.Shutdown;

import java.lang.reflect.InvocationTargetException;

public class LegitCrasher implements Shutdown {
    @Override
    public void tryCrash() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        System.exit(123);
    }
}
