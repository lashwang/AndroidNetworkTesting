package com.example.simon.androidnetworktesting;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import sockslib.client.Socks5;
import sockslib.client.SocksProxy;
import sockslib.client.SocksSocket;


public class ProxySocketFactory extends SocketFactory {
    private SocksProxy proxy;
    private String host;
    private static final Logger mLog = Logger.getLogger(SocketFactory.class);


    public ProxySocketFactory(String proxyHost,int proxyPort,String user,String password){
        mLog.debug("ProxySocketFactory");
        if(user == null){
            proxy = new Socks5(
                    new InetSocketAddress(proxyHost, proxyPort));
        }else{
            proxy = new Socks5(
                    new InetSocketAddress(proxyHost, proxyPort), user, password);
        }
    }

    public ProxySocketFactory(String proxyHost,int proxyPort){
        mLog.debug("ProxySocketFactory with no auth");
        proxy = new Socks5(
                new InetSocketAddress(proxyHost, proxyPort));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        mLog.debug("createSocket#1");
        Socket socket = new SocksSocket(proxy, new InetSocketAddress(host,port));
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        mLog.debug("createSocket#2");
        Socket socket = new SocksSocket(proxy, new InetSocketAddress(host,port));
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        mLog.debug("createSocket#3");
        Socket socket = new SocksSocket(proxy, new InetSocketAddress(host,port));
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        mLog.debug("createSocket#4");
        Socket socket = new SocksSocket(proxy, new InetSocketAddress(address,port));
        return socket;
    }

    @Override
    public Socket createSocket() throws IOException {
        mLog.debug("createSocket#5");
        Socket socket = new SocksSocket(proxy);
        return socket;
    }
}
