package com.hfut.bean;

import java.io.Serializable;
import java.util.Date;

public class Header implements Serializable {
    private Date date;
    //1-是否私聊 2-连接 4-断开 8-聊天内容
    private Integer type;
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

	@Override
	public String toString() {
		return "Header [date=" + date + ", type=" + type + "]";
	}
    
}
