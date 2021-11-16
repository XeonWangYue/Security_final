package top.xeonwang.securityfinal.Netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.xeonwang.securityfinal.VO.SystemInfoVO;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Chen Q.
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class MsgServerHandler extends SimpleChannelInboundHandler<MsgProtocol> {
    @Autowired
    RedisTemplate redisTemplate;

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>();

    public String sendWarining(String hostname) {
        Channel channel;
        if ((channel = map.get(hostname)) != null) {
            MsgProtocol msg = new MsgProtocol();
            msg.setStep(1);
            byte[] content = new String("我警告你别整有的没的").getBytes(StandardCharsets.UTF_8);
            msg.setContent(content);
            msg.setLength(content.length);
            channel.writeAndFlush(msg);
        }
        return null;
    }

    public String sendDisable(String hostname) {
        Channel channel;
        if ((channel = map.get(hostname)) != null) {
            MsgProtocol msg = new MsgProtocol();
            msg.setStep(2);
            byte[] content = new String("你网没了").getBytes(StandardCharsets.UTF_8);
            msg.setContent(content);
            msg.setLength(content.length);
            channel.writeAndFlush(msg);
        }
        return null;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String ip = ctx.channel().remoteAddress().toString().split(":")[0];
        ip = ip.split("/")[1];
        Channel channel = ctx.channel();
        channelGroup.add(channel);
        map.put(ip, channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String ip = ctx.channel().remoteAddress().toString().split(":")[0];
        ip = ip.split("/")[1];
        redisTemplate.opsForHash().delete("SystemInfo", ip);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProtocol msg) throws Exception {
        log.debug("get msg from " + ctx.channel().remoteAddress());
        if (msg.getStep() == 0) {
            ObjectMapper mapper = new ObjectMapper();

            String ip = ctx.channel().remoteAddress().toString().split(":")[0];
            ip = ip.split("/")[1];
            try {
                redisTemplate.boundHashOps("SystemInfo").put(ip, new String(msg.getContent(), StandardCharsets.UTF_8));
                String s = (String) redisTemplate.boundHashOps("SystemInfo").get(ip);
                SystemInfoVO vo = mapper.readValue(msg.getContent(), SystemInfoVO.class);
                log.debug(ip + " " + vo.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常捕获 ");
        String ip = ctx.channel().remoteAddress().toString().split(":")[0];
        ip = ip.split("/")[1];
        redisTemplate.opsForHash().delete("SystemInfo", ip);
        ctx.close();
    }
}
