public class Carta {
    String peso;
    String naipe;

    public Carta(String peso, String naipe) {
        this.peso = peso;
        this.naipe = naipe;
    }

    public String getPeso() {
        return peso;
    }

    public String getNaipe() {
        return naipe;
    }

    public int valor() {
        return switch (peso) {
            case "J", "Q", "K" -> 10;
            case "A" -> 11;
            default -> Integer.parseInt(peso);
        };
    }

    @Override
    public String toString() {
        return peso + naipe;
    }

    // ======== 🔹 CARTA ESTILIZADA COM CORES ========

    public static String formatCarta(Carta c) {
        boolean vermelho = c.naipe.equals("♥") || c.naipe.equals("♦");
        String cor = vermelho ? "\u001B[31m" : "\u001B[37m";
        String reset = "\u001B[0m";

        String valor = c.peso.length() == 1 ? c.peso + " " : c.peso;
        String linha1 = "┌─────────┐";
        String linha2 = String.format("│%s%-2s%s       │", cor, valor, reset);
        String linha3 = "│         │";
        String linha4 = String.format("│    %s%s%s    │", cor, c.naipe, reset);
        String linha5 = "│         │";
        String linha6 = String.format("│       %s%-2s%s│", cor, valor, reset);
        String linha7 = "└─────────┘";

        return linha1 + "\n" + linha2 + "\n" + linha3 + "\n" + linha4 + "\n" + linha5 + "\n" + linha6 + "\n" + linha7;
    }
}
