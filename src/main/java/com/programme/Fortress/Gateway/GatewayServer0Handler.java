package com.programme.Fortress.Gateway;

import Protobuf.BankMessagePOJO;
import com.programme.Fortress.Util.SpringContextUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.*;

public class GatewayServer0Handler extends SimpleChannelInboundHandler<BankMessagePOJO.MyMessage> {

    GatewayLogic gatewayLogic=(GatewayLogic) SpringContextUtil.getBean(GatewayLogic.class);

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 100, 3,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200), new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void channelActive(ChannelHandlerContext ctx)   {
        System.out.println("有新的客户端连接!"+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)   {
        System.out.println("客户端断开连接!{}"+ctx.channel().remoteAddress());
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BankMessagePOJO.MyMessage myMessage) throws Exception {
        //加入业务线程池
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("服务端接收到请求");
                Object responseMsg=gatewayLogic.DistinguishType(myMessage);
                ctx.writeAndFlush(responseMsg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            int counter = 0;
            if (event.state().equals(IdleState.READER_IDLE)) {
                if(counter>=4){
                    System.out.println("无读操作！");
                    ctx.channel().close().sync();
                    System.out.println("已与"+ctx.channel().remoteAddress()+"断开连接");
                }else{
                    counter++;
                    System.out.println("读，丢失了第 " + counter + " 个心跳包");
                }
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                if(counter>=4){
                    System.out.println("无写操作！");
                    ctx.channel().close().sync();
                    System.out.println("已与"+ctx.channel().remoteAddress()+"断开连接");
                }else{
                    counter++;
                    System.out.println("写，丢失了第 " + counter + " 个心跳包");
                }
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                //未进行读写
                System.out.println("无读写操作！");
                // 发送心跳消息
                ctx.channel().close().sync();

            }else {
                System.out.println("复位！");
                super.userEventTriggered(ctx,evt);
            }

        }
    }
}
