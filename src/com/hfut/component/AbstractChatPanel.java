package com.hfut.component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

import com.hfut.gui.LoginGUI;


public abstract class AbstractChatPanel extends JPanel {

	protected JTextArea jta;
	protected String username;
	protected JTextArea input;
	protected JFrame frame;
	
    public JFrame getFrame() {
		return frame;
	}
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	public abstract void send();
    public abstract void close();

    public AbstractChatPanel(String username) {
        /**JFrame windows3 = new JFrame("im");
         windows3.setLayout(null);					//布局
         windows3.setBounds(300,100,width,height);	//设置窗口大小
         windows3.setResizable(false);				//该窗口不可调整大小
         windows3.setDefaultCloseOperation(0);		//屏蔽右上角关闭按钮
         */
    	this.username = username;
    	
    	Font font = new Font("宋体",Font.BOLD,20);
    	
    	
        setLayout(null);
        setSize(600, 700);            //设置JPanel的大小
        setLocation(0, 0);            //设置JPanel的位置
        setBackground(Color.WHITE);    //设置JPanel的背景色

        jta = new JTextArea();    //聊天记录展示区域
        jta.setEnabled(false);                //该区域只读
        jta.setBounds(0,0,600,400);
        jta.setBackground(Color.BLACK);
        jta.setFont(font);
        jta.setForeground(Color.red);
        add(jta);

        JPanel inputPanel = new JPanel();
        inputPanel.setSize(600, 210);            //设置JPanel的大小
        inputPanel.setLocation(0, 420);
        inputPanel.setBackground(Color.LIGHT_GRAY);    //设置JPanel的背景色
        inputPanel.setLayout(null);

        JTextArea input = new JTextArea();//文本框
        this.input = input;
        input.setColumns(60);
        input.setRows(3);
        input.setBounds(10, 10, 450, 100);
        input.setCaretColor(Color.BLACK);
        inputPanel.add(input);

        JButton sentButton = new JButton("发送");
        sentButton.setBounds(480, 16, 100, 40);
        inputPanel.add(sentButton);
        sentButton.addActionListener((e) -> {
			this.send();
		});

        JButton closeButton = new JButton("关闭");
        closeButton.setBounds(480, 65, 100, 40);
        inputPanel.add(closeButton);
        add(BorderLayout.CENTER, inputPanel);
        closeButton.addActionListener((e)->{
        	this.close();
        });
        
    }
    public void appendContent(String append) {
    	this.jta.append(append);
    }

}
