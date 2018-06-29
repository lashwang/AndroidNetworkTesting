package com.example.simon.androidnetworktesting;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import sockslib.client.Socks5;
import sockslib.client.SocksProxy;
import sockslib.client.SocksSocket;


public class ProxySocketFactory extends SocketFactory {
    private SocksProxy proxy;
    private InetSocketAddress address;

    public ProxySocketFactory(String proxyHost,int proxyPort,String user,String password){
        if(user != null){
            proxy = new Socks5(
                    new InetSocketAddress(proxyHost, proxyPort), user, password);
        }else{
            proxy = new Socks5(
                    new InetSocketAddress(proxyHost, proxyPort));
        }
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
}
