import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.zip.CRC32;

/**
 * This class is used for exchanging messages between the multicast client and server.
 * The checksum algorithm used is CRC32. Message content generated randomly have a maximum
 * length specified by <i><b>MAX_MESSAGE_SIZE</b></i> and with the same charset as the JVM
 * current charset.
 * @author Farjad Ali
 * @version 1.0
 */

public class MulticastMessage implements Serializable
{
    /**
     * Used to verify if sender has same version on receiver of the object.
     * This UID specifies whether to same classes are compatible with each other during
     * serialization/deserialization process, if the UID of the object sent doesnt match
     * with the receiver class definition UID then the received object cant be deserialized.
     */
    public static final long serialVersionUID = 42L;
    /**
     * The content of the multicast message.
     */
    private String message;
    /**
     * The checksum of the multicast message content.
     */
    private long checksum;
    /**
     * Random for generating messages.
     */
    private transient static Random rnd = new Random();
    /**
     * CRC32 checksum generator for messages.
     * The CRC32 choice was obvious since its fast and excellent for error detection.
     */
    private transient static CRC32 crc = new CRC32();

    /**
     * Initializes a new multicast message given its message and checksum.
     * This method allows to use custom checksums instead of the default
     * CRC32 one.
     * @param message content of the multicast message.
     * @param checksum checksum of the multicast message.
     */
    MulticastMessage(String message, long checksum)
    {
        this.message = message;
        this.checksum = checksum;
    }

    /**
     * Initializes a new multicast message given its message.
     * The method generates a CRC32 for message.
     * @param message content of the multicast message.
     */
    MulticastMessage(String message)
    {
        this.message = message;
        crc.update(message.getBytes());
        this.checksum = crc.getValue();
    }

    /**
     * Initializes a new multicast message with a random length string.
     * The string charset is the JVM current default charset and the string size is specified by MAX_MSG_SIZE.
     */
    MulticastMessage()
    {
        /*Since random bytes are used and no control are made on the generated
          string, it can include special characters such as backspace, newline, ecc..
          the implications are that in case of printing of the string there can be
          weird behaviour such random newline and deletions.
         */
        byte[] randomBytes = new byte[rnd.nextInt(Consts.MESSAGE_PAYLOAD_MAX)];
        rnd.nextBytes(randomBytes);
        this.message = new String(randomBytes, Charset.defaultCharset());
        crc.update(randomBytes);
        this.checksum = crc.getValue();
    }

    /**
     * @return the content of the multicast message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @return the checksum of the multicast message
     */
    public long getChecksum()
    {
        return checksum;
    }

    /**
     * @return the checksum of the multicast message in hex
     */
    public String getChecksumHex()
    {
        return Long.toHexString(checksum);
    }
}
