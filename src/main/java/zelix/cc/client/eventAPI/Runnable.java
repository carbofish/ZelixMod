package zelix.cc.client.eventAPI;

import java.lang.annotation.*;

@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Runnable {

    byte value() default EventOrder.MEDIUM;
}

