/* 
 * Copyright 2015-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sockslib.client;

import com.example.simon.androidnetworktesting.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import sockslib.common.Socks5DatagramPacketHandler;
import sockslib.common.SocksException;

/**
 * The class <code>Socks5DatagramSocket</code> is a DatagramSocket that support
 * SOCKS5 proxy.<br>
 * For example:<br>
 * <pre>
 * 	SocksProxy proxy = new Socks5(new InetSocketAddress("foo.com", 1080));
 *  //Setting proxy...
 * 	DatagramSocket client = new Socks5DatagramSocket(proxy);
 *  //Just use it as normal java.net.DatagramSocket now.
 * </pre>
 * 
 * @author Youchao Feng
 * @date  Mar 23, 2015 5:54:37 PM 
 * @version 1.0
 */
public class Socks5DatagramSocket extends DatagramSocket{

	/**
	 * logger which subclass also can use.
	 */
	protected static final Logger logger = 
			Logger.getLogger(Socks5DatagramSocket.class);

	/**
	 * SOCKS proxy.
	 */
	private SocksProxy proxy;

	/**
	 * Relay server's IP address.
	 */
	private InetAddress relayServerInetAddress;

	/**
	 * Relay server's port which listens UDP  connection.
	 */
	private int relayServerPort;

	private Socks5DatagramPacketHandler datagramPacketHandler =
			new Socks5DatagramPacketHandler();

	/**
	 * Constructs a datagram socket with a {@link SocksProxy}. <br>
	 * <b>Notice:</b> The proxy must be {@link Socks5}, because only SOCKS5
	 * protocol supports UDP ASSOCIATE. It will throw {@link SocksException} if
	 * you give other {@link SocksProxy} implementation which not supports SOCKS5
	 * protocol.
	 * 
	 * @param proxy 			{@link Socks5} instance.
	 * @throws SocksException	If any error about SOCKS protocol occurs.
	 * @throws IOException		If any I/O error occurs.
	 * 
	 * @see	<a href="http://www.ietf.org/rfc/rfc1928.txt">SOCKS Protocol Version 5</a>
	 */
	public Socks5DatagramSocket(SocksProxy proxy) throws SocksException, IOException {
		super();
		this.proxy = proxy;
		if( !(proxy instanceof Socks5) ){
			throw new SocksException("Only SOCKS5 protocol support UDP ASSOCIATE");
		}
		proxy.buildConnection();
		CommandReplyMesasge mesasge = 
				proxy.requestUdpAssociat(this.getLocalAddress(), this.getLocalPort());
		this.relayServerInetAddress = mesasge.getIp();
		this.relayServerPort = mesasge.getPort();

		datagramPacketHandler.setRelayServerInetAddress(relayServerInetAddress);
		datagramPacketHandler.setRelayServerPort(relayServerPort);

		logger.info("relay server's address:"+relayServerInetAddress);
		logger.info("relay server's port:"+relayServerPort);
	}


	@Override
	public void send(DatagramPacket packet) throws SocksException, IOException {
		super.send( datagramPacketHandler.encapsulate(packet) );
	}

	@Override
	public synchronized void receive(DatagramPacket packet) 
			throws SocksException, IOException {
		super.receive(packet);
		datagramPacketHandler.decapsulate(packet);
	}

	@Override
	public void close() {
		super.close();
		//Close TCP connection.
		try {
			if(!proxy.getProxySocket().isClosed()){
				proxy.getProxySocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SocksProxy getProxy() {
		return proxy;
	}

	public void setProxy(SocksProxy proxy) {
		this.proxy = proxy;
	}


}
