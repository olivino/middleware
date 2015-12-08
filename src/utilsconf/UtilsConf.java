package utilsconf;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class UtilsConf {

	public static final int MIN_PORT_NUMBER = 8000;
	public static final int MAX_PORT_NUMBER = 9000;

	public static int nextPortAvailable() {
		int p = MIN_PORT_NUMBER;

		while (!portAvailable(p))
			p++;
		return p;

	}

	public static boolean portAvailable(int port) {
		if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
			throw new IllegalArgumentException("Invalid start port: " + port);
		}

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}

		return false;
	}
}
