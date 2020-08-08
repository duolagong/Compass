package com.programme.Fortress.Gateway;

import com.programme.Fortress.Staircase.Bank301ServerImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class GatewayServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx)   {
        System.out.println("客户端连接成功!"+ctx.channel().remoteAddress());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx)   {
        System.out.println("客户端断开连接!{}"+ctx.channel().remoteAddress());
        ctx.channel().close();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println(Thread.currentThread().getName()+"接收到的报文："+msg);
        //客户端调用服务端时需要符合规定
        if(msg.toString().startsWith("#Bank301#")){
            String responseMsg = new Bank301ServerImpl().SendMessage(
                    msg.toString().substring(msg.toString().lastIndexOf("#") + 1),
                    20,null);
            ctx.writeAndFlush(responseMsg);
        }else{
            System.out.println("反馈错误："+msg);
            ctx.writeAndFlush("返回的");
        }
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.copiedBuffer("11111111111111111", CharsetUtil.UTF_8));
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
