package chatserver;
// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
import java.io.*;
import java.net.*;


import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Font;
// import java.awt.event.KeyListener;

import javax.swing.*;
// import jdk.internal.org.jline.utils.InputStreamReader;

class Server extends JFrame
{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

    public Server() throws IOException
    {
        try{
        server=new ServerSocket(7778);
        System.out.println("server is ready to accept connection");
        System.out.println("waiting...");
        socket = server.accept();

        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out=new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();


        startReading();
        startWriting();
        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener()
        {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("key released"+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

        });

    }

    /**
     * 
     */
    private void createGUI()
    {
        // this.setTitle("Server Messanger[END]");
      
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

    //     // heading.setIcon(new ImageIcon("live-chat-icon.png"));
    //     heading.setHorizontalTextPosition(SwingConstants.CENTER);
    //     heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.LEFT);

       

        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);

    }

    
    /**
     * 
     */
    public void startReading()
    {
        Runnable r1=()->{
            System.out.println("reader started");
            try{
            while(true)
            {
               
                String msg = br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Client terminated the chat");

                    // JOptionPane.showMessageDialog(heading, this, "Client Terminated the chat", 0);
                    JOptionPane.showMessageDialog(this, "Client Terminated the Chat");
                    messageInput.setEnabled(false);;

                    socket.close();
                    break;
                }

                // System.out.println("Client: "+msg);
                messageArea.append("Client: "+msg+"\n");
                }

            } catch(Exception e)
            {
                System.out.println("connection is closed");
            }

        };
        new Thread(r1).start();

    }

    public void startWriting()
    {
        Runnable r2=()->{
            System.out.println("writer started...");
            try{
                while(!socket.isClosed())
            {
               
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                
                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }


               
            }
        } catch(Exception e){
        //   
        System.out.println("connection is closed");
        }
        

        };

        new Thread(r2).start();

    }
    public static void main(String[] args) throws IOException {
        System.out.println("this is server...going to start");
        new Server();
    }
}