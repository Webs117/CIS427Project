/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Adam, Griffin
 */
import java.io.*;
import java.net.*;

class TCPServer 
{
	public static void main(String argv[]) throws Exception
	{

		ServerSocket welcomeSocket = new ServerSocket(12001);
                
                //get project directory path
                String basePath = new File("").getAbsolutePath();
                String fileName = basePath + "\\src\\resources\\alice.txt";
     
                System.out.println("Server Log: \n");
                
                //server constantly running
                while(true) 
                {

                    Socket connectionSocket = welcomeSocket.accept();
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                    // client's first query
                    String clientQuery = inFromClient.readLine();
                    
                   
                    //keep connection to client open until quit
                    while(clientQuery.equals("quit") == false){

                        if (clientQuery.equals("query")){

                            // store current line
                            String line;
                            String initial = "0\n";

                            // byte array of current line
                            byte[] indvLine;

                            // cumulative byte count of each line being added 
                            int seqNum = 0; 

                            // For reading the text file
                            FileReader fileReader = new FileReader(fileName);
                            BufferedReader bufferedReader = new BufferedReader(fileReader);

                            // Log user command
                            System.out.println("User command: query");
                            
                            //Initial bytes
                            outToClient.writeBytes(initial);


                            while((line = bufferedReader.readLine()) != null) {

                                indvLine = line.getBytes();

                                seqNum += indvLine.length;

                                String toString = Integer.toString(seqNum) + '\n';

                                outToClient.writeBytes(toString);
                            } 

                            //send end of file special key 
                            outToClient.writeBytes("-1" + '\n');
                            
                            //close buffer
                            bufferedReader.close();
                            
                            //close file
                            fileReader.close();

                        }else if(clientQuery.equals("download")){
                            
                            //receive UDP port number 
                            String clientPort = inFromClient.readLine();
                            int clientUDPport = Integer.parseInt(clientPort);
                            InetAddress clientIPaddress = connectionSocket.getInetAddress();
                            
                            DatagramSocket UDPserverSocket = new DatagramSocket(6789);
                            
                
                            
                            // store current line
                            String line;
                            String initial = "0\n";

                            // byte array of current line
                            byte[] indvLine;

                            // cumulative byte count of each line being added 
                            int seqNum = 0; 

                            // For reading the text file
                            FileReader fileReader = new FileReader(fileName);
                            BufferedReader bufferedReader = new BufferedReader(fileReader);

                            // Log user command
                            System.out.println("User command: download");
                            
                            //Initial bytes
                            outToClient.writeBytes(initial);


                            while((line = bufferedReader.readLine()) != null) {
                                byte[] clientData = new byte[2048];
                                
                                indvLine = line.getBytes();

                                seqNum += indvLine.length;

                                String toString = Integer.toString(seqNum) + line;
                                
                                System.out.println(toString);
                                
                                clientData = toString.getBytes();
                                
                               DatagramPacket aliceTextUDPdatagram = new DatagramPacket(clientData, clientData.length, clientIPaddress, clientUDPport);
                               
                               UDPserverSocket.send(aliceTextUDPdatagram);
                            } 

                            //send end of file special key 
                            //outToClient.writeBytes("-1" + '\n');

                            //close UDP transfer socket
                            UDPserverSocket.close();
                            
                            //close buffer
                            bufferedReader.close();
                            
                            //close file
                            fileReader.close();
                            
                        }else{
                            System.out.println("Client managed to break client side if loop");
                        }
                        
                        //wait for next command
                        clientQuery = inFromClient.readLine();
                    }
                // user sent over "quit"
                System.out.println("User command: quit");
                
                //welcomeSocket.close();        
            }
                
	}
}


