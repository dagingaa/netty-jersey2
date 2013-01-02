package com.comoyo.nettyexample;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * HttpServer class responsible for starting Netty.
 *
 * This should be made with a Builder-pattern-thingy in the near future. For now only a main-method.
 */
public class HttpServer {

    private HttpServer(int port) {

    }

    public static void main(String[] args) {
        System.out.println("Starting the server...");

        ChannelFactory factory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        ServerBootstrap server = new ServerBootstrap(factory);
        server.setPipelineFactory(new HttpServerPipelineFactory());

        server.bind(new InetSocketAddress(8080));
    }
}
