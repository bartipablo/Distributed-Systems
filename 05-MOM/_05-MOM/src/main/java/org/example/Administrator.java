package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Scanner;


public class Administrator {

    public static void main(String[] args) throws Exception {
        System.out.println("Running Administrator...");

        Administrator administrator = new Administrator();
        administrator.start();
    }

    private void start() throws Exception  {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
             Scanner scanner = new Scanner(System.in)) {

            channel.exchangeDeclare(Config.ADMIN_EXCHANGE_NAME, "topic");
            channel.queueDeclare(Config.ADMIN_QUEUE_NAME, true, false, false, null);
            channel.queueBind(Config.ADMIN_QUEUE_NAME, Config.ADMIN_EXCHANGE_NAME, Config.ADMIN_ROUTING_KEY);

            DeliverCallback adminCommunicatesReceiver = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(Config.ADMIN_QUEUE_NAME, false, adminCommunicatesReceiver, consumerTag -> {});

            System.out.println("Enter the message: ");
            while (true) {
                String message = scanner.nextLine();

                if (message.equals("/exit")) {
                    break;
                }

                channel.basicPublish(Config.ADMIN_EXCHANGE_NAME, Config.CLIENT_ROUTING_KEY, null, message.getBytes("UTF-8"));
            }
        }
    }
}
