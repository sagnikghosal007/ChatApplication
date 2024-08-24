
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading=new JLabel("SERVER AREA");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();

    private Font font =new Font("Roboto",Font.BOLD,20) ;
    //Constructor..
    public Server(){
        try{
            server=new ServerSocket(7779);
            System.out.println("Server is ready to accpet connection");
            System.out.println("waiting...");
            socket=server.accept();

            br =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();

            startReading();
            //startWriting();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        

    }


    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
              
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()!=10){
                } else {
                    //System.out.println("you have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("\n Person A :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

        });
    }

    private void createGUI(){
        //gui
        this.setTitle("Server Messager");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("downloadimg.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //we will set theblayout of the frame

        this.setLayout(new BorderLayout());

        //adding the components to frames
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        
        
        
        
        this.setVisible(true);
    }


    // to makew both the functions work simultaneously we will use the concept of multithreading 
    public void startReading(){
        //thread-will read the data 

        Runnable r1=()->{
            System.out.println("Reader startred...");
            try {

            while (true) { 
                
                String msg=br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"client Terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                messageArea.append("Client: "+msg+"\n");
                } 
            }
            catch (Exception e) {
                System.out.println("connection is closed");
            }  
        };

        new Thread(r1).start();
    }
    public void startWriting(){
            //thread- will send data from user to client
            Runnable r2=()->{
                System.out.println("writer started...");

                try {
                while(!socket.isClosed()){
                        BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                        String content=br1.readLine();
                        out.println(content);
                        out.flush();

                        if(content.equals("exit")){
                            socket.close();
                            break;
                        }
                        
                    }
                    System.out.println("connection is closed");
                }
                catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("connection is closed");
                }  

            };
            new Thread(r2).start();
    }


    public static void main(String[] args) {
        System.out.println("this is server ..going to start server");
        new Server();
    }
}