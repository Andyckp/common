package com.ac.common.dispatch2;

import com.ac.common.Message;

public class SingleMessageEvent {
    private Message<?, ?> message;

    public SingleMessageEvent() {
    }

    public void setMessage(Message<?, ?> message) {
        this.message = message;
    }

    public Message<?, ?> getMessage() {
        return message;
    }
}
