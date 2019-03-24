import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.sql.Time;

@SuppressWarnings("Duplicates")

public class MulticastClient
{
    public static void main(String args[])
    {
        MulticastSocket socket;
        InetAddress group;
        int port;
        try
        {
            //These clauses are just for checking and parsing possible CLI arguments.
            if(args.length >= 1)
            {
                group = InetAddress.getByName(args[0]);
                System.out.printf("<Info> Specified group is %s.\n", group.getHostAddress());
            }
            else
            {
                System.out.printf("<Warning> Group not specified, going to use default %s.\n", Consts.DEFAULT_GROUP);
                group = InetAddress.getByName(Consts.DEFAULT_GROUP);
            }
            if(args.length >= 2)
            {
                port = Integer.parseInt(args[1]);
                System.out.printf("<Info> Specified port is %s.\n", port);
            }
            else
            {
                System.out.printf("<Warning> Port not specified, going to use default %d.\n", Consts.DEFAULT_PORT);
                port = Consts.DEFAULT_PORT;
            }
            socket = new MulticastSocket(port);
            socket.joinGroup(group);
            System.out.printf("<Info> Listening...\n");
            while(true)
            {
                byte[] messageBytes = new byte[Consts.DATAGRAM_PAYLOAD_MAX];
                DatagramPacket messagePacket = new DatagramPacket(messageBytes, Consts.DATAGRAM_PAYLOAD_MAX);
                socket.receive(messagePacket);
                ByteArrayInputStream bais = new ByteArrayInputStream(messagePacket.getData());
                ObjectInputStream oos = new ObjectInputStream(bais);
                MulticastMessage message = (MulticastMessage) oos.readObject();
                System.out.printf("%s\t%s\n", new Time(System.currentTimeMillis()), message.getMessage());
            }
        } catch(UnknownHostException e)
        {
            System.out.printf("<Error> Invalid group address!\n");
            e.printStackTrace();
        } catch (NumberFormatException e1)
        {
            System.out.printf("<Error> Invalid port number!\n");
            e1.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
