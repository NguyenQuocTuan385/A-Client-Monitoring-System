package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ClientHandlerUI extends JFrame implements ActionListener {
    private Socket socket;
    private int port;
    private String clientUsername, dirCurrent;
    private JTextField portServer, username, jTextPath, jTextStatus;
    private DefaultTableModel dtmClient;
    private JTable jTableClient;
    private  JButton jButtonConnect;

    public ClientHandlerUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setTitle("Client Connect");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);

        Font fontHeaderAndFooter = new Font("Arial", Font.BOLD, 35);
        Font fontBody = new Font("Arial", Font.PLAIN, 20);

        JPanel jPanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 30,10));
        jPanelHeader.setPreferredSize(new Dimension(1000, 100));

        JPanel jPanelPort = new JPanel(new FlowLayout());
        JLabel jLabelPort = new JLabel("Port");
        jLabelPort.setFont(fontBody);
        jLabelPort.setForeground(new Color(32, 82, 149));

        portServer = new JTextField(20);
        jPanelPort.add(jLabelPort);
        jPanelPort.add(portServer);

        JPanel jPanelUsername = new JPanel(new FlowLayout());
        JLabel jLabelUsername = new JLabel("Username");
        jLabelUsername.setFont(fontBody);
        jLabelUsername.setForeground(new Color(32, 82, 149));

        username = new JTextField(20);
        jPanelUsername.add(jLabelUsername);
        jPanelUsername.add(username);

        jButtonConnect = new JButton("Kết nối");
        jButtonConnect.setFont(fontBody);
        jButtonConnect.setPreferredSize(new Dimension(150,40));
        jButtonConnect.addActionListener(this);
        jButtonConnect.setBackground(new Color(1, 119, 216));
        jButtonConnect.setForeground(Color.white);

        JPanel jPanelPath = new JPanel(new FlowLayout());
        JLabel jLabelPath = new JLabel("Path");
        jLabelPath.setFont(fontBody);
        jLabelPath.setForeground(new Color(32, 82, 149));

        jTextPath = new JTextField(20);
        jTextPath.setEditable(false);
        jPanelPath.add(jLabelPath);
        jPanelPath.add(jTextPath);

        JPanel jPanelStatus = new JPanel(new FlowLayout());
        JLabel jLabelStatus = new JLabel("Status");
        jLabelStatus.setFont(fontBody);
        jLabelStatus.setForeground(new Color(32, 82, 149));

        jTextStatus = new JTextField(20);
        jTextStatus.setEditable(false);
        jPanelStatus.add(jLabelStatus);
        jPanelStatus.add(jTextStatus);

        jPanelHeader.add(jPanelPort);
        jPanelHeader.add(jPanelUsername);
        jPanelHeader.add(jButtonConnect);
        jPanelHeader.add(jPanelPath);
        jPanelHeader.add(jPanelStatus);
        jPanelHeader.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));

        JLabel jLabelServer = new JLabel("Client UI", JLabel.CENTER);
        jLabelServer.setFont(fontHeaderAndFooter);
        jLabelServer.setForeground(new Color(20, 66, 114));
        JPanel jPanelHeaderFinal = new JPanel(new BorderLayout());
        jPanelHeaderFinal.add(jLabelServer,BorderLayout.PAGE_START);
        jPanelHeaderFinal.add(jPanelHeader, BorderLayout.CENTER);

        dtmClient = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        dtmClient.addColumn("Username");
        dtmClient.addColumn("Action");
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

        jTableClient.getColumnModel().getColumn(0).setPreferredWidth(25);
        jTableClient.getColumnModel().getColumn(1).setPreferredWidth(75);
        jTableClient.getColumnModel().getColumn(2).setPreferredWidth(300);

        JScrollPane sc = new JScrollPane(jTableClient, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel jPanelBody = new JPanel(new BorderLayout());
        jPanelBody.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        jPanelBody.add(sc);
        jPanelBody.setPreferredSize(new Dimension(1000,500));

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (jButtonConnect.getText().equals("Đóng kết nối")) {
                    try {
                        new ClientSend(socket, "Disconnect",clientUsername, dirCurrent);
                        socket.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Đóng kết nối không thành công!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        this.setLayout(new BorderLayout());
        this.add(jPanelHeaderFinal, BorderLayout.PAGE_START);
        this.add(jPanelBody, BorderLayout.CENTER);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String strAction = e.getActionCommand();
        if (strAction.equals("Kết nối")) { //Nếu client chọn  button kết nối tới server
            if (portServer.getText().equals("") || username.getText().equals("")) { //Nếu thông tin cần thiết bị rỗng
                JOptionPane.showMessageDialog(null, "Bạn chưa nhập port hoặc username!!!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                try {
                    port = Integer.parseInt(portServer.getText());
                    this.clientUsername = username.getText();
                    this.socket = new Socket("127.0.0.1", port);
                    dirCurrent = "C:\\";
                    jTextPath.setText(dirCurrent);
                    new ClientSend(socket, "Connect",clientUsername, dirCurrent); //Gửi yêu cầu kết nối tới server
                    jTextStatus.setText("Being monitored");
                    //Tạo thread client nhận gói tin
                    new Thread(new ClientReceive(socket,jButtonConnect, dirCurrent, clientUsername,jTextPath, dtmClient, jTextStatus)).start();
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, "Kết nối không thành công!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (strAction.equals("Đóng kết nối")) { //Kiểm tra nếu client click button đóng kết nối
            try {
                new ClientSend(socket, "Disconnect",clientUsername, dirCurrent); //Gửi yêu cầu đóng kết nối tới server
                dtmClient.setRowCount(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Đóng kết nối không thành công!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
