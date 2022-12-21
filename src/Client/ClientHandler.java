package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends JFrame implements ActionListener {
    private Socket socket;
    private int port;
    private String clientUsername;
    private String dirCurrent;
    private JTextField portServer;
    private JTextField username;
    private DefaultTableModel dtmClient;
    private JTable jTableClient;
    private  JButton jButtonConnect;

    public ClientHandler() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setTitle("Client Connect");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);

        Font fontHeaderAndFooter = new Font("Arial", Font.BOLD, 35);
        Font fontBody = new Font("Arial", Font.PLAIN, 20);

        JPanel jPanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 30,10));
        jPanelHeader.setPreferredSize(new Dimension(1000, 60));

        JPanel jPanelPort = new JPanel(new FlowLayout());
        JLabel jLabelPort = new JLabel("Port");
        jLabelPort.setFont(fontBody);
        portServer = new JTextField(20);
        jPanelPort.add(jLabelPort);
        jPanelPort.add(portServer);

        JPanel jPanelUsername = new JPanel(new FlowLayout());
        JLabel jLabelUsername = new JLabel("Username");
        jLabelUsername.setFont(fontBody);
        username = new JTextField(20);
        jPanelUsername.add(jLabelUsername);
        jPanelUsername.add(username);

        jButtonConnect = new JButton("Kết nối");
        jButtonConnect.setFont(fontBody);
        jButtonConnect.setPreferredSize(new Dimension(150,40));
        jButtonConnect.addActionListener(this);

        jPanelHeader.add(jPanelPort);
        jPanelHeader.add(jPanelUsername);
        jPanelHeader.add(jButtonConnect);
        jPanelHeader.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));

        dtmClient = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        dtmClient.addColumn("Username");
        dtmClient.addColumn("Action");
        dtmClient.addColumn("Ip Address");
        dtmClient.addColumn("Description");

        jTableClient = new JTable(dtmClient);
        jTableClient.setRowHeight(30);
        jTableClient.setFont(new Font("Arial", Font.PLAIN, 15));
        jTableClient.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        jTableClient.getTableHeader().setForeground(new Color(1, 119, 216));
        jTableClient.getTableHeader().setBackground(new Color(255, 255, 255));
        jTableClient.getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jTableClient.setShowGrid(false);
        jTableClient.setShowVerticalLines(true);
        jTableClient.setShowHorizontalLines(true);
        jTableClient.setGridColor(new Color(1, 119, 216));
        jTableClient.setSelectionBackground(new Color(1, 119, 216));
        jTableClient.setSelectionForeground(new Color(255, 255, 255));
        jTableClient.setRowSelectionAllowed(true);
        jTableClient.setColumnSelectionAllowed(false);
        jTableClient.setCellSelectionEnabled(false);
        jTableClient.setDragEnabled(false);
        jTableClient.setFillsViewportHeight(true);
        jTableClient.setPreferredScrollableViewportSize(new Dimension(500, 300));
        jTableClient.setFillsViewportHeight(true);
        jTableClient.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableClient.setRowSelectionAllowed(true);
        jTableClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane sc = new JScrollPane(jTableClient, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel jPanelBody = new JPanel(new BorderLayout());
        jPanelBody.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        jPanelBody.add(sc);
        jPanelBody.setPreferredSize(new Dimension(1000,500));

        this.setLayout(new BorderLayout());
        this.add(jPanelHeader, BorderLayout.PAGE_START);
        this.add(jPanelBody, BorderLayout.CENTER);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String strAction = e.getActionCommand();
        if (strAction.equals("Kết nối")) {
            if (portServer.getText().equals("") || username.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Bạn chưa nhập port hoặc username!!!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                try {
                    port = Integer.parseInt(portServer.getText());
                    this.clientUsername = username.getText();
                    this.socket = new Socket("127.0.0.1", port);
                    dirCurrent = "C:";

                    new ClientSend(socket, "Connected", "2",clientUsername, dirCurrent);
                    new Thread(new ClientReceive(socket,jButtonConnect)).start();
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, "Kết nối không thành công!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (strAction.equals("Đóng kết nối")) {
            try {
                new ClientSend(socket, "Disconnect", "3",clientUsername, dirCurrent);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Đóng kết nối không thành công!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new ClientHandler();
    }
}
