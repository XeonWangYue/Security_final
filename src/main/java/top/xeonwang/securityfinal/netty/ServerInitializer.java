package top.xeonwang.securityfinal.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @author Chen Q.
 */
@Slf4j
@Component
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    MsgServerHandler msgServerHandler;
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast("Decoder", new MessageDecoder());
        pipeline.addLast("Encoder", new MessageEncoder());
        pipeline.addLast(new HeartBeatServerHandler());
        pipeline.addLast(msgServerHandler);
    }

}
