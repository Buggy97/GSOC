import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Timer;

@SuppressWarnings("Duplicates")

public class MulticastServer
{
    public static void main(String args[])
    {
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
            /*
                The time class allows to schedule easily any form of TimerTask, in this case
                the MulticastTask (that extends a TimerTask) is scheduled to be executed every
                Consts.PERIOD milliseconds.
             */
            MulticastSocket ms = new MulticastSocket(port);
            ms.joinGroup(group);
            Timer timer = new Timer();
            MulticastTask task = new MulticastTask(ms, group, Consts.DEFAULT_PORT);
            timer.schedule(task, 0, Consts.PERIOD);

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
        }
    }
}
