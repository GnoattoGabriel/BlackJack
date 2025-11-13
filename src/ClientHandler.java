import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;
    private final GameState gameState;
    private final int id;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean ativo = true;

    public ClientHandler(Socket socket, Server server, GameState gameState, int id) {
        this.socket = socket;
        this.server = server;
        this.gameState = gameState;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            send("BEM-VINDO Jogador " + id);

            // Cada jogador recebe sua mão do GameState
            Mao mao = gameState.getMaoJogador(id);
            send("SUA MÃO INICIAL:\n" + mao.formatMao());

            String msg;
            while (ativo && (msg = in.readLine()) != null) {
                msg = msg.trim().toUpperCase();
                server.receberJogada(this, msg);
            }

        } catch (IOException e) {
            System.out.println("Jogador " + id + " desconectado!");
        } finally {
            encerrar();
        }
    }

    // Envia mensagem ao cliente
    public void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            System.out.println("Erro ao enviar mensagem ao jogador " + id + ": " + e.getMessage());
        }
    }

    public void encerrar() {
        ativo = false;
        try { socket.close(); } catch (IOException ignored) {}
    }

}
