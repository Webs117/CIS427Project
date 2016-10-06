import java.io.*;
import java.net.*;

class TCPServer 
{
	public static void main(String argv[]) throws Exception
	{
		
               
                while(true) 
                {
                    ServerSocket welcomeSocket = new ServerSocket(12001);
                
                    //get project directory path
                    String basePath = new File("").getAbsolutePath();
                    String fileName = basePath + "\\src\\resources\\alice.txt";
                    String reply = "Invalid Input." + '\n';
                    
                    Socket connectionSocket = welcomeSocket.accept();
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    String clientQuery = inFromClient.readLine();
                    System.out.println(clientQuery);
                        if (clientQuery.equals("query") == true){
                            // store current line
                            String line; 
                            // byte array of current line
                            byte[] indvLine;
                            // cumulative byte count of each line being added 
                            int seqNum = 0; 
                            //Initial byte count
                            System.out.println(seqNum);
                            // For reading the text file
                            FileReader fileReader = new FileReader(fileName);
                            BufferedReader bufferedReader = new BufferedReader(fileReader);

                            while((line = bufferedReader.readLine()) != null) {
                                //line = bufferedReader.readLine();
                                indvLine = line.getBytes();
                                seqNum += indvLine.length;
                                //System.out.println(seqNum);
                                String toString = Integer.toString(seqNum) + '\n';
                                //System.out.println("toString:" + toString);
                                outToClient.writeBytes(toString);
                            } 
                            outToClient.writeBytes("-1" + '\n');
                        }else if(clientQuery.equals("download") == true){
                            outToClient.writeBytes(reply);
                        }else{
                            outToClient.writeBytes(reply);
                        }
                        welcomeSocket.close();
               }
	}
}
