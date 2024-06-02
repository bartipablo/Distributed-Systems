package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

public class Config {

    public static final String MEDICAL_EXCHANGE_NAME = "medical-ex1";

    public static final String ADMIN_EXCHANGE_NAME = "admin-ex1";

    public static final String CLIENT_ROUTING_KEY = "client";

    public static final String ADMIN_ROUTING_KEY = "admin";

    public static final String ADMIN_QUEUE_NAME = "admin";

    public void listenAdminCommunicates(Connection connection) throws Exception {
        // Create an admin channel
        final Channel adminChannel = connection.createChannel();

        // Declare the admin exchange
        adminChannel.exchangeDeclare(Config.ADMIN_EXCHANGE_NAME, "topic");
        String queueName = adminChannel.queueDeclare().getQueue();
        adminChannel.queueBind(queueName, Config.ADMIN_EXCHANGE_NAME, Config.CLIENT_ROUTING_KEY);

        DeliverCallback adminReceiver = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Admin: '" + message + "'");
        };
        adminChannel.basicConsume(queueName, true, adminReceiver, consumerTag -> {});
    }
}
