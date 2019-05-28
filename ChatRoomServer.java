package xl.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ChatRoomServer {
    private ServerSocket ss;
    private Set<Socket> allSockets;
    public ChatRoomServer() {
        try {
            ss=new ServerSocket(9999);
            allSockets=new HashSet<Socket>();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    public void init() throws IOException {
        while(true) {
            Socket s = ss.accept();
            allSockets.add(s);
            new ServerThread(s).start();
        }
    }
    class ServerThread extends Thread{
        private Socket s;
        private BufferedReader br;

        public  ServerThread(Socket s){
            this.s=s;
            try{
                br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run(){
            while (true) {
                try {

                    String str = br.readLine();
                    if (str.indexOf("%EXIT%") == 0) {
                        allSockets.remove(s);
                        s.close();
                        sendMassageToALLClient(str.split(":")[1] + ",离开聊天室");
                    }
                    sendMassageToALLClient(str);
                    break;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void sendMassageToALLClient(String mesg) throws IOException {
            Date data=new Date();
            System.out.println(mesg+"\t["+data+"]"+s.getInetAddress());
            for (Socket s:allSockets){
                PrintWriter pw=new PrintWriter(s.getOutputStream());
                pw.println(mesg+"\t["+data+"]");
                pw.flush();
            }
        }
    }
}
