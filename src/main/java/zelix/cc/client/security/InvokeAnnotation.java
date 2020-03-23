package zelix.cc.client.security;


import java.lang.annotation.*;

@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface InvokeAnnotation {

    byte value() default 0;
}
