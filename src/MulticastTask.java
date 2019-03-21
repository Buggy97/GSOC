import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.Time;
import java.util.Arrays;
import java.util.TimerTask;

/**
 * This class sends a multicast messages to a specific multicast group, it extends a TimerTask.
 * The default multicast group and port are specified in Consts
 * @see Consts
 * @see TimerTask
 */
public class MulticastTask extends TimerTask
{
    /**
     * Receiver multicast group.
     * It has a default value specified in Consts
     * @see Consts
     */
    private InetAddress group;
    /**
     * Receiver port.
     * It has a default value specified in Consts
     * @see Consts
     */
    private int port;
    /**
     * Receiver multicast socket.
     * The sockets binds to the default address and port in case its not specified.
     */
    private MulticastSocket socket;
    /**
     * Initializes the task with default values for the address and port.
     * The default value for the address and the port are specified in Consts
     * @see Consts
     * @throws IOException
     */
    MulticastTask() throws IOException
    {
        this.group = InetAddress.getByName(Consts.DEFAULT_GROUP);
        this.port = Consts.DEFAULT_PORT;
        this.socket = new MulticastSocket(Consts.DEFAULT_PORT);
        this.socket.joinGroup(InetAddress.getByName(Consts.DEFAULT_GROUP));
    }
    /**
     * Initializes the task with given multicast socket, group address, port and Output streams.
     * Group and port are respectively the multicast group joined and the port binded by multicastSocket
     */
    MulticastTask(MulticastSocket multicastSocket,
                  InetAddress group,
                  int port)
    {
        this.socket = multicastSocket;
        this.group = group;
        this.port = port;
    }

    /**
     * This task sends a MulticastMessage with a random string of a random length on the socket.
     * The method prints the content of the message and the time it was sent, since the random
     * generation of the message might include special characters (e.g. backspace), the printing of
     * the message can have unexpected results.
     * @see MulticastMessage
     */
    @Override
    public void run()
    {
        try
        {
            MulticastMessage randomMessage = new MulticastMessage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(randomMessage);
            oos.flush(); //We must make sure the message is sent.
            /* processArray is a helper function that makes sure the bytes in the
               packet are always are Consts.DATAGRAM_PAYLOAD_MAX.
             */
            DatagramPacket messagePacket = new DatagramPacket(this.processArray(baos.toByteArray()),
                                                                Consts.DATAGRAM_PAYLOAD_MAX,
                                                                this.group,
                                                                this.port);
            this.socket.send(messagePacket);
            System.out.printf("%s\t%s\n", new Time(System.currentTimeMillis()), randomMessage.getMessage());
            //To avoid memory leak
            oos.close();
            baos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private byte[] processArray(byte[] data) throws Exception
    {
        if(data.length > Consts.DATAGRAM_PAYLOAD_MAX)
            throw new Exception("data too big. ");
        return Arrays.copyOf(data, Consts.DATAGRAM_PAYLOAD_MAX);
    }
}
