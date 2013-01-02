package com.comoyo.nettyexample.handlers;

import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.internal.PropertiesDelegate;
import org.glassfish.jersey.server.*;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;


import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class NettyHandlerContainer extends SimpleChannelUpstreamHandler implements Container
{
    public static final String PROPERTY_BASE_URI = "com.sun.jersey.server.impl.container.netty.baseUri";

    private ApplicationHandler application;
    private String baseUri;


    public NettyHandlerContainer(ApplicationHandler application)
    {
        this.application = application;
        this.baseUri = (String)application.getConfiguration().getProperty(PROPERTY_BASE_URI);
    }

    @Override
    public ResourceConfig getConfiguration() {
        return application.getConfiguration();
    }

    @Override
    public void reload() {
        reload(application.getConfiguration());
    }

    @Override
    public void reload(ResourceConfig resourceConfig) {
        // TODO Figure out what this does...
    }

    private final static class Writer implements ContainerResponseWriter
    {
        private final Channel channel;
        private HttpResponse response;

        private Writer(Channel channel)
        {
            this.channel = channel;
        }

        @Override
        public OutputStream writeResponseStatusAndHeaders(long l, ContainerResponse containerResponse)
                throws ContainerException {
            response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.valueOf(containerResponse.getStatus()));

            for (Map.Entry<String, List<Object>> e : containerResponse.getHeaders().entrySet())
            {
                List<String> values = new ArrayList<String>();
                for (Object v : e.getValue())
                    values.add(containerResponse.getHeaderString((v.toString())));
                response.setHeader(e.getKey(), values);
            }

            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
            response.setContent(buffer);
            return new ChannelBufferOutputStream(buffer);        }

        @Override
        public boolean suspend(long l, TimeUnit timeUnit, TimeoutHandler timeoutHandler) {
            // TODO Work out what this is, and what it does.
            return false;
        }

        @Override
        public void setSuspendTimeout(long l, TimeUnit timeUnit) throws IllegalStateException {
            // TODO Probably a good idea to do something here.
        }

        @Override
        public void commit() {
            channel.write(response).addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void failure(Throwable throwable) {
            // Figure out if something else is needed here.
            channel.close();
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent e) throws Exception
    {
        HttpRequest request = (HttpRequest)e.getMessage();

        String base = request.getUri();
        final URI baseUri = new URI(base);
        final URI requestUri = new URI(base.substring(0, base.length() - 1) + request.getUri());

        // TODO This is major. Figure out what this does, and how to implement it properly.
        // I'm guessing security-stuff such as basic-auth won't work without it.
        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        };

        PropertiesDelegate properties = new MapPropertiesDelegate();


        ContainerRequest containerRequest = new ContainerRequest(
                baseUri,
                requestUri,
                request.getMethod().getName(),
                securityContext,
                properties
        );
        containerRequest.setWriter(new Writer(context.getChannel()));

        application.handle(containerRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getChannel().close();
    }
}