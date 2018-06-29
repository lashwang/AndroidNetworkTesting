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

package socks5lib.client;

import com.example.simon.androidnetworktesting.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import socks5lib.common.SocksCommand;
import socks5lib.common.SocksException;
import socks5lib.utils.LogMessage;
import socks5lib.utils.UnsignedByte;


/**
 * The class <code>GenericSocksCommandSender</code> implements {@link SocksCommandSender}.
 * 
 * @author Youchao Feng
 * @date  Mar 19, 2015 2:45:23 PM 
 * @version 1.0
 */
public class GenericSocksCommandSender implements SocksCommandSender{

	protected static final Logger logger = Logger.getLogger(GenericSocksMethodRequestor.class);

	
	/**
	 * length of IPv4 address.
	 */
	protected static final int LENGTH_OF_IPV4 = 4;
	
	/**
	 * length of IPv6 address.
	 */
	protected static final int LENGTH_OF_IPV6 = 16;
	
	@Override
	public CommandReplyMesasge send(Socket socket, SocksCommand
			command, InetAddress address,
			int port, int version) throws SocksException, IOException {
		return send(socket, command, new InetSocketAddress(address, port), version);
	}

	@Override
	public CommandReplyMesasge send(Socket socket, SocksCommand command,
			SocketAddress socketAddress, int version) throws SocksException,
			IOException {
		if (!(socketAddress instanceof InetSocketAddress)){
			throw new IllegalArgumentException("Unsupported address type");
		}

		final InputStream inputStream = socket.getInputStream();
		final OutputStream outputStream = socket.getOutputStream();
		final InetSocketAddress address = (InetSocketAddress)socketAddress;
		final byte[] bytesOfAddress = address.getAddress().getAddress();
		final int ADDRESS_LENGTH = bytesOfAddress.length;
		final int port  = address.getPort();
		byte addressType = -1;
		byte[] bufferSent = null;
		
		if(ADDRESS_LENGTH == LENGTH_OF_IPV4){
			addressType = ATYPE_IPV4;
			bufferSent = new byte[6 + LENGTH_OF_IPV4];
		}
		else if(ADDRESS_LENGTH == LENGTH_OF_IPV6){
			addressType = ATYPE_IPV6;
			bufferSent = new byte[6 + LENGTH_OF_IPV6];
		}else{
			throw new SocksException("Address error");//TODO
		}
		
		bufferSent[0] = (byte) version;
		bufferSent[1] = (byte) command.getValue();
		bufferSent[2] = RESERVED;
		bufferSent[3] = addressType;
		System.arraycopy(bytesOfAddress, 0, bufferSent, 4, ADDRESS_LENGTH);//copy address bytes
		bufferSent[4+ADDRESS_LENGTH] = (byte) ((port&0xff00)>>8);
		bufferSent[5+ADDRESS_LENGTH] = (byte)(port&0xff);

		outputStream.write(bufferSent);
		outputStream.flush();

		return checkServerReply(inputStream);
	}

	@Override
	public CommandReplyMesasge send(Socket socket, SocksCommand command, String host, int port,
			int version) throws SocksException, IOException {
		final InputStream inputStream = socket.getInputStream();
		final OutputStream outputStream = socket.getOutputStream();
		final int lengthOfHost = host.getBytes().length;
		final byte[] bufferSent = new byte[7+lengthOfHost];

		bufferSent[0] = (byte) version;
		bufferSent[1] = (byte) command.getValue();
		bufferSent[2] = RESERVED;
		bufferSent[3] = ATYPE_DOMAINNAME;
		bufferSent[4] = (byte) lengthOfHost;
		byte[] bytesOfHost = host.getBytes();
		System.arraycopy(bytesOfHost, 0, bufferSent, 5, lengthOfHost);//copy host bytes.
		bufferSent[5+host.length()] = (byte) ((port&0xff00)>>8);
		bufferSent[6+host.length()] = (byte)(port&0xff);

		outputStream.write(bufferSent);
		outputStream.flush();
		logger.debug("" + LogMessage.create(bufferSent, LogMessage.MsgType.SEND));

		return checkServerReply(inputStream);
	}

	@Override
	public CommandReplyMesasge checkServerReply(InputStream inputStream) 
			throws SocksException, IOException{
		byte serverReply = -1;
		final byte[] bufferReceived = new byte[1024];
		int bufferReceivedSize = inputStream.read(bufferReceived);

		logger.debug("{}" + LogMessage.create(bufferReceived,
				bufferReceivedSize, LogMessage.MsgType.RECEIVE));
		
		byte[] addressBytes = null;
		byte[] portBytes = new byte[2];
		
		if(bufferReceived[3] == Socks5.ATYPE_IPV4){
			addressBytes = new byte[4];
			for(int i=0;i < addressBytes.length;i++){
				addressBytes[i] = bufferReceived[4+i];
			}
			int a = UnsignedByte.toInt(addressBytes[0]);
			int b = UnsignedByte.toInt(addressBytes[1]);
			int c = UnsignedByte.toInt(addressBytes[2]);
			int d = UnsignedByte.toInt(addressBytes[3]);
			portBytes[0] = bufferReceived[8];
			portBytes[1] = bufferReceived[9];
			

			
		}
		else if(bufferReceived[3] == Socks5.ATYPE_DOMAINNAME){
			int size = bufferReceived[4];
			size = size & 0xFF;
			addressBytes = new byte[size];
			for(int i = 0; i < size ; i++){
				addressBytes[i] = bufferReceived[4+i];
			}
			portBytes[0] = bufferReceived[4+size];
			portBytes[1] = bufferReceived[5+size];
		}
		
		else if(bufferReceived[3] == Socks5.ATYPE_IPV6){
			int size = bufferReceived[4];
			size = size & 0xFF;
			addressBytes = new byte[16];
			for(int i = 0; i < addressBytes.length ; i++){
				addressBytes[i] = bufferReceived[4+i];
			}
		}


		serverReply = bufferReceived[1];

		if(serverReply != REP_SUCCEEDED){
			throw SocksException.serverReplyException(serverReply);
		}

		logger.debug("SOCKS server response success");
		
		byte[] receivedBytes = new byte[bufferReceivedSize];
		System.arraycopy(bufferReceived, 0, receivedBytes, 0, bufferReceivedSize);
		return new CommandReplyMesasge(receivedBytes);
	}

}
