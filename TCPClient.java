import java.io.*;
import java.net.*;

class TCPClient 
{
	public static void main(String argv[]) throws Exception
	{
		String sentence;
		String modifiedSentence;
		
		Socket clientSocket = new Socket("localhost", 12001);
                
                if(clientSocket != null){
                    String seqNum;
                    
                    System.out.println("welcome to the server. you know what to do");
                    
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          
		
                    sentence = inFromUser.readLine();
                    outToServer.writeBytes(sentence + '\n');
                    
                    
                    while((seqNum = inFromServer.readLine()) != null){
                        System.out.println(seqNum);
                     //   int parseStr = Integer.parseInt(seqNum);
                       // System.out.println(parseStr);
                    }
                    
                    System.out.println("We did it!");

                    clientSocket.close();
                    
                }else{
                    System.out.println("something went wrong with the connection");
                }

	}
}

