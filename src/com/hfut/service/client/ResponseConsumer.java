package com.hfut.service.client;

import java.util.HashMap;
import java.util.Map;

import com.hfut.bean.ChatResponse;
import com.hfut.gui.ChatRoomGUI;
import com.hfut.gui.PrivateChatGUI;

public class ResponseConsumer implements Runnable {

	private ChatRoomGUI publicChatRoom;
	private String name;
	private ChatClient client;
	public static Map<String, PrivateChatGUI> privateChat = new HashMap<>();

	public ResponseConsumer(String name, ChatClient client) {
		this.name = name;
		this.client = client;
	}

	@Override
	public void run() {
		while (true) {
			ChatResponse response = client.get(name);
			if (response != null) {
				int type = response.getHead().getType();
				// 建立连接
				if (type == 2) {
					// response中name
					String res_name = response.getSrc();
					// userlist
					String userList = response.getContent();
					// message
					String msg = res_name + "已加入聊天室\r\n";
					//自己的连接信号
					if (this.name.equals(res_name)) {
						this.publicChatRoom = new ChatRoomGUI(name);
						this.publicChatRoom.getPanel().setFrame(this.publicChatRoom);
					} else {
						;
					}
					String[] aim = userList.split(",");
					this.publicChatRoom.getUserList().removeAll();
					for (String s : aim) {
						this.publicChatRoom.getUserList().addUser(s);
					}
					this.publicChatRoom.getPanel().appendContent(msg);

				}
				// disconnect
				else if (type == 4) {
					String disname = response.getSrc();
					if(disname.equals(name)) {
						publicChatRoom.dispose();
						client.disconnect(name);
					}
					else {
						;
					}
					String userList = response.getContent();
					String[] aim = userList.split(",");
					this.publicChatRoom.getUserList().removeAll();
					for (String s : aim) {
						this.publicChatRoom.getUserList().addUser(s);
					}
					System.out.println(response.toString() + 1211 + name);

				}
				// public
				else if (type == 8) {
					this.publicChatRoom.getPanel().appendContent(response.getContent());
				}
				// private
				else if (type == 9) {
					String src = response.getSrc();
					if (privateChat.containsKey(src)) {
						;
					} else {
						PrivateChatGUI privateRoom = new PrivateChatGUI(name,src);
						privateRoom.getPrivatePanel().setFrame(privateRoom);
						privateChat.put(src, privateRoom);
					}
					privateChat.get(src).getPrivatePanel().appendContent(response.getContent());
				}

			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
