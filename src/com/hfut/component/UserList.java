package com.hfut.component;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hfut.gui.PrivateChatGUI;
import com.hfut.service.client.ResponseConsumer;


public class UserList extends JPanel {

    private DefaultListModel<String> dlm;
    private String username;

    public UserList(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        dlm = new DefaultListModel<>();
        JList jList = new JList<>(dlm);
        JScrollPane jScrollPane = new JScrollPane(jList);
        JLabel title = new JLabel("用户列表：");
        jList.addMouseListener(new DoubleClickEvent());
        setSize(180, 500);
        add(title, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);

    }

    public void removeAll() {
        dlm.removeAllElements();
    }

    public void addUser(String name) {
        dlm.add(dlm.getSize(), name);
    }

    public void removeUser(String name) {
        dlm.removeElement(name);
    }

    public void updateUser(String users) {
        String[] userList = users.split(",");
        dlm.clear();
        for (String s : userList) {
            addUser(s);
        }
    }

    private class DoubleClickEvent extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            JList theList = (JList) e.getSource();
            if (e.getClickCount() == 2) {
                int index = theList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    System.out.println(theList.getModel().getElementAt(index));
                    String dest = (String) theList.getModel().getElementAt(index);
                    PrivateChatGUI privateRoom = new PrivateChatGUI(username, dest);
                    privateRoom.getPrivatePanel().setFrame(privateRoom);
                    ResponseConsumer.privateChat.put(dest, privateRoom);
                }
            }
        }
    }
}
