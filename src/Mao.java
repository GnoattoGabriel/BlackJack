import java.util.ArrayList;
import java.util.List;

public class Mao {

    private final List<Carta> cartas = new ArrayList<>();

    public void add(Carta c) {
        cartas.add(c);
    }

    public synchronized void clear() {
        cartas.clear();
    }

    public int maiorPeso() {
        int total = 0;
        int As = 0;

        for (Carta c : cartas) {
            total += c.valor();
            if ("A".equals(c.getPeso())) As++;
        }

        while (total > 21 && As > 0) {
            total -= 10;
            As--;
        }

        return total;
    }

    public boolean isBust() {
        return maiorPeso() > 21;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Carta c : cartas) {
            sb.append(c.toString()).append(" ");
        }
        sb.append("(").append(maiorPeso()).append(")");
        return sb.toString();
    }

    //  Exibe cartas lado a lado em blocos ASCII
    public String formatMao() {
        if (cartas.isEmpty()) return "(vazia)";

        StringBuilder topo = new StringBuilder();
        StringBuilder meio1 = new StringBuilder();
        StringBuilder meio2 = new StringBuilder();
        StringBuilder meio3 = new StringBuilder();
        StringBuilder meio4 = new StringBuilder();
        StringBuilder meio5 = new StringBuilder();
        StringBuilder base = new StringBuilder();

        for (Carta c : cartas) {
            String[] linhas = Carta.formatCarta(c).split("\n");
            topo.append(linhas[0]).append("  ");
            meio1.append(linhas[1]).append("  ");
            meio2.append(linhas[2]).append("  ");
            meio3.append(linhas[3]).append("  ");
            meio4.append(linhas[4]).append("  ");
            meio5.append(linhas[5]).append("  ");
            base.append(linhas[6]).append("  ");
        }

        return topo + "\n" +
                meio1 + "\n" +
                meio2 + "\n" +
                meio3 + "\n" +
                meio4 + "\n" +
                meio5 + "\n" +
                base + "\n(" + maiorPeso() + ")";
    }

    // Para acessar cartas individuais (ex: dealer mostrar só a primeira)
    public Carta getPrimeiraCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.get(0);
    }
}
