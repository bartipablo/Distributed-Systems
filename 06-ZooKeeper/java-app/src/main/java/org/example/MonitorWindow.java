package org.example;

import javax.swing.*;

/**
 * The MonitorWindow class provides a graphical user interface (GUI) to monitor
 * a ZooKeeper znode. It allows users to view the tree structure of the znode
 * and display the content in a text area.
 */
public class MonitorWindow {

    private Controller controller;
    private final JFrame frame;

    // text area ------------------------------
    private final int MAX_CONTENT_LENGTH = 1_000_000;
    private final StringBuilder content = new StringBuilder();
    private final JTextArea textArea;
    private final JScrollPane scrollPane;
    // text area ------------------------------

    private final JButton showTreeStructureButton;

    MonitorWindow() {
        frame = new JFrame("Distributed systems app");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(null);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(50, 50, 700, 300);
        frame.add(scrollPane);

        showTreeStructureButton = new JButton("Show tree structure");
        showTreeStructureButton.setBounds(300, 400, 200, 30);
        frame.add(showTreeStructureButton);

        showTreeStructureButton.addActionListener(e -> showTreeStructureButtonHandler());
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void showTreeStructureButtonHandler() {
        controller.displayTreeStructure();
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    /**
     * Appends new content to the text area. If the total content length exceeds
     * the maximum allowed length, the oldest content is removed to accommodate the new content.
     *
     * @param newContent The new content to be appended.
     */
    public void appendContent(String newContent) {
        if (content.length() + newContent.length() > MAX_CONTENT_LENGTH) {
            content.delete(0, content.length() + newContent.length() - MAX_CONTENT_LENGTH);
        }
        content.append(newContent);
        textArea.setText(content.toString());
    }
}
