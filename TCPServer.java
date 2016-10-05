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
     
       
                while(true) 
                {


                        Socket connectionSocket = welcomeSocket.accept();
                        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());


                        String clientQuery = inFromClient.readLine();

                        if (clientQuery.equals("Query")){

                            // store current line
                            String line = null; 

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

                                indvLine = line.getBytes();

                                seqNum += indvLine.length;

                                System.out.println(seqNum);

                                String toString = Integer.toString(seqNum);
                                System.out.println("toString:" + toString);
                                outToClient.writeBytes(toString);
                            } 
                           

                        }else if(clientQuery.equals("Download")){

                        }else{
                            System.out.println("Client messed up");
                        }
        
	  }
	}
}

