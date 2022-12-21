package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI extends JFrame implements ActionListener {
    private JTextField portServer;
    private JTextField username;
    ClientHandler clientHandler;
    public ClientUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setTitle("Client Connect");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 300);
        this.setLocationRelativeTo(null);

        Font fontHeaderAndFooter = new Font("Arial", Font.BOLD, 35);
        Font fontBody = new Font("Arial", Font.PLAIN, 20);

        JPanel jPanelHeader = new JPanel();
        JLabel labelHeader = new JLabel("Client Connect");
        labelHeader.setFont(fontHeaderAndFooter);
        labelHeader.setForeground(new Color(1, 119, 216));

        jPanelHeader.add(labelHeader);
        jPanelHeader.setPreferredSize(new Dimension(600, 80));

        JPanel jPanelBody = new JPanel(new GridLayout(2,2,10,10));
        JLabel jLabelPort = new JLabel("Port");
        jLabelPort.setFont(fontHeaderAndFooter);
        portServer = new JTextField(50);
        JLabel jLabelUsername = new JLabel("Username");
        jLabelUsername.setFont(fontHeaderAndFooter);
        username = new JTextField(50);

        jPanelBody.add(jLabelPort);
        jPanelBody.add(portServer);
        jPanelBody.add(jLabelUsername);
        jPanelBody.add(username);
        jPanelBody.setPreferredSize(new Dimension(500,150));

        JPanel jPanelBodyLeft = new JPanel();
        jPanelBodyLeft.setPreferredSize(new Dimension(50,150));

        JPanel jPanelBodyRight = new JPanel();
        jPanelBodyRight.setPreferredSize(new Dimension(50,150));

        JButton jButtonConnect = new JButton("Kết nối");
        jButtonConnect.setFont(fontBody);
        jButtonConnect.setPreferredSize(new Dimension(200,30));
        jButtonConnect.addActionListener(this);

        JPanel jPanelBot = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,20));
        jPanelBot.setPreferredSize(new Dimension(600, 70));
        jPanelBot.add(jButtonConnect);

        this.setLayout(new BorderLayout());
        this.add(jPanelHeader, BorderLayout.PAGE_START);
        this.add(jPanelBody, BorderLayout.CENTER);
        this.add(jPanelBodyLeft, BorderLayout.LINE_START);
        this.add(jPanelBodyRight, BorderLayout.LINE_END);
        this.add(jPanelBot, BorderLayout.PAGE_END);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String strAction = e.getActionCommand();
        if (strAction.equals("Kết nối")) {

        }
    }
}
