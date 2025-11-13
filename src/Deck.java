import java.util.*;

public class Deck {

    private final Deque<Carta> cartas = new ArrayDeque<>();

    public Deck() {
        String[] naipes = {"♣", "♦", "♥", "♠"};
        String[] pesos = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};

        List<Carta> list = new ArrayList<>();
        for (String p : pesos){
            for (String n : naipes) {
                list.add(new Carta(p, n));
            }
        }
        Collections.shuffle(list);
        cartas.addAll(list);
    }

    public Carta draw() {
        if (cartas.isEmpty()) return null;
        return cartas.removeFirst();
    }
}
