import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        for (String word : new String[]{"бизнес", "Бизнес"}) {
            try (Socket clientSocket = new Socket(Main.SERVER_HOST, Main.SERVER_PORT);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                out.println(word);
                String resp = in.readLine();
                System.out.println("Ответ на '" + word + "' = " + resp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}