package zelix.cc.client.eventAPI;

public class EventOrder {
    public static final byte
            /**
             * Highest priority, called first.
             */
            HIGHEST = 0,
    /**
     * High priority, called after the highest priority.
     */
    HIGH = 1,
    /**
     * Medium priority, called after the high priority.
     */
    MEDIUM = 2,
    /**
     * Low priority, called after the medium priority.
     */
    LOW = 3,
    /**
     * Lowest priority, called after all the other priorities.
     */
    LOWEST = 4;

    /**
     * Array containing all the prioriy values.
     */
    public static final byte[] VALUE_ARRAY;

    /**
     * Sets up the VALUE_ARRAY the first time anything in this class is called.
     */
    static {
        VALUE_ARRAY = new byte[]{
                HIGHEST,
                HIGH,
                MEDIUM,
                LOW,
                LOWEST
        };
    }
}
