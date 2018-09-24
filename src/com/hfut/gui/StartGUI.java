package com.hfut.gui;

import com.hfut.service.client.ChatClient;
import com.hfut.service.client.ResponseConsumer;

import javax.swing.*;

public class StartGUI extends JFrame {
	public static ChatClient CLIENT = new ChatClient();

	static {
		new Thread(() -> CLIENT.listen()).start();
	}

	public StartGUI() {
		setTitle("启动");
		setBounds(500, 300, 400, 200);
		setLayout(null);
		JButton button = new JButton("启动客户端");
		button.setBounds(150, 57, 100, 40);
		button.addActionListener((e) -> {
			new LoginGUI();
			
		});
		add(button);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String args[]) {
		new StartGUI();
	}
}
