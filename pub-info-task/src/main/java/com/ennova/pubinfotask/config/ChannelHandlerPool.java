package com.ennova.pubinfotask.config;

import io.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelHandlerPool {

	public ChannelHandlerPool() {
	}

	public static Set<Channel> channelGroup = Collections.synchronizedSet(new HashSet<>());

	public static Map<String,Channel> getConnects = new ConcurrentHashMap<>();

	//public static ChannelGroup channelGroup  = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	//public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

}
