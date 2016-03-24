package com.wheretoeat.service.chat;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ChatUserService {
	private static final Logger log = Logger.getLogger(ChatUserService.class);

	private final Map<String, ChatUser> onlineUsers = new ConcurrentHashMap<>();
	private final Map<String, ChatUser> onlineSessions = new ConcurrentHashMap<>();
	
	public Collection<ChatUser> getOnlineUsers() {
		return onlineUsers.values();
	}
	
	public ChatUser get(String sessionId) {
		return onlineSessions.get(sessionId);
	}
	
	public void login(String sessionId, ChatUser user) {
		onlineUsers.put(user.getUsername(), user);
		onlineSessions.put(sessionId, user);
	}
	
	public void logout(String sessionId) {
		ChatUser user = onlineSessions.get(sessionId);
		onlineSessions.remove(sessionId);
		
		if (!onlineSessions.containsValue(user))
			onlineUsers.remove(user.getUsername());
	}
	
}
