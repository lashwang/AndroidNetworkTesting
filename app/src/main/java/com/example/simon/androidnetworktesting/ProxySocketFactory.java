package com.example.simon.androidnetworktesting;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import socks5lib.client.Socks5;
import socks5lib.client.SocksProxy;
import socks5lib.client.SocksSocket;


public class ProxySocketFactory extends SocketFactory {
    private SocksProxy proxy;
    private String host;
    private static final Logger mLog = Logger.getLogger(SocketFactory.class);


    public ProxySocketFactory(String proxyHost,int proxyPort,String user,String password,String host){
        if(user != null){
            proxy = new Socks5(
                    new InetSocketAddress(proxyHost, proxyPort), user, password);
        }else{
            proxy = new Socks5(
                    new InetSocketAddress(proxyHost, proxyPort));
        }
        this.host = host;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        Socket socket = new SocksSocket(proxy, new InetSocketAddress(host,port));
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        Socket socket = new SocksSocket(proxy, new InetSocketAddress(host,port));
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        Socket socket = new SocksSocket(proxy, new InetSocketAddress(host,port));
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        Socket socket = new SocksSocket(proxy, new InetSocketAddress(address,port));
        return socket;
    }

    @Override
    public Socket createSocket() throws IOException {
        mLog.debug("createSocket");
        Socket socket = new SocksSocket(proxy, new InetSocketAddress(host,80));
        return socket;
    }
}
