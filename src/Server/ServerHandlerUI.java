package Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class ServerHandlerUI extends JFrame implements Runnable, ActionListener {
    private int port;
    private ServerSocket serverSocket = null;
    private ArrayList<Socket> listClient;
    private ArrayList<String> listNameClient;
    private TreeMap<String, Socket> mapClient;
    private TreeMap<String, String> mapPathClient;
    private JTextField portServer;
    private DefaultTableModel dtmListClient;
    private JTable jTableListClient;
    private DefaultTableModel dtmListActionClient;
    private JTable jTableListActionClient;
    private  JButton jButtonStart;
    private JFileChooser chooser;
    private boolean isStart;

    public ServerHandlerUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setTitle("Server");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400, 700);
        this.setLocationRelativeTo(null);

        Font fontHeaderAndFooter = new Font("Arial", Font.BOLD, 35);
        Font fontBody = new Font("Arial", Font.PLAIN, 20);

        JPanel jPanelHeaderLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 30,10));
        jPanelHeaderLeft.setPreferredSize(new Dimension(600, 60));

        JPanel jPanelPort = new JPanel(new FlowLayout());
        JLabel jLabelPort = new JLabel("Port");
        jLabelPort.setFont(fontBody);
        jLabelPort.setForeground(new Color(32, 82, 149));
        portServer = new JTextField(20);
        jPanelPort.add(jLabelPort);
        jPanelPort.add(portServer);

        jButtonStart = new JButton("Start");
        jButtonStart.setFont(fontBody);
        jButtonStart.setPreferredSize(new Dimension(150,40));
        jButtonStart.addActionListener(this);
        jButtonStart.setBackground(new Color(1, 119, 216));
        jButtonStart.setForeground(Color.white);

        JLabel jLabelServer = new JLabel("Server UI", JLabel.CENTER);
        jLabelServer.setFont(fontHeaderAndFooter);
        jLabelServer.setForeground(new Color(20, 66, 114));

        jPanelHeaderLeft.add(jPanelPort);
        jPanelHeaderLeft.add(jButtonStart);
        jPanelHeaderLeft.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));

        dtmListActionClient = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        dtmListActionClient.addColumn("Username");
        dtmListActionClient.addColumn("Action");
        dtmListActionClient.addColumn("Description");

        jTableListActionClient = new JTable(dtmListActionClient);
        jTableListActionClient.setRowHeight(30);
        jTableListActionClient.setFont(new Font("Arial", Font.PLAIN, 15));
        jTableListActionClient.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        jTableListActionClient.getTableHeader().setForeground(new Color(1, 119, 216));
        jTableListActionClient.getTableHeader().setBackground(new Color(255, 255, 255));
        jTableListActionClient.getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jTableListActionClient.setShowGrid(false);
        jTableListActionClient.setShowVerticalLines(true);
        jTableListActionClient.setShowHorizontalLines(true);
        jTableListActionClient.setGridColor(new Color(1, 119, 216));
        jTableListActionClient.setSelectionBackground(new Color(1, 119, 216));
        jTableListActionClient.setSelectionForeground(new Color(255, 255, 255));
        jTableListActionClient.setRowSelectionAllowed(true);
        jTableListActionClient.setColumnSelectionAllowed(false);
        jTableListActionClient.setCellSelectionEnabled(false);
        jTableListActionClient.setDragEnabled(false);
        jTableListActionClient.setFillsViewportHeight(true);
        jTableListActionClient.setPreferredScrollableViewportSize(new Dimension(500, 300));
        jTableListActionClient.setFillsViewportHeight(true);
        jTableListActionClient.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableListActionClient.setRowSelectionAllowed(true);
        jTableListActionClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jTableListActionClient.getColumnModel().getColumn(0).setPreferredWidth(150);
        jTableListActionClient.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTableListActionClient.getColumnModel().getColumn(2).setPreferredWidth(450);

        JScrollPane sc = new JScrollPane(jTableListActionClient, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel jPanelBody = new JPanel(new BorderLayout());
        jPanelBody.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        jPanelBody.add(sc);
        jPanelBody.setPreferredSize(new Dimension(800,500));

        JPanel jPanelBodyLeft = new JPanel(new BorderLayout());
        jPanelBodyLeft.add(jPanelHeaderLeft, BorderLayout.PAGE_START);
        jPanelBodyLeft.add(jPanelBody, BorderLayout.CENTER);
        jPanelBodyLeft.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel jPanelHeaderRight = new JPanel();
        jPanelHeaderRight.setPreferredSize(new Dimension(600, 60));

        JLabel jLabelClient = new JLabel("List Client");
        jLabelClient.setFont(fontBody);
        jLabelClient.setForeground(new Color(32, 82, 149));
        jPanelHeaderRight.add(jLabelClient);

        jPanelHeaderRight.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));

        dtmListClient = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        dtmListClient.addColumn("Username");
        dtmListClient.addColumn("Status");
        dtmListClient.addColumn("Path");

        jTableListClient = new JTable(dtmListClient);
        jTableListClient.setRowHeight(30);
        jTableListClient.setFont(new Font("Arial", Font.PLAIN, 15));
        jTableListClient.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        jTableListClient.getTableHeader().setForeground(new Color(1, 119, 216));
        jTableListClient.getTableHeader().setBackground(new Color(255, 255, 255));
        jTableListClient.getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jTableListClient.setShowGrid(false);
        jTableListClient.setShowVerticalLines(true);
        jTableListClient.setShowHorizontalLines(true);
        jTableListClient.setGridColor(new Color(1, 119, 216));
        jTableListClient.setSelectionBackground(new Color(1, 119, 216));
        jTableListClient.setSelectionForeground(new Color(255, 255, 255));
        jTableListClient.setRowSelectionAllowed(true);
        jTableListClient.setColumnSelectionAllowed(false);
        jTableListClient.setCellSelectionEnabled(false);
        jTableListClient.setDragEnabled(false);
        jTableListClient.setFillsViewportHeight(true);
        jTableListClient.setPreferredScrollableViewportSize(new Dimension(500, 300));
        jTableListClient.setFillsViewportHeight(true);
        jTableListClient.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableListClient.setRowSelectionAllowed(true);
        jTableListClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jTableListClient.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTableListClient.getColumnModel().getColumn(1).setPreferredWidth(50);
        jTableListClient.getColumnModel().getColumn(2).setPreferredWidth(250);

        JScrollPane sc1 = new JScrollPane(jTableListClient, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JButton jButtonSelectFolder = new JButton("Chọn thư mục");
        jButtonSelectFolder.addActionListener(this);
        jButtonSelectFolder.setBackground(new Color(1, 119, 216));
        jButtonSelectFolder.setForeground(Color.white);

        JButton jButtonStartMonitor = new JButton("Start monitor");
        jButtonStartMonitor.addActionListener(this);
        jButtonStartMonitor.setBackground(new Color(1, 119, 216));
        jButtonStartMonitor.setForeground(Color.white);

        JButton jButtonStopMonitor = new JButton("Stop monitor");
        jButtonStopMonitor.addActionListener(this);
        jButtonStopMonitor.setBackground(new Color(1, 119, 216));
        jButtonStopMonitor.setForeground(Color.white);

        JButton jButtonDisconnect = new JButton("Disconnect");
        jButtonDisconnect.addActionListener(this);
        jButtonDisconnect.setBackground(new Color(1, 119, 216));
        jButtonDisconnect.setForeground(Color.white);

        JPanel jPanelBody1Bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        jPanelBody1Bot.add(jButtonSelectFolder);
        jPanelBody1Bot.add(jButtonStartMonitor);
        jPanelBody1Bot.add(jButtonStopMonitor);
        jPanelBody1Bot.add(jButtonDisconnect);

        JPanel jPanelBody1= new JPanel(new BorderLayout());
        jPanelBody1.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        jPanelBody1.add(sc1, BorderLayout.CENTER);
        jPanelBody1.add(jPanelBody1Bot, BorderLayout.PAGE_END);
        jPanelBody1.setPreferredSize(new Dimension(400,500));


        JPanel jPanelBodyRight = new JPanel(new BorderLayout());
        jPanelBodyRight.add(jPanelHeaderRight, BorderLayout.PAGE_START);
        jPanelBodyRight.add(jPanelBody1, BorderLayout.CENTER);
        jPanelBodyRight.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        this.setLayout(new BorderLayout());
        this.add(jLabelServer, BorderLayout.PAGE_START);
        this.add(jPanelBodyLeft, BorderLayout.LINE_START);
        this.add(jPanelBodyRight, BorderLayout.LINE_END);
        this.setVisible(true);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (jButtonStart.getText().equals("Stop")) {
                    try {
                        new ServerSend(listClient, "Disconnect success", "Server stop all exit window");
                        isStart = false;
                        jButtonStart.setText("Start");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Đóng kết nối không thành công!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    @Override
    public void run() {
        listClient =  new ArrayList<>();
        listNameClient = new ArrayList<>();
        mapClient = new TreeMap<>();
        mapPathClient = new TreeMap<>();
        Socket s = new Socket();
        System.out.println("Bắt đầu khởi động máy chủ");
        try {
            if (serverSocket == null) {
                JOptionPane.showMessageDialog(null, "Start server thành công!!!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                serverSocket = new ServerSocket(port);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Start server thất bại!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
        }

        while (isStart) {
            try {
                s = serverSocket.accept();
                if (isStart == false) {
                    new ServerSend(s, "Disconnect success", "Server stop");
                    break;
                }
                listClient.add(s);
                new Thread(new ServerReceive(s, listClient, listNameClient, mapClient, dtmListClient, mapPathClient,dtmListActionClient, isStart)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serverSocket != null ){ //Đóng kết nối server
            try {
                serverSocket.close();
                serverSocket = null;
                System.out.println("Stop máy chủ");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Stop server thất bại!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String strAction = e.getActionCommand();
        if (strAction.equals("Start")) { //Nếu người dùng click button start server
            if (portServer.getText().equals("")) { //Kiểm tra chuỗi có bằng rỗng
                JOptionPane.showMessageDialog(null, "Bạn chưa nhập port!!!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                this.port = Integer.parseInt(portServer.getText());
                isStart = true;
                jButtonStart.setText("Stop");
                new Thread(this).start();
            }
        }
        else if (strAction.equals("Chọn thư mục")) { //Nếu người dùng click button thay đổi thư mục giám sát client
            if (!jTableListClient.getSelectionModel().isSelectionEmpty()) {

                DefaultTableModel model = (DefaultTableModel) jTableListClient.getModel();

                int selectedRowIndex = jTableListClient.getSelectedRow();
                String username = model.getValueAt(selectedRowIndex, 0).toString();
                String path = model.getValueAt(selectedRowIndex, 2).toString();

                chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File(path));
                chooser.setDialogTitle("Chọn thư mục giám sát");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                //
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { //Server chốt được path giám sát
                    String pathChange = String.valueOf(chooser.getSelectedFile());
                    if (!pathChange.equals(path)) {
                        dtmListClient.setValueAt(pathChange, selectedRowIndex, 2);
                        Socket socket = mapClient.get(username);
                        mapPathClient.put(username, pathChange);

                        try {
                            new ServerSend(socket,"Change Folder Monitoring", "server", pathChange);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn client để đổi thư mục giám sát"
                        , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (strAction.equals("Start monitor")) {  //Nếu người dùng click button bắt đầu giám sát client
            if (!jTableListClient.getSelectionModel().isSelectionEmpty()) {

                DefaultTableModel model = (DefaultTableModel) jTableListClient.getModel();

                int selectedRowIndex = jTableListClient.getSelectedRow();
                String username = model.getValueAt(selectedRowIndex, 0).toString();
                String status = model.getValueAt(selectedRowIndex, 1).toString();

                if (status.equals("Stop")) {
                    Socket socket = mapClient.get(username);
                    status = "Start";
                    try {
                        new ServerSend(socket, "Start monitor", username);
                        dtmListClient.setValueAt(status, selectedRowIndex, 1);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Client trước đó đã đang được giám sát!!!"
                            , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn client để bắt đầu giám sát"
                        , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (strAction.equals("Stop monitor")) {  //Nếu người dùng click button dừng giám sát client
            if (!jTableListClient.getSelectionModel().isSelectionEmpty()) {

                DefaultTableModel model = (DefaultTableModel) jTableListClient.getModel();

                int selectedRowIndex = jTableListClient.getSelectedRow();
                String username = model.getValueAt(selectedRowIndex, 0).toString();
                String status = model.getValueAt(selectedRowIndex, 1).toString();

                if (status.equals("Start")) {
                    Socket socket = mapClient.get(username);
                    status = "Stop";
                    try {
                        new ServerSend(socket, "Stop monitor", username);
                        dtmListClient.setValueAt(status, selectedRowIndex, 1);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Client trước đó đã đang dừng giám sát!!!"
                            , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn client để dừng giám sát"
                        , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (strAction.equals("Disconnect")) {
            if (!jTableListClient.getSelectionModel().isSelectionEmpty()) {
                DefaultTableModel model = (DefaultTableModel) jTableListClient.getModel();

                int selectedRowIndex = jTableListClient.getSelectedRow();
                String username = model.getValueAt(selectedRowIndex, 0).toString();

                Socket socket = mapClient.get(username);
                try {
                    new ServerSend(socket, "Disconnect success", "Server disconnect");
                    dtmListClient.removeRow(listNameClient.indexOf(username));
                    listNameClient.remove(username);
                    mapClient.remove(username);
                    mapPathClient.remove(username);
                    listClient.remove(socket);
                    System.out.println("Username:" + username + " was disconnected");
                    JOptionPane.showMessageDialog(this, "Ngắt kết nối với client thành công"
                            , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn client để ngắt kết nối"
                        , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (strAction.equals("Stop")) {
            try {
                new ServerSend(listClient, "Disconnect success", "Server stop all");
                isStart = false;
                jButtonStart.setText("Start");
                JOptionPane.showMessageDialog(null, "Stop server thành công!!!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
