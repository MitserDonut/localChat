package com.hfut.gui;

import com.hfut.bean.ChatResponse;
import com.hfut.service.client.ChatClient;
import com.hfut.service.client.ResponseConsumer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.hfut.gui.StartGUI.CLIENT;

public class LoginGUI extends JFrame {
	private JTextField field;
	private JLabel jt1;

	public LoginGUI() {
		setTitle("登陆");
		setBounds(500, 300, 400, 200);// 设置窗口大小、位置
		JPanel j = new JPanel();
		j.setSize(400, 200);
		j.setLocation(0, 0);
		j.setBackground(Color.WHITE);
		setLayout(null);
		j.setLayout(null);
		JTextField field = new JTextField();
		this.field = field;
		field.setBounds(200, 50, 100, 30);
		j.add(field);

		JButton Button = new JButton("登陆");// 增加登陆按钮
		Button.setBounds(280, 130, 100, 30);
		j.add(Button);

		JLabel jt = new JLabel("用户名"); // 增加文本标签
		jt.setBounds(130, 50, 100, 30);
		j.add(jt);

		JLabel jt1 = new JLabel(); // 错误提示标签
		jt1.setBounds(80, 140, 200, 30);
		jt1.setFont(new Font("宋体", 0, 12));
		this.jt1 = jt1;
		j.add(jt1);

		add(j);
		setVisible(true);// 设置窗口可见状态
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 窗口关闭后停止运行
		Button.addActionListener(new ActionListener() { // button
			@Override
			public void actionPerformed(ActionEvent e) {

					try {
						String name = field.getText();
						CLIENT.addClient(name);
						setVisible(false);
						new Thread(new ResponseConsumer(name, CLIENT)).start();
					} catch (Exception e1) {
						e1.printStackTrace();
						jt1.setText(e1.getMessage());
					}
				
			}
		});
	}
}
