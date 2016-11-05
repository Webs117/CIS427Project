/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 * Sequence number should include sequence number into byte.length
 * so it should go 0 then 52 then 56
 * @author Adam, Griffin
 */
import java.io.*;
import java.net.*;
import java.util.Random;

class TCPClient 
{
	public static void main(String argv[]) throws Exception
	{
		
		
		
		Socket clientSocket = new Socket("localhost", 12001);
                
                //test for socket connection
                if(clientSocket != null){
                   
                   String seqNum;
                   String menuSelection;
                    
                   //Output Menu
                   System.out.print("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n" +
                            "+-+-+-+-+-+-+   Multiple Channel Protocol   +-+-+-+-+-+-+\n" +
                            "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n" +
                            "\tCommands allowed by the server for this client:\n" +
                            "\tquery\n" +
                            "\tdownload\n" +
                            "\tquit\n\n");
                    
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          
                    //first user input 
                    System.out.print("Command: ");
                    menuSelection = inFromUser.readLine();
                    
                    //take commands until user quits
                    while(menuSelection.equals("quit") == false){
                        
                        if(menuSelection.equals("query")){
                            
                            outToServer.writeBytes(menuSelection + '\n');
                            
                            //Write out sequence number
                            while(((seqNum = inFromServer.readLine()).equals("-1") == false)){
                                System.out.println(seqNum);
                            }
                        
                        }else if(menuSelection.equals("download")){
                            
                            outToServer.writeBytes("6800" + menuSelection + '\n');
                            
                            Random rand = new Random();
                            
                            //create UDP client UDP socket
                            DatagramSocket UDPclientSocket = new DatagramSocket(6800);
                            
                            InetAddress serverAddress = InetAddress.getByName("localhost");
                            
                            //hardcode initial sequence number 
                            int cumulativeSeqNum = 0;
                            
                            int endOfFile = 0; 
                            do{
                                byte[] serverData = new byte[2048];
                                byte[] sendACK = new byte[2048];
                                byte[] failACK = new byte[2048];
                                
                                String line;

                                DatagramPacket serverDatagram = new DatagramPacket(serverData, serverData.length);

                                UDPclientSocket.receive(serverDatagram);
                                
                                /*
                                int spinTheWheel = rand.nextInt(10);
                                if(spinTheWheel == 9){
                                    String toString = Integer.toString(cumulativeSeqNum);
                                    failACK = toString.getBytes();
                                    DatagramPacket sendFailACKpacket = new DatagramPacket(failACK, failACK.length, serverAddress,6789);
                                    UDPclientSocket.send(sendFailACKpacket);
                                    System.out.println("Packet Corrupted ");
                                    
                                }else{
                                    
                                }
                                */
                                                               
                                //this string is including all the extra white space in the 2048 bytes
                                line = new String(serverDatagram.getData());
                                
                                //grab length of entire length including seqNum
                                //datamgram length exludes the blank spaces 
                                int ackData = serverDatagram.getLength();
                                
                                cumulativeSeqNum += ackData;
                                
                                String toString = Integer.toString(cumulativeSeqNum);
                                sendACK = toString.getBytes();
                                
                                
                                DatagramPacket sendACKpacket = new DatagramPacket(sendACK, sendACK.length, serverAddress,6789);
                                UDPclientSocket.send(sendACKpacket);
                                
                                if(line.equals("-1")){
                                    endOfFile = 1;
                                }else{
                                    System.out.println(line); 
                                }
                                
                            }while(endOfFile == 0);
                            
                            

                            
                            UDPclientSocket.close();
                                    
                        }else{
                            System.out.println("Invalid Command. Try again \n");
                        }
                        
                        //get user input again 
                        System.out.print("Command: ");
                        menuSelection = inFromUser.readLine();
                    }
                    
                    //quit
                    outToServer.writeBytes(menuSelection + '\n');
                           
                    //close socket after sending quit
                    clientSocket.close();
                    
                }else{
                    System.out.println("Could not connect to server");
                }

	}
        
        
        

}


