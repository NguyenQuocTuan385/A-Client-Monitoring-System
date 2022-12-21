package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerUI extends JFrame implements ActionListener {
    private JTextField portServer;

    public ServerUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setTitle("Start Server");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 300);
        this.setLocationRelativeTo(null);

        Font fontHeaderAndFooter = new Font("Arial", Font.BOLD, 35);
        Font fontBody = new Font("Arial", Font.PLAIN, 20);

        JPanel jPanelHeader = new JPanel();
        JLabel labelHeader = new JLabel("Start Server");
        labelHeader.setFont(fontHeaderAndFooter);
        labelHeader.setForeground(new Color(1, 119, 216));

        jPanelHeader.add(labelHeader);
        jPanelHeader.setPreferredSize(new Dimension(600, 100));

        JPanel jPanelBody = new JPanel(new GridLayout(1,2));
        JLabel jLabelPort = new JLabel("Port");
        jLabelPort.setFont(fontHeaderAndFooter);
        portServer = new JTextField(150);

        jPanelBody.add(jLabelPort);
        jPanelBody.add(portServer);
        jPanelBody.setPreferredSize(new Dimension(400,100));

        JPanel jPanelBodyLeft = new JPanel();
        jPanelBodyLeft.setPreferredSize(new Dimension(100,100));

        JPanel jPanelBodyRight = new JPanel();
        jPanelBodyRight.setPreferredSize(new Dimension(100,100));

        JButton jButtonStart = new JButton("Start");
        jButtonStart.setFont(fontBody);
        jButtonStart.setPreferredSize(new Dimension(200,50));
        jButtonStart.addActionListener(this);

        JPanel jPanelBot = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,20));
        jPanelBot.setPreferredSize(new Dimension(600, 100));
        jPanelBot.add(jButtonStart);

        this.setLayout(new BorderLayout());
        this.add(jPanelHeader, BorderLayout.PAGE_START);
        this.add(jPanelBody, BorderLayout.CENTER);
        this.add(jPanelBodyLeft, BorderLayout.LINE_START);
        this.add(jPanelBodyRight, BorderLayout.LINE_END);
        this.add(jPanelBot, BorderLayout.PAGE_END);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new ServerUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String strAction = e.getActionCommand();
        if (strAction.equals("Start")) {
            this.dispose();
        }
    }
}
