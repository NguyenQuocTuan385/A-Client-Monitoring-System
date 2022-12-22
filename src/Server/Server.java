package Server;

import Client.ClientHandler;

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

public class Server extends JFrame implements Runnable, ActionListener {
    private int port;
    private ServerSocket serverSocket;
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

    public Server() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setTitle("Client Connect");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400, 600);
        this.setLocationRelativeTo(null);

        Font fontHeaderAndFooter = new Font("Arial", Font.BOLD, 35);
        Font fontBody = new Font("Arial", Font.PLAIN, 20);

        JPanel jPanelHeaderLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 30,10));
        jPanelHeaderLeft.setPreferredSize(new Dimension(600, 60));

        JPanel jPanelPort = new JPanel(new FlowLayout());
        JLabel jLabelPort = new JLabel("Port");
        jLabelPort.setFont(fontBody);
        portServer = new JTextField(20);
        jPanelPort.add(jLabelPort);
        jPanelPort.add(portServer);

        jButtonStart = new JButton("Start");
        jButtonStart.setFont(fontBody);
        jButtonStart.setPreferredSize(new Dimension(150,40));
        jButtonStart.addActionListener(this);

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
        jPanelHeaderRight.add(jLabelClient);

        jPanelHeaderRight.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));

        dtmListClient = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        dtmListClient.addColumn("ID");
        dtmListClient.addColumn("Username");
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

        jTableListClient.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTableListClient.getColumnModel().getColumn(1).setPreferredWidth(100);
        jTableListClient.getColumnModel().getColumn(2).setPreferredWidth(250);

        JScrollPane sc1 = new JScrollPane(jTableListClient, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JButton jButtonSelectFolder = new JButton("Chọn thư mục");
        jButtonSelectFolder.addActionListener(this);

        JPanel jPanelBody1= new JPanel(new BorderLayout());
        jPanelBody1.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        jPanelBody1.add(sc1, BorderLayout.CENTER);
        jPanelBody1.add(jButtonSelectFolder, BorderLayout.PAGE_END);
        jPanelBody1.setPreferredSize(new Dimension(400,500));


        JPanel jPanelBodyRight = new JPanel(new BorderLayout());
        jPanelBodyRight.add(jPanelHeaderRight, BorderLayout.PAGE_START);
        jPanelBodyRight.add(jPanelBody1, BorderLayout.CENTER);
        jPanelBodyRight.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        this.setLayout(new BorderLayout());
        this.add(jPanelBodyLeft, BorderLayout.LINE_START);
        this.add(jPanelBodyRight, BorderLayout.LINE_END);
        this.setVisible(true);
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
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                s = serverSocket.accept();
                listClient.add(s);
                new Thread(new ServerReceive(s, listClient, listNameClient, mapClient, dtmListClient, mapPathClient)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String strAction = e.getActionCommand();
        if (strAction.equals("Start")) {
            if (portServer.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Bạn chưa nhập port!!!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                this.port = Integer.parseInt(portServer.getText());
                new Thread(this).start();
            }
        }
        else if (strAction.equals("Chọn thư mục")) {
            if (!jTableListClient.getSelectionModel().isSelectionEmpty()) {

                DefaultTableModel model = (DefaultTableModel) jTableListClient.getModel();

                int selectedRowIndex = jTableListClient.getSelectedRow();
                String username = model.getValueAt(selectedRowIndex, 1).toString();
                String path = model.getValueAt(selectedRowIndex, 2).toString();

                chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File(path));
                chooser.setDialogTitle("Chọn thư mục giám sát");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                //
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    String pathChange = String.valueOf(chooser.getSelectedFile());
                    dtmListClient.setValueAt(pathChange, selectedRowIndex, 2);
                    Socket socket = mapClient.get(username);
                    mapPathClient.put(username, pathChange);

                    try {
                        new ServerSend(socket,"Change Folder Monitoring", "5", "server", pathChange);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn client để đổi thư mục giám sát"
                        , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
