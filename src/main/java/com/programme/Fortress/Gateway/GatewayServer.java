package com.programme.Fortress.Gateway;

import Protobuf.BankMessagePOJO;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.LogUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Component
public class GatewayServer {

    public void start(int nettyPort){
        startService(nettyPort) ;
    }

    private void startService(int nettyPort) {
        EventLoopGroup boosGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(20);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_REUSEADDR,true)//端口复用
                    .childOption(ChannelOption.SO_BACKLOG,1024)//缓存队列大小
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//心跳保活
                    .childOption(ChannelOption.TCP_NODELAY,true)//关闭Nagle算法,高时效性发送
                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,new WriteBufferWaterMark(1,1024*1024*8))
                    .localAddress(new InetSocketAddress(nettyPort))
                    .childHandler(new ChannelInitializer<SocketChannel>() {//可以单独写个类
                        protected void initChannel(SocketChannel sh) throws Exception {
                            ChannelPipeline pipeline = sh.pipeline();
                            pipeline.addLast("decoder32",new ProtobufVarint32FrameDecoder());//半包处理
                            pipeline.addLast("decoder",new ProtobufDecoder(BankMessagePOJO.MyMessage.getDefaultInstance()));//加密的类
                            /*pipeline.addLast("encoder",new StringEncoder());*/
                            //返回客户端存在粘包问题
                            pipeline.addLast("encoder32",new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast("encoder",new ProtobufEncoder());
                            /*pipeline.addLast(new StringDecoder());*/
                            pipeline.addLast("logging", new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast("ping", new IdleStateHandler(60*1, 60*1, 60*1, TimeUnit.SECONDS));//心跳检测
                            pipeline.addLast(new GatewayServer0Handler());//业务处理
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            new LogUtil().getStamp(new MyDateUtil().getMaxDate("yyyyMMdd"));
            System.out.println("                                ...端口:"+nettyPort);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
