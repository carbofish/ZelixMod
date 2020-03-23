package zelix.cc.client.eventAPI.api;

public class Option<V>
        extends Value<V> {

    public Option(String displayName, String name, V enabled) {
        super(displayName, name,enabled);
        this.setValue(enabled);
    }
}
