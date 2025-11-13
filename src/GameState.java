import java.util.*;

public class GameState {

    private final Deck deck;
    private final Mao dealer;
    private final Map<Integer, Mao> maosJogadores;
    private int jogadorAtual;
    private boolean partidaEmAndamento;

    public GameState() {
        this.deck = new Deck();
        this.dealer = new Mao();
        this.maosJogadores = new HashMap<>();
        this.jogadorAtual = 0;
        this.partidaEmAndamento = false;
    }

    public synchronized void adicionarJogador(int id) {
        maosJogadores.put(id, new Mao());
    }

    public synchronized Mao getMaoJogador(int id) {
        return maosJogadores.get(id);
    }

    public synchronized Mao getDealer() {
        return dealer;
    }

    public synchronized Deck getDeck() {
        return deck;
    }

    public synchronized void setJogadorAtual(int id) {
        this.jogadorAtual = id;
    }

    public synchronized void iniciarPartida() {
        partidaEmAndamento = true;
    }

    public synchronized void encerrarPartida() {
        partidaEmAndamento = false;
    }

}
