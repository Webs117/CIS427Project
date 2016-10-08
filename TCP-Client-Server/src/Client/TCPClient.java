/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author Adam, Griffin
 */
import java.io.*;
import java.net.*;

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
                            System.out.println("Soon");
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


