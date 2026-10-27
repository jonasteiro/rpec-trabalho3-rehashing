import java.util.Random;

public class Trabalho3 {
    static int VAZIO = -1;
    static int REMOVIDO = -2;
    static int acessos = 0;
    static int reaproveitamentos = 0;
    static int duplicadasDescartadas = 0;

    // Calcula a posição inicial da chave na tabela e retorna o índice calculado
    static int funcaoHash(int chave, int tamanho) {
        return chave % tamanho;
    }

    // Calcula a próxima posição em caso de colisão e retorna o novo índice
    static int rehash(int posicao, int tamanho) {
        return (posicao + 1) % tamanho;
    }

    // Cria uma tabela nova preenchida com VAZIO e retorna o vetor criado
    static int[] criaTabela(int tamanho) {
        int[] tabela = new int[tamanho];
        for (int posicao = 0; posicao < tamanho; posicao++) {
            tabela[posicao] = VAZIO;
        }
        return tabela;
    }

    // Conta quantas posições da tabela possuem chaves válidas e retorna essa quantidade
    static int contaOcupadas(int[] tabela) {
        int quantidade = 0;
        for (int posicao = 0; posicao < tabela.length; posicao++) {
            if (tabela[posicao] >= 0) {
                quantidade++;
            }
        }
        return quantidade;
    }

    // Conta quantas posições da tabela estão marcadas como REMOVIDO e retorna essa quantidade
    static int contaRemovidas(int[] tabela) {
        int quantidade = 0;
        for (int posicao = 0; posicao < tabela.length; posicao++) {
            if (tabela[posicao] == REMOVIDO) {
                quantidade++;
            }
        }
        return quantidade;
    }

    // Conta quantas posições da tabela estão marcadas como VAZIO e retorna essa quantidade
    static int contaVazias(int[] tabela) {
        int quantidade = 0;
        for (int posicao = 0; posicao < tabela.length; posicao++) {
            if (tabela[posicao] == VAZIO) {
                quantidade++;
            }
        }
        return quantidade;
    }
}