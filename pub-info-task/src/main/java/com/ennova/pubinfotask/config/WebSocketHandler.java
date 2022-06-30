package com.ennova.pubinfotask.config;

import com.alibaba.fastjson.JSON;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private RedisTemplate redisTemplate;
//    private final String USER = "user";
//    private final AttributeKey<String> key = AttributeKey.valueOf(USER);
    private String WEBSOCKET_PATH = "wss://139.196.150.88:8096/ws";

    static Map<String,Channel> userMap=new HashMap<>();
    static Map<Channel,String> channelMap=new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 首次连接是FullHttpRequest，处理参数
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            //ConcurrentMap<String, String> paramMap = getUrlParams(uri);
            ConcurrentMap<String, String> paramMap = getUrlParams(uri);
            //paramMap2 = paramMap;
            log.info("接收到的参数：{}", JSON.toJSONString(paramMap));
            handleHttpRequest(ctx, (FullHttpRequest) msg);
            //if (MapUtils.isEmpty(paramMap)) {
            //    paramMap.put("userId", paramMap2.get("userId"));
            //}
            HttpHeaders headers = ((FullHttpRequest) msg).headers();
            String cookie = headers.get("Cookie");
            if(null != cookie){
                String[] cookies = cookie.split(";");
                for(String c : cookies){
                    if(c.contains("token")){
                        String[] token = c.split("=");
                        if (MapUtils.isEmpty(paramMap)) {
                            log.info("netty: 请求token：{}", token[1]);
                            UserVO userVO = JWTUtil.getUserVOByToken(token[1]);
                            if (null != userVO) {
                                paramMap.put("userId", String.valueOf(userVO.getId()));
                            }
                        }
                    }
                }
            }

            online(paramMap.get("userId"), ctx.channel());




            // 首次建立连接，获取该用户的推送记录进行推送
//            List<String> ysBulletinIds = redisTemplate.opsForList().range("bulletin:add:" + paramMap.get("userId"), 0, -1);
//            if (null != ysBulletinIds && ysBulletinIds.size() > 0) {
//                ChannelHandlerPool.channelGroup.stream().forEach(channel -> {
//                    ysBulletinIds.stream().forEach(bulletinId -> {
//                        //channel.writeAndFlush(new TextWebSocketFrame("您有一条编号为" + bulletinId + "的公告需要审核！"));
//                        channel.writeAndFlush(new TextWebSocketFrame(bulletinId));
//                        redisTemplate.opsForList().remove("bulletin:add:" + paramMap.get("userId"), 0, bulletinId);
//                    });
//                });
//            }

            List<String> ysBulletinIds = redisTemplate.opsForList().range("bulletin:add:" + paramMap.get("userId"), 0, -1);
            if (null != ysBulletinIds && ysBulletinIds.size() > 0) {
                Channel channel = userMap.get(paramMap.get("userId")); //获取用户的channel
                if (null != channel) {
                    ysBulletinIds.stream().forEach(bulletinId -> {
                        channel.writeAndFlush(new TextWebSocketFrame(bulletinId));
                        redisTemplate.opsForList().remove("bulletin:add:" + paramMap.get("userId"), 0, bulletinId);
                    });

                };
            }


            // 首次建立连接，推送公告
//            List<String> pushIds = redisTemplate.opsForList().range("bulletin:push:" + paramMap.get("userId"), 0, -1);
//            if (null != pushIds && pushIds.size() > 0) {
//                ChannelHandlerPool.channelGroup.stream().forEach(channel -> {
//                    pushIds.stream().forEach(content -> {
//                        channel.writeAndFlush(new TextWebSocketFrame(content));
//                        redisTemplate.opsForList().remove("bulletin:push:" + paramMap.get("userId"), 0, content);
//                    });
//                });
//            }
            List<String> pushIds = redisTemplate.opsForList().range("bulletin:push:" + paramMap.get("userId"), 0, -1);
            if (null != pushIds && pushIds.size() > 0) {
                Channel channel = userMap.get(paramMap.get("userId")); //获取用户的channel
                if (null != channel) {
                    pushIds.stream().forEach(content -> {
                        channel.writeAndFlush(new TextWebSocketFrame(content));
                        redisTemplate.opsForList().remove("bulletin:push:" + paramMap.get("userId"), 0, content);
                    });
                }
            }

            // 首次建立连接，推送驳回
//            List<String> rejectIds = redisTemplate.opsForList().range("bulletin:reject:" + paramMap.get("userId"), 0, -1);
//            if (null != rejectIds && rejectIds.size() > 0) {
//                ChannelHandlerPool.channelGroup.stream().forEach(channel -> {
//                    rejectIds.stream().forEach(bulletinId -> {
//                        //channel.writeAndFlush(new TextWebSocketFrame("您有一条编号为" + bulletinId + "的公告已驳回！"));
//                        channel.writeAndFlush(new TextWebSocketFrame(bulletinId));
//                        redisTemplate.opsForList().remove("bulletin:reject:" + paramMap.get("userId"), 0, bulletinId);
//                    });
//                });
//            }

            List<String> rejectIds = redisTemplate.opsForList().range("bulletin:reject:" + paramMap.get("userId"), 0, -1);
            if (null != rejectIds && rejectIds.size() > 0) {
                Channel channel = userMap.get(paramMap.get("userId")); //获取用户的channel
                if (null != channel) {
                    rejectIds.stream().forEach(bulletinId -> {
                        channel.writeAndFlush(new TextWebSocketFrame(bulletinId));
                        redisTemplate.opsForList().remove("bulletin:reject:" + paramMap.get("userId"), 0, bulletinId);
                    });
                }
            }

            // 如果url包含参数，需要处理
            if (uri.contains("?")) {
                String newUri = uri.substring(0, uri.indexOf("?"));
                request.setUri(newUri);
            }
        } else if (msg instanceof TextWebSocketFrame) {
            // 调试信息
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            ctx.channel().writeAndFlush(frame.text());
            log.info("收到消息：" + frame.text());
        }
        super.channelRead(ctx, msg);

    }


    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 该请求是不是websocket upgrade请求
        if (isWebSocketUpgrade(req)) {
            // String ws = "wss://172.168.3.104:8096/ws";
            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(WEBSOCKET_PATH, null, false);
            WebSocketServerHandshaker handshaker = factory.newHandshaker(req);

            if (handshaker == null) {// 请求头不合法, 导致handshaker没创建成功
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                // 响应该请求
                handshaker.handshake(ctx.channel(), req);
            }
            return;
        }
    }

    //n1.GET? 2.Upgrade头 包含websocket字符串?
    private boolean isWebSocketUpgrade(FullHttpRequest req) {
        HttpHeaders headers = req.headers();
        log.info("netty: 请求头：{}", headers);
        //cookie
        log.info("netty: 请求cookie：{}", headers.get("Cookie"));
        //boolean empty = MapUtils.isEmpty(paramMap2);
        //    if (empty) {
        //        // request中获取cookie
        //        String cookie = headers.get("Cookie");
        //        if (StringUtils.isNotEmpty(cookie)) {
        //            String[] split = cookie.split(";");
        //            for (String s : split) {
        //                if (s.contains("token")) {
        //                    String[] split1 = s.split("=");
        //                    log.info("从request中获取token:{}", split1[1]);
        //                    UserVO userVO = JWTUtil.getUserVOByToken(split1[1]);
        //                    if (null != userVO) {
        //                        paramMap2.put("userId", String.valueOf(userVO.getId()));
        //                    }
        //                    break;
        //                }
        //            }
        //        }
        //
        //    }

        return req.method().equals(HttpMethod.GET)
                && headers.get(HttpHeaderNames.UPGRADE).equals("websocket");
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("与客户端建立连接，通道开启！" + ChannelHandlerPool.channelGroup.size());
        // 将通道加入到ChannelGroup中
        //ChannelHandlerPool.channelGroup.add(ctx.channel());
        ////添加连接
        //log.debug("客户端加入连接："+ctx.channel());
        //Channel channel = ctx.channel();
        //ChannelMap.put(channel.id().asShortText(),channel);


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        //断开连接
        //Channel channel = ctx.channel();
        //ChannelMap.remove(channel.id().asShortText());


        log.info("与客户端断开连接，通道关闭！" + ChannelHandlerPool.channelGroup.size());
        // ChannelHandlerPool.channelGroup.remove(ctx.channel());
        userMap.remove(channelMap.get(ctx.channel()));
        channelMap.remove(ctx.channel());



    }

    private static ConcurrentMap<String, String> getUrlParams(String url) {
        ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

        url = url.replace("?", ";");
        if (!url.contains(";")) {
            return map;
        }
        if (url.split(";").length > 0) {
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                if (key.equals("token")) {
                    UserVO userVO = JWTUtil.getUserVOByToken(value);
                    if (null != userVO) {
                        map.put("userId", String.valueOf(userVO.getId()));
                    }
                }
            }
            return map;
        } else {
            return map;
        }

    }


    /**
     * 上线一个用户
     *
     * @param channel
     * @param userId
     */
    private void online(String userId, Channel channel) {
        log.info("用户上线，userId：" + userId);
        log.info("用户上线，channel：" + channel);
        // 重复登陆，关闭之前的连接
        if(userMap.containsKey(userId)){
            channel.writeAndFlush(new TextWebSocketFrame("账号异地登录强制下线!"));
            channel.close();
            channelMap.remove(channel);
        }
        userMap.put(userId,channel);
        channelMap.put(channel,userId);
        ChannelHandlerPool.getConnects=userMap;

//        ChannelHandlerPool.channelGroup.add(channel);
//        channel.attr(key).set(userId);
    }

}