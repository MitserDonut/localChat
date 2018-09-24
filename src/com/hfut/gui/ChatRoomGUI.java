package com.hfut.gui;

import com.hfut.component.PublicChatPanel;
import com.hfut.component.UserList;
import com.hfut.service.client.ChatClient;
import javax.swing.*;
import static com.hfut.gui.StartGUI.CLIENT;

public class ChatRoomGUI extends JFrame {
    private ChatClient client;
    private PublicChatPanel panel;
    private UserList userList;

    public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}

	public PublicChatPanel getPanel() {
		return panel;
	}

	public void setPanel(PublicChatPanel panel) {
		this.panel = panel;
	}

	public ChatRoomGUI(String username) {
        setTitle("聊天室");
        setResizable(false);
        setLayout(null);
        this.panel = new PublicChatPanel(username);
        add(this.panel);
        UserList userList = new UserList(username);
        this.userList = userList;
        userList.setLocation(610, 0);
        add(userList);
        setSize(820, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
    }

    public ChatClient getClient() {
        return client;
    }

    public void setClient(ChatClient client) {
        this.client = client;
    }
}
