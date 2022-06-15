import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static final int SERVER_PORT = 8989;
    public static final String SERVER_HOST = "localhost";

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        System.out.println(engine.search("бизнес"));
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    String word = in.readLine();
                    System.out.println("Запрос на поиск '" + word + "'");
                    List<PageEntry> answer = engine.search(word);
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    Type listType = new TypeToken<List<PageEntry>>() {
                    }.getType();
                    String result = gson.toJson(answer, listType);
                    out.println(result);
                    System.out.println("Ответ: " + result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}