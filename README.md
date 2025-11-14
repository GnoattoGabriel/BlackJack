📌 Sobre o projeto

Este projeto implementa um jogo Blackjack multiplayer utilizando comunicação entre processos via sockets TCP.
Um servidor coordena toda a lógica do jogo, enquanto cada cliente se conecta ao servidor para participar da partida.

A comunicação é feita por mensagens de texto, e o servidor controla os turnos, distribui cartas e calcula os resultados.

Como executar o jogo
1. Compile o projeto

   javac *.java

2. Abra o arquivo Iniciar Jogo.bat

   

Como jogar

Após ambos os jogadores conectarem, o servidor inicia a partida:

Cada jogador recebe duas cartas.

O dealer recebe duas cartas (apenas uma visível).

O jogador 1 começa o turno.

No seu turno, o cliente deve digitar:

Comandos disponíveis

📌 HIT
Pede uma nova carta.

📌 STAND
Para de jogar e mantém a pontuação atual.

📌 QUIT
Sai da partida.



