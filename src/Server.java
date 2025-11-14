import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private static final int PORT = 12345;

    private final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private final GameState gameState = new GameState();

    private String ultimaJogada = null;
    private ClientHandler jogadorAtual = null;

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        new Server().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor Blackjack aguardando clientes...");

            while (clients.size() < 2) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, this, gameState, clients.size() + 1);
                clients.add(handler);
                gameState.adicionarJogador(handler.getId());
                new Thread(handler).start();
                System.out.println("Jogador " + clients.size() + " conectado!");
            }

            System.out.println("Dois jogadores conectados. Iniciando partida!");
            transmissao("Dois jogadores conectados. Iniciando partida!");
            startGame();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void receberJogada(ClientHandler jogador, String msg) {
        if (jogador == jogadorAtual) {
            ultimaJogada = msg;
            notifyAll();
        }
    }

    private void transmissao(String msg) {
        for (ClientHandler c : clients) {
            c.send(msg);
        }
    }

    private synchronized void esperarJogada() {
        ultimaJogada = null;
        while (ultimaJogada == null) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
    }

    private void reiniciarPartida() {
        gameState.getDeck().reset();
        gameState.getDealer().clear();

        for (ClientHandler c : clients) {
            gameState.getMaoJogador(c.getId()).clear();
        }

        ultimaJogada = null;
        jogadorAtual = null;
    }


    public void startGame() {
        gameState.iniciarPartida();

        // Distribui 2 cartas para cada jogador e 2 para o dealer
        for (int i = 0; i < 2; i++) {
            for (ClientHandler c : clients) {
                Mao mao = gameState.getMaoJogador(c.getId());
                mao.add(gameState.getDeck().draw());
            }
            gameState.getDealer().add(gameState.getDeck().draw());
        }

        // Mostra a primeira carta do dealer
        Carta primeira = gameState.getDealer().getPrimeiraCarta();
        if (primeira != null) {
            transmissao("CARTA DO DEALER VISÍVEL:\n" + Carta.formatCarta(primeira));
            System.out.println("CARTA DO DEALER VISÍVEL:\n" + Carta.formatCarta(primeira));
        }

        // Envia as mãos iniciais
        for (ClientHandler c : clients) {
            c.send("SUA MÃO:\n" + gameState.getMaoJogador(c.getId()).formatMao());
        }

        // Turnos dos jogadores
        for (ClientHandler player : clients) {
            jogadorAtual = player;
            gameState.setJogadorAtual(player.getId());
            player.send("SEU TURNO — Digite HIT ou STAND");

            boolean fimTurno = false;

            while (!fimTurno) {
                esperarJogada();
                String acao = ultimaJogada;

                if ("HIT".equalsIgnoreCase(acao)) {
                    Carta c = gameState.getDeck().draw();
                    Mao mao = gameState.getMaoJogador(player.getId());
                    mao.add(c);
                    player.send("Você recebeu:\n" + Carta.formatCarta(c));
                    player.send("SUA MÃO:\n" + mao.formatMao());

                    if (mao.isBust()) {
                        player.send("Você estourou!");
                        fimTurno = true;
                    } else {
                        player.send("HIT ou STAND?");
                    }

                } else if ("STAND".equalsIgnoreCase(acao)) {
                    player.send("Você parou com:\n" + gameState.getMaoJogador(player.getId()).formatMao());
                    fimTurno = true;

                } else {
                    player.send("Comando inválido. Use HIT ou STAND.");
                }
            }
        }

        // Turno do dealer
        while (gameState.getDealer().maiorPeso() < 17) {
            gameState.getDealer().add(gameState.getDeck().draw());
        }

        transmissao("MÃO FINAL DO DEALER:\n" + gameState.getDealer().formatMao());
        System.out.println("MÃO FINAL DO DEALER:\n" + gameState.getDealer().formatMao());

        // Resultados finais
        for (ClientHandler player : clients) {
            Mao maoJogador = gameState.getMaoJogador(player.getId());
            String result = calcularResultado(maoJogador, gameState.getDealer());
            player.send(
                    "RESULTADO: " + result +
                            "\nSUA MÃO:\n" + maoJogador.formatMao() +
                            "\nDEALER:\n" + gameState.getDealer().formatMao()
            );

            System.out.println("Jogador " + player.getId() + ": " + result);
        }

        System.out.println("Mão final do Dealer:\n" + gameState.getDealer().formatMao());
        System.out.println("==============================\n");

        transmissao("GAME_OVER");
        gameState.encerrarPartida();



        transmissao("Deseja jogar novamente? (SIM/NAO)");
        Map<ClientHandler, String> respostas = new HashMap<>();

        for (ClientHandler player : clients) {
            jogadorAtual = player;
            esperarJogada();
            respostas.put(player, ultimaJogada);
        }

        boolean todosQuerem = respostas.values()
                .stream()
                .allMatch(r -> r.equalsIgnoreCase("SIM"));

        if (todosQuerem) {
            transmissao("Reiniciando partida...");
            reiniciarPartida();
            startGame();
        } else {
            transmissao("Encerrando jogo. Obrigado por jogar!");
            for (ClientHandler c : clients) {
                c.encerrar();
            }
        }
    }

    private String calcularResultado(Mao player, Mao dealer) {
        int p = player.maiorPeso();
        int d = dealer.maiorPeso();

        if (player.isBust()) return "Você perdeu (estourou)";
        if (dealer.isBust()) return "Você ganhou (dealer estourou)";
        if (p > d) return "Você ganhou!";
        if (p == d) return "Empate!";
        return "Você perdeu!";
    }
}
