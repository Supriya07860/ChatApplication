import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;
//import java.io.*;
//import java.net.*;
public class client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //declare components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messsageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);
    //constructor
    public client(){
            try{      
            System.out.println("sending request to server");
            socket= new Socket("127.0.0.1", 7775);
            System.out.println("connection done.");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
                createGUI();
                handleEvents();
            startReading();
            // startWriting();
             
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
    private void handleEvents(){
        messsageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
           //     throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
              //  throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
           // System.out.println("Key released"+e.getKeyCode());
            if(e.getKeyCode()==10){
                //System.out.println("you have pressed enter button");

                String contentToSend= messsageInput.getText();
                messageArea.append("Me :"+contentToSend+"\n");

                out.println(contentToSend);
                out.flush();
                messsageInput.setText("");
                messsageInput.requestFocus();

            }    
            
            }
            
        });
    }
    private void createGUI(){
        //gui code...
        this.setTitle("CLIENT MESSAGAER[END]");  //this is the window
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messsageInput.setFont(font);
       // heading.setIcon(new ImageIcon("messicon.jpg"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //frame ka layout

        this.setLayout(new BorderLayout());
        //Adding components to frame
        this.add(heading, BorderLayout.NORTH);
        this.add(messageArea, BorderLayout.CENTER);
        this.add(messsageInput, BorderLayout.SOUTH);






        this.setVisible(true);


    }
    // start reading[method]

    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("reader has started");
            try{
            while(true){
                
                String msg = br.readLine();
                if (msg.equals("exit")) {
                    System.out.println("server terminated the chat");
                    JOptionPane.showMessageDialog(null, "server terminates the chat");
                    messsageInput.setEnabled(false);

                    socket.close();
                    break;
                }
                //System.out.println("server : "+msg);
                messageArea.append("server : "+msg+"\n");

            }
        }
            catch(Exception e){
          //   e.printStackTrace();
          System.out.println("connection closed");   

            }
            
        };
         new Thread(r1).start();
    }
    // start writing [method]

    public void startWriting(){
        System.out.println("writer started..");
        Runnable r2 = ()->{
            try {
                while(true  && !socket.isClosed()){
                BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
                String content = br1.readLine();
                out.println(content);
                out.flush();
                if(content.equals("exit")){
                    socket.close();
                    break;
                }
                
                }
                System.out.println("connection closed");
            } catch (Exception e) {
                
                e.printStackTrace();
            }
        
        };
        new Thread(r2).start();
    }
    public static  void main( String[] args){
        System.out.println("this is server");
        new client();
    }
}
