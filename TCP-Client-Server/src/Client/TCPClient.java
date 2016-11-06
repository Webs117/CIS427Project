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
import java.util.Arrays;
import java.util.Random;

class TCPClient 
{
	public static void main(String argv[]) throws Exception
	{
		
		Socket clientSocket = new Socket("localhost", 12001);
                
                //test for socket connection
                if(clientSocket != null){
                   
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
                            
                            String seqNum;
                            
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
                                

                                DatagramPacket serverDatagram = new DatagramPacket(serverData, serverData.length);

                                UDPclientSocket.receive(serverDatagram);
                                                               
                               //remove empty spaces after aliceline from packet data byte array
                                byte[] packetData = serverDatagram.getData();     
                                int trimPos = getEmptySlots(packetData); 
                                byte[] trimPacketData = Arrays.copyOfRange(packetData, 0, trimPos);
                                
                                //find pos of end of seq and start of txt line
                                int seqPos = getPosition(trimPacketData);
                                
                                //grab sequence number portion from trim
                                byte [] packetSeqNum = Arrays.copyOfRange(trimPacketData, 0, seqPos);
                                
                                //grab Txt file line from trim 
                                byte [] packetTxtLine = Arrays.copyOfRange(trimPacketData, seqPos, trimPacketData.length);
                                
                                //convert seqNum byte array into int seqNum
                                String strSeqNum = new String(packetSeqNum);
                                int currentSeqNum = Integer.parseInt(strSeqNum);
                                
                                
                                String txtLine = new String(packetTxtLine);
                                
                                boolean positiveACK = false;
                                
                             
                                    int spinTheWheel = rand.nextInt(10);
                                    if (spinTheWheel == 9){                                    
                                        //bad packet
                                        //do everything except update the sequence number and print
                                        String toString = Integer.toString(cumulativeSeqNum);
                                        sendACK = toString.getBytes();

                                        DatagramPacket sendACKpacket = new DatagramPacket(sendACK, sendACK.length, serverAddress,6789);
                                        UDPclientSocket.send(sendACKpacket);
                                    }
                                    else{   //everything normal
                                        if(txtLine.equals("-1")){
                                            endOfFile = 1;
                                        }else{
                                            String printString = Integer.toString(cumulativeSeqNum) + '\t' + txtLine;
                                            System.out.println(printString);
                                        }

                                        // Update sequence number                             
                                        cumulativeSeqNum += (txtLine.length() + 4);

                                        String toString = Integer.toString(cumulativeSeqNum);
                                        sendACK = toString.getBytes();

                                        DatagramPacket sendACKpacket = new DatagramPacket(sendACK, sendACK.length, serverAddress,6789);
                                        UDPclientSocket.send(sendACKpacket);
                                        positiveACK = true;
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
        
    //Assumes numbers at front of byte array and no alice line starts with a num
    //gets position of last number to split byte array        
    public static int getPosition(byte[] arr){
        int position = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i] <= 57 && arr[i] >= 48){
                position += 1;
            }else{
                //found end of numbers
                //break 
                i = arr.length;
            }
        }
        return position;
    }
    
    //Assumes first empty slot signals rest of array is empty 
    //Finds position of first empty slot
    public static int getEmptySlots(byte[] arr){
        int position = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == 0){
                //found end of numbers
                //break 
                i = arr.length;
            }else{

                position += 1;
            }
        }
        return position;
    }   
        

}