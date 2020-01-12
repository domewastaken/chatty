package servers;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {

    public static InetAddress parseStringToAddress(String address) throws UnknownHostException {

        return InetAddress.getByAddress( new byte[]{ (byte) Integer.parseInt( address.split("\\.")[0]),
                                                     (byte) Integer.parseInt( address.split("\\.")[1]),
                                                     (byte) Integer.parseInt( address.split("\\.")[2]),
                                                     (byte) Integer.parseInt( address.split("\\.")[3]) } );
    }
}
