package com.hfut.gui;

import com.hfut.component.PrivateChatPanel;

import javax.swing.*;

public class PrivateChatGUI extends JFrame {

	private PrivateChatPanel privatePanel;
	
    public PrivateChatPanel getPrivatePanel() {
		return privatePanel;
	}

	public void setPrivatePanel(PrivateChatPanel privatePanel) {
		this.privatePanel = privatePanel;
	}

	public PrivateChatGUI(String username,String dest) {
    	this.privatePanel = new PrivateChatPanel(username,dest);
        add(this.privatePanel);
        this.setTitle(dest);
        setSize(600, 600);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
