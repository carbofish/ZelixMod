package zelix.cc.client.eventAPI.api;

public abstract class Value<V> {
    public String displayName;
    public String configlnName;
    public V value,defaultValue;

    public Value(String displayName, String configlnName,V defaultValue) {
        this.displayName = displayName;
        this.configlnName = configlnName;
        this.defaultValue = defaultValue;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getConfiglnName() {
        return this.configlnName;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean isInteger(){
        return false;
    }
}
