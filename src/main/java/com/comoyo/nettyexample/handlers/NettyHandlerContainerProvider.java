package com.comoyo.nettyexample.handlers;

import org.glassfish.jersey.internal.ProcessingException;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.spi.ContainerProvider;

import javax.ws.rs.ext.Provider;

@Provider
public class NettyHandlerContainerProvider implements ContainerProvider {

    @Override
    public <T> T createContainer(Class<T> type, ApplicationHandler applicationHandler) throws ProcessingException {
        if (type != NettyHandlerContainer.class) {
            return null;
        }
        return type.cast(new NettyHandlerContainer(applicationHandler));
    }
}