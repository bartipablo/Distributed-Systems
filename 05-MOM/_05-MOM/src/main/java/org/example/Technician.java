package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Technician {

    public static void main(String[] args) throws Exception {
        System.out.println("Running Technician...");

        Technician technician = new Technician(
                MedicalExamination.fromString(args[0]),
                MedicalExamination.fromString(args[1])
        );

        technician.start();
    }

    private final Config config = new Config();

    private final MedicalExamination medicalExaminationOne;

    private final MedicalExamination medicalExaminationTwo;

    Technician(MedicalExamination medicalExaminationOne, MedicalExamination medicalExaminationTwo) {
        this.medicalExaminationOne = medicalExaminationOne;
        this.medicalExaminationTwo = medicalExaminationTwo;
    }

    private void start() throws Exception {
        // Create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // Create a connection
        try (Connection connection = factory.newConnection()) {

            config.listenAdminCommunicates(connection);

            initMedicalCommunication(connection);

            while (true) {}
        }
    }

    void doPhysicalExamination(MedicalExamination examination) throws InterruptedException {
        // Simulate the physical examination
        Thread.sleep(examination.examinationTime());
    }

    private void initMedicalCommunication(Connection connection) throws Exception {
        // Create a medical channel and declare a medical queue
        final Channel medicalChannel = connection.createChannel();

        // Declare the medical exchange
        medicalChannel.exchangeDeclare(Config.MEDICAL_EXCHANGE_NAME, "direct");

        int prefetchCount = 1;
        medicalChannel.basicQos(prefetchCount);

        // Declare the medical queues for the specializations
        medicalChannel.queueDeclare(medicalExaminationOne.toString(), true, false, false, null);
        medicalChannel.queueBind(medicalExaminationOne.toString(), Config.MEDICAL_EXCHANGE_NAME, medicalExaminationOne.toString());

        medicalChannel.queueDeclare(medicalExaminationTwo.toString(), true, false, false, null);
        medicalChannel.queueBind(medicalExaminationTwo.toString(), Config.MEDICAL_EXCHANGE_NAME, medicalExaminationTwo.toString());

        DeliverCallback medicalReceiver = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String[] parts = message.split(":::");

            MedicalExamination examination = null;

            // Validate the message
            try {
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Invalid message format.");
                }
                examination = MedicalExamination.fromString(parts[2]);
                if (!examination.equals(medicalExaminationOne) && !examination.equals(medicalExaminationTwo)) {
                    throw new IllegalArgumentException("Invalid examination type.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e);
                return;
            }

            System.out.println("Doctor: 'new examination: " + parts[1] + " " + parts[2] + "'");

            try {
                doPhysicalExamination(examination);
            } catch (InterruptedException e) {
                System.out.println("Error in the physical examination.");
            } finally {
                System.out.println("Done.");
                String feedBackMessage = parts[1] + " " + parts[2] + " done";

                medicalChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                medicalChannel.basicPublish(Config.MEDICAL_EXCHANGE_NAME, parts[0], null, feedBackMessage.getBytes("UTF-8"));
                medicalChannel.basicPublish(Config.ADMIN_EXCHANGE_NAME, Config.ADMIN_ROUTING_KEY, null, feedBackMessage.getBytes("UTF-8"));
            }
        };

        // Consume from the declared queues
        medicalChannel.basicConsume(medicalExaminationOne.toString(), false, medicalReceiver, consumerTag -> {});
        medicalChannel.basicConsume(medicalExaminationTwo.toString(), false, medicalReceiver, consumerTag -> {});

    }
}
