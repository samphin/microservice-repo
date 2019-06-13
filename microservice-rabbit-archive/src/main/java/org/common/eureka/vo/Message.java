package org.common.eureka.vo;

import com.rabbitmq.client.MessageProperties;

/**
 * 消息对象
 * @author samphin
 * @date 2019-1-10 16:28:54
 */
public class Message{

	private final MessageProperties messageProperties;

    private final byte[] body;

    public Message(byte[] body, MessageProperties messageProperties) {
        this.body = body;
        this.messageProperties = messageProperties;
    }

    public byte[] getBody() {
        return this.body;
    }

    public MessageProperties getMessageProperties() {
        return this.messageProperties;
    }
}
