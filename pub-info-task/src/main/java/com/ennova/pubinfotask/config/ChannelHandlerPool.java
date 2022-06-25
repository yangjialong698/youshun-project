package com.ennova.pubinfotask.config;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelHandlerPool {

	public ChannelHandlerPool() {
	}

	public static Set<Channel> channelGroup = Collections.synchronizedSet(new HashSet<>());

	//public static ChannelGroup channelGroup  = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

}
