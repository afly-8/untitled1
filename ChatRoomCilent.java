package xl.chatroom;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatRoomCilent {

    private JFrame jf;
    private JTextArea jta;
    private JTextField jtf;
    private JButton jb;
    private JLabel lable;
    private Socket s;
    private BufferedReader br;
    private PrintWriter pw;
    private String name;
    public  ChatRoomCilent() {
        jf=new JFrame("聊天室1.0");
        do {
            String ip=JOptionPane.showInputDialog(jf,"请输入服务器地址");
            int port=Integer.parseInt(JOptionPane.showInputDialog(jf,"请输入端口号"));
            try {
                s=new Socket(ip,port);
                pw=new PrintWriter(s.getOutputStream());
                br=new BufferedReader(new InputStreamReader(s.getInputStream()));

            } catch (Exception e) {
                // TODO: handle exception
                JOptionPane.showMessageDialog(jf, "建立连接失败，请重新输入地址和端口号！");

            }
        }
        while(s==null);
        jta=new JTextArea(20,40);
        jta.setFont(new Font("宋体",Font.BOLD,24));
        jta.setEditable(false);
        jtf=new JTextField(28);
        jb=new JButton("发送");
        name=JOptionPane.showInputDialog(jf,"请输入您的名称：");
        lable=new JLabel(name+":");
        init();
        addEventHandler();
        showMe();
    }
    private void init() {
        // TODO Auto-generated method stub
        JScrollPane jsp=new JScrollPane(jta);
        jf.add(jsp,BorderLayout.CENTER);
        JPanel panel=new JPanel();
        panel.add(lable);
        panel.add(jtf);
        panel.add(jb);
        jf.add(panel, BorderLayout.SOUTH);
    }
    public void showMe() {
        jf.pack();
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }
    private void addEventHandler() {
        // TODO Auto-generated method stub
        jb.addActionListener((ActionListener) this);
        jtf.addActionListener((ActionListener) this);
        jf.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int op=JOptionPane.showConfirmDialog(jf, "确定退出聊天室吗？");
                if(op==JOptionPane.YES_NO_OPTION) {

                    pw.println("%EXIT%"+name);
                    pw.flush();
                    try {

                    } catch (Exception e2) {
                        // TODO: handle exception
                        e2.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });

    }
    public void actionPerformed(){
        if (jtf.getText().trim().equals("")){
            JOptionPane.showConfirmDialog(jf,"不能发送空白消息");
            return;
        }
        pw.println(name+":"+jtf.getText());
        pw.flush();
        jtf.setText("");
    }
    class ReadMassageThread extends Thread{
        public void run(){
            while (true){
                try{
                    String str=br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String args[]) throws IOException {
        ChatRoomCilent crc=new ChatRoomCilent();
        crc.actionPerformed();
    }
}