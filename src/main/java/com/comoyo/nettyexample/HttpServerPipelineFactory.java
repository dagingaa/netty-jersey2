package com.comoyo.nettyexample;

import com.comoyo.nettyexample.handlers.NettyHandlerContainer;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;


class HttpServerPipelineFactory implements ChannelPipelineFactory
{
    private NettyHandlerContainer jerseyHandler;

    public HttpServerPipelineFactory()
    {
        this.jerseyHandler = getJerseyHandler();
    }

    public ChannelPipeline getPipeline() throws Exception
    {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("jerseyHandler", jerseyHandler);
        return pipeline;
    }

    private NettyHandlerContainer getJerseyHandler()
    {
        ResourceConfig rcf = new ResourceConfig();
        rcf.registerClasses(HelloResource.class);

        return new NettyHandlerContainer(new ApplicationHandler(rcf));
    }
}
