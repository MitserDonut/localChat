package com.hfut.service;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public interface Listenable {
    default void listen() {
        Selector selector = getSelector();
        while (true) {
        	try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
                int count = selector.select(200);
                //唤醒selector
                int a = count + 1;
                if (count > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    selectionKeys.forEach(this::handle);
                    selectionKeys.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Selector getSelector();

    void handle(SelectionKey key);
}
