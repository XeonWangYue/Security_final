package top.xeonwang.securityfinal.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.xeonwang.securityfinal.VO.SystemInfoVO;

import java.nio.charset.StandardCharsets;

/**
 * @author Chen Q.
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class MsgServerHandler extends SimpleChannelInboundHandler<MsgProtocol> {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String ip = ctx.channel().remoteAddress().toString().split(":")[0];
        redisTemplate.opsForHash().delete("SystemInfo", ip);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProtocol msg) throws Exception {
        log.debug("get msg from " + ctx.channel().remoteAddress());
        if (msg.getStep() == 0) {
            ObjectMapper mapper = new ObjectMapper();

            String ip = ctx.channel().remoteAddress().toString().split(":")[0];
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
        redisTemplate.opsForHash().delete("SystemInfo", ip);
        ctx.close();
    }
}
