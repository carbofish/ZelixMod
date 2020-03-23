package zelix.cc.client.eventAPI.api;

public class Amount<T extends Number>
        extends Value<T>{
    public T max,min,inc,def;

    public Amount(String displayName, String configlnName,T max,T min,T inc,T def) {
        super(displayName, configlnName, def);
        this.max = max;
        this.inc = inc;
        this.min = min;
        this.def = def;
    }
}
