package com.hfut.service.server;

import com.hfut.bean.ChatRequest;
import com.hfut.bean.ChatResponse;
import com.hfut.bean.Header;
import com.hfut.utils.BytesUtils;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class MessageHelper {
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public <T extends Serializable> T readObject(SocketChannel client, Class<T> clazz) throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
		client.read(buffer);
		System.out.println(new String(buffer.array()));
		T obj = BytesUtils.bytesToObject(buffer.array(), clazz);
		return obj;
	}

	public String handleMessage(ChatRequest request, Set<String> list) {
		int type = request.getHead().getType();
		StringBuilder sb = new StringBuilder();
		String separator = System.getProperty("line.separator");
		if ((type & 2) != 0) {
			sb.append(userListToString(list));
		}
		if ((type & 4) != 0) {
			sb.append(userListToString(list));
		}
		if ((type & 8) != 0) {
			sb.append(sdf.format(new Date())).append(separator);
			sb.append(request.getName()).append("说：").append(request.getContent());
			sb.append(separator);
		}
		return sb.toString();
	}

	public ChatResponse buildResponse(ChatRequest request, String errorMsg, Set<String> list) {
		ChatResponse response = new ChatResponse();
		response.setContent(handleMessage(request, list));
		response.setSucceed(false);
		response.setHead(buildHeader(request));
		response.getHead().setType(4);
		response.setErrorMsg(errorMsg);
		return response;
	}

	public ChatResponse buildResponse(ChatRequest request, Set<String> list) {
		ChatResponse response = new ChatResponse();
		response.setContent(handleMessage(request, list));
		response.setSucceed(true);
		response.setHead(buildHeader(request));
		response.setSrc(request.getName());
		return response;
	}

	private Header buildHeader(ChatRequest request) {
		Header header = new Header();
		header.setDate(new Date());
		header.setType(request.getHead().getType());
		return header;
	}

	private String userListToString(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for (String s : set) {
			sb.append(s).append(",");
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
			
		}
		return sb.toString();
	}

}
