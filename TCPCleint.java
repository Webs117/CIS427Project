import java.io.*;
import java.net.*;

class TCPClient 
{
    	public static void main(String argv[]) throws Exception
	{    
            String sentence = "quit";
            System.out.print("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n" +
                            "+-+-+-+-+-+-+   Multiple Channel Protocol   +-+-+-+-+-+-+\n" +
                            "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n" +
                            "\tCommands allowed by the server for this client:\n" +
                            "\tquery\n" +
                            "\tdownload\n" +
                            "\tquit\n\n");
            
            
            
            do{     
                Socket clientSocket = new Socket("localhost", 12001);
                if(clientSocket != null){  
                    String seqNum;
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    
                    System.out.print("Command: ");
                    sentence = inFromUser.readLine();
                    System.out.println();
                    if (sentence.equals("quit") == false)
                    {
                        outToServer.writeBytes(sentence + '\n');

                        while((inFromServer.readLine()).equals("-1") == false){
                            seqNum = inFromServer.readLine();
                            System.out.println(seqNum);
                        }
                    }     
                } else{
                    System.out.println("Connection Error.");
                }
                clientSocket.close(); 
            }while(sentence.equals("quit") == false);
          
        System.out.println("Goodbye");
    }
}

