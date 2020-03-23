package zelix.cc.client.eventAPI.api;

public class Mode<V extends Enum>
        extends Value<V> {
    public V[] modes;
    public Mode(String displayName, String name, V[] modes, V value) {
        super(displayName, name,value);
        this.modes = modes;
        this.setValue(value);
    }

    public V[] getModes() {
        return this.modes;
    }

    public void setMode(String mode) {
        V[] arrV = this.modes;
        int n = arrV.length;
        int n2 = 0;
        while (n2 < n) {
            V e = arrV[n2];
            if (e.name().equalsIgnoreCase(mode)) {
                this.setValue(e);
            }
            ++n2;
        }
    }

    public boolean isValid(String name) {
        V[] array = this.modes;
        int length = array.length;
        int i = 0;
        while (i < length) {
            V e = array[i];
            if (e.name().equalsIgnoreCase(name)) {
                return true;
            }
            ++i;
        }
        return false;
    }
}
