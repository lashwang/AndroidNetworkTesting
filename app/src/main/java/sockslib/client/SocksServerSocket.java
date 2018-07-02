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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import sockslib.common.SocksException;

/**
 * The class <code>SocksServerSocket</code> is server socket 
 * that can bind a port at SOCKS server and accept a connection 
 * with SOCKS4 or SOCKS5. This server socket can only accept one 
 * connection from specified IP address and port.
 * protocol.
 * 
 * @author Youchao Feng
 * @date  Mar 25, 2015 11:40:36 AM 
 * @version 1.0
 */
public class SocksServerSocket extends ServerSocket{
	
	/**
	 * Logger.
	 */
	protected static final Logger logger = Logger.getLogger(SocksServerSocket.class);
	
	/**
	 * SOCKS proxy.
	 */
	private SocksProxy proxy;
	
	/**
	 * The remote host's IP address that will connect the server. 
	 */
	private InetAddress incomeAddress;
	
	
	/**
	 * The remote host's port that will connect the server.
	 */
	private int incomePort;
	
	
	/**
	 * Server's IP address.
	 */
	private InetAddress bindAddress;
	
	/**
	 * Server's port.
	 */
	private int bindPort;
	
	/**
	 * If {@link #accept()} is called, it will be <code>true</code>.
	 */
	private boolean alreadyAccepted = false;
	
	
	/**
	 * Constructs a server socket. This server socket will established in 
	 * SOCKS server.
	 * 
	 * @param proxy				SOCKS proxy.
	 * @param inetAddress		The IP address that server socket will accept.
	 * @param port				The port that server socket will accept.
	 * @throws SocksException	If any error about SOCKS protocol occurs.
	 * @throws IOException		If any I/O error occurs.
	 */
	public SocksServerSocket(SocksProxy proxy, InetAddress inetAddress, int port)
			throws SocksException, IOException {
		this.proxy = proxy;
		this.incomePort = port;
		this.incomeAddress = inetAddress;
		proxy.buildConnection();
		//Send BIND command to SOCKS server.
		CommandReplyMesasge  replyMesasge = proxy.requestBind(incomeAddress, incomePort);
		//Get a bind IP and port in proxy server.
		bindAddress = replyMesasge.getIp();
		bindPort = replyMesasge.getPort();
		//logger.debug("Bind at {}:{}", bindAddress, bindPort);
	}

	/**
	 * Accepts a connection.<br>
	 * <b>Notice:</b> This method can be called only once. It will throw
	 * SocksException if this method is called more than once.
	 */
	@Override
	public synchronized Socket accept() throws SocksException, IOException {
		
		if(alreadyAccepted){
			throw new SocksException("SOCKS4/SOCKS5 protocol only allows one income connection");
		}

		alreadyAccepted = true;
		return proxy.accept();
	}

	public InetAddress getBindAddress() {
		return bindAddress;
	}

	public void setBindAddress(InetAddress bindAddress) {
		this.bindAddress = bindAddress;
	}

	public int getBindPort() {
		return bindPort;
	}

	public void setBindPort(int bindPort) {
		this.bindPort = bindPort;
	}
	
	public SocketAddress getBindSocketAddress(){
		return new InetSocketAddress(bindAddress, bindPort);
	}
}
