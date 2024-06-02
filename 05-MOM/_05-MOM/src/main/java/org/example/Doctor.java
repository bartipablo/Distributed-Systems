package org.example;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Scanner;

public class Doctor {

    public static void main(String[] args) throws Exception {
        System.out.println("Running Doctor...");

        Doctor doctor = new Doctor();

        doctor.start();
    }

    private final Config config = new Config();

    private void start() throws Exception {
        // Create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // Create a connection
        try (Connection connection = factory.newConnection();
            Scanner scanner = new Scanner(System.in)) {

            config.listenAdminCommunicates(connection);

            initMedicalCommunication(connection, scanner);
        }
    }

    private void initMedicalCommunication(Connection connection, Scanner scanner) throws Exception {
        // Create an admin channel
        final Channel medicalChannel = connection.createChannel();

        // Declare the admin exchange
        medicalChannel.exchangeDeclare(Config.MEDICAL_EXCHANGE_NAME, "direct");
        String queueID = medicalChannel.queueDeclare().getQueue();

        medicalChannel.queueBind(queueID, Config.MEDICAL_EXCHANGE_NAME, queueID);

        DeliverCallback medicalConfirmationReceiver = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Technician: '" + message + "'");
        };
        // Consume from the declared queue
        medicalChannel.basicConsume(queueID, true, medicalConfirmationReceiver, consumerTag -> {});

        System.out.println("Refer the patient for a medical examination: `{patient_name} {Hip|Knee|Elbow}`");
        while (true) {
            // Read the message from the console
            String readLine = scanner.nextLine();

            if (readLine.equals("/exit")) {
                break;
            }

            try {
                String[] parts = readLine.split(" ");
                if (parts.length != 2) {
                    System.out.println("Invalid message format. Please try again.");
                    continue;
                }

                String patientName = parts[0];
                MedicalExamination examination = MedicalExamination.fromString(parts[1]);
                String message = queueID + ":::" + patientName + ":::" + examination.toString();

                medicalChannel.basicPublish(Config.MEDICAL_EXCHANGE_NAME, examination.toString(), null, message.getBytes("UTF-8"));
                medicalChannel.basicPublish(Config.ADMIN_EXCHANGE_NAME, Config.ADMIN_ROUTING_KEY, null, message.getBytes("UTF-8"));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid examination refer format. Please try again.");
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
            }
        }
    }
}
