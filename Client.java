
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //declare components
    private JLabel heading=new JLabel("CLIENT AREA");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();

    private Font font =new Font("Roboto",Font.BOLD,20) ;
    //constructor
    public Client(){
        try {
            System.out.println("sending request to server");
            socket=new Socket("192.168.29.207",7779);
            System.out.println("connection done.");

            br =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            //startWriting();
            
        } catch (Exception e) {
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
                    messageArea.append("\n Me :"+contentToSend+"\n");
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
        this.setTitle("Client Messager");
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


    


    //start readingg
    public void startReading(){
        //thread-will read the data 

        Runnable r1=()->{
            System.out.println("Reader startred...");

try {
            while (true) { 
                
                String msg=br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"server Terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                //System.out.println();
                messageArea.append("Server: "+msg+"\n");
                } 
            }catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("Connection closed:");
                }  
        };

        new Thread(r1).start();
    }
    //start writing send[method]
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
                System.out.println("connection is closed");
            }  

        };
        new Thread(r2).start();
}



    public static void main(String[] args) {
        System.out.println("this is client..");
        new Client();
    }
}
