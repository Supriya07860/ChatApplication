import java.net.*;
import java.io.*;
class server {
    ServerSocket server;
    Socket  socket;
    BufferedReader br;
    PrintWriter out;
    public server(){
        try {
            server = new ServerSocket(7775);
            System.out.println("server is ready to accept connections");
            System.out.println("waiting...");
            socket=server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("reader has started");
            try{
            while(true){
                
                String msg = br.readLine();
                if (msg.equals("exit")) {
                    System.out.println("client terminated the chat");
                    socket.close();
                    break;
                }
                System.out.println("client : "+msg);
            }
        }catch(Exception e){
          //  e.printStackTrace();
          System.out.println("connection closed"); 
        }
            
            
        };
         new Thread(r1).start();
    }
    public void startWriting(){
        System.out.println("writer started..");
        Runnable r2 = ()->{
            try {
                while(true && !socket.isClosed()){
                BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
                String content = br1.readLine();
                out.println(content);
                out.flush();
                }
            
            } catch (Exception e) {
                
                //e.printStackTrace();
                System.out.println("connection closed"); 
            }
            //System.out.println("connection closed"); 
        };
        new Thread(r2).start();
    }
    public static void main(String[]  args){
        System.out.println("this is sever.. going to start server");
        new server();
        
    }
}