import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception{

        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));


        String host = "localhost";
        int port = 12345;

        Socket socket = new Socket(host, port);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Thread reader = new Thread(() -> {
           try {
               String line;
               while ((line = in.readLine()) != null) {
                   System.out.println("[SERVER] " + line);
               }
           } catch (IOException e ){
               System.out.println("Conexao encerrada.");
           }
        });

        reader.setDaemon(true);
        reader.start();

        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = sc.nextLine();
            out.write(input + "\n");
            out.flush();
            if ("QUIT".equalsIgnoreCase(input)) break;
        }

        socket.close();
        sc.close();

    }
}
