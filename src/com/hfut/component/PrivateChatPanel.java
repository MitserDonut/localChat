package com.hfut.component;

import static com.hfut.gui.StartGUI.CLIENT;

import java.util.Date;

import com.hfut.gui.PrivateChatGUI;
import com.hfut.service.client.ResponseConsumer;

public class PrivateChatPanel extends AbstractChatPanel {
	private String dest = "";

	public PrivateChatPanel(String username, String dest) {
		super(username);
		this.dest = dest;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send() {
		String content = super.input.getText();
		super.input.setText("");
		if (content != "" && content != null) {
			CLIENT.sendMessage(username, dest, content);
			this.appendContent(new Date().toString() + "\r\n" + "我说:" + content + "\r\n");
		}
	}

	@Override
	public void close() {
		ResponseConsumer.privateChat.remove(dest);
		this.getFrame().dispose();
	}
}
