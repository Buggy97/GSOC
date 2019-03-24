public class Consts
{
    /**
     * The maximum message length the server can send.
     * It must be less than DATAGRAM_PAYLOAD_MAX to avoid any possible errors or exceptions.
     */
    public static final int MESSAGE_PAYLOAD_MAX = 100;
    /**
     * The maximum bytes the server can send at each multicast.
     * This constant allows to correctly send and receive objects via
     * the multicast socket.
     */
    public static final int DATAGRAM_PAYLOAD_MAX = 400;
    /**
     * Default port the sockets of both client and servers bind in case its unspecified.
     */
    public static final int DEFAULT_PORT = 4242;
    /**
     * Default multicast group.
     * The sockets of both client and servers join this group in case its unspecified.
     */
    public static final String DEFAULT_GROUP = "224.0.0.1";
    /**
     * Period every which the server sends the message.
     */
    public static final long PERIOD = 10000;
}

