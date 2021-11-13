package top.xeonwang.securityfinal.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.xeonwang.securityfinal.netty.ServerInitializer;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @author Chen Q.
 */
@Slf4j
@Component
public class Netty implements ApplicationRunner {
    @Autowired
    ServerInitializer serverInitializer;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public void start() {
        InetSocketAddress socketAddress = new InetSocketAddress("192.168.17.140", 8090);
        //new 一个主线程组
        bossGroup = new NioEventLoopGroup(1);
        //new 一个工作线程组
        workGroup = new NioEventLoopGroup(8);
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(serverInitializer)
                .localAddress(socketAddress)
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        //绑定端口,开始接收进来的连接
        try {
            ChannelFuture future = bootstrap.bind(socketAddress).sync();
            log.info("服务器启动开始监听端口: {}", socketAddress.getPort());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Order(1)
    @Async("thread")
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        log.info("启动netty心跳服务...");
//        start();
    }

    @PreDestroy
    public void destory() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workGroup.shutdownGracefully().sync();
        log.info("关闭Netty");
    }
}
