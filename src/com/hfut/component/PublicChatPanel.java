package com.hfut.component;

import static com.hfut.gui.StartGUI.CLIENT;

public class PublicChatPanel extends AbstractChatPanel {
	public PublicChatPanel(String username) {
		super(username);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send() {
		String content = super.input.getText();
		super.input.setText("");
		if (content != "" && content != null) {
			CLIENT.sendMessage(username, content);
		}
	}

	@Override
	public void close() {
		CLIENT.disconnect(username);
		this.getFrame().dispose();
	}
}
