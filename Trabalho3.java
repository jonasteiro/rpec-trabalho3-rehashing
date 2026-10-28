import java.util.Random;

public class Trabalho3 {
    private static int VAZIO = -1;
    private static int REMOVIDO = -2;
    private static int acessos = 0;
    private static int reaproveitamentos = 0;
    private static int duplicadasDescartadas = 0;

    // Calcula a posição inicial da chave na tabela e retorna o índice calculado
    public static int funcaoHash(int chave, int tamanho) {
        return chave % tamanho;
    }

    // Calcula a próxima posição em caso de colisão e retorna o novo índice
    public static int rehash(int posicao, int tamanho) {
        return (posicao + 1) % tamanho;
    }

    // Cria uma tabela nova preenchida com VAZIO e retorna o vetor criado
    public static int[] criaTabela(int tamanho) {
        int[] tabela = new int[tamanho];
        for (int posicao = 0; posicao < tamanho; posicao++) {
            tabela[posicao] = VAZIO;
        }
        return tabela;
    }

    // Conta quantas posições da tabela possuem chaves válidas e retorna essa quantidade
    public static int contaOcupadas(int[] tabela) {
        int quantidade = 0;
        for (int posicao = 0; posicao < tabela.length; posicao++) {
            if (tabela[posicao] >= 0) {
                quantidade++;
            }
        }
        return quantidade;
    }

    // Conta quantas posições da tabela estão marcadas como REMOVIDO e retorna essa quantidade
    public static int contaRemovidas(int[] tabela) {
        int quantidade = 0;
        for (int posicao = 0; posicao < tabela.length; posicao++) {
            if (tabela[posicao] == REMOVIDO) {
                quantidade++;
            }
        }
        return quantidade;
    }

    // Conta quantas posições da tabela estão marcadas como VAZIO e retorna essa quantidade
    public static int contaVazias(int[] tabela) {
        int quantidade = 0;
        for (int posicao = 0; posicao < tabela.length; posicao++) {
            if (tabela[posicao] == VAZIO) {
                quantidade++;
            }
        }
        return quantidade;
    }

    // Procura uma chave na tabela e retorna sua posição ou -1 se não existir
    public static int busca(int[] tabela, int chave) {
        int tamanho = tabela.length;
        int posicao = funcaoHash(chave, tamanho);
        int passos = 0;
        while (passos < tamanho) {
            acessos++;
            passos++;
            if (tabela[posicao] == VAZIO) {
                return -1;
            }
            if (tabela[posicao] == chave) {
                return posicao;
            }
            posicao = rehash(posicao, tamanho);
        }
        return -1;
    }

    // Remove uma chave gravando VAZIO na posição e retorna true se removeu ou false se não encontrou
    public static boolean removeIngenua(int[] tabela, int chave) {
        int posicao = busca(tabela, chave);
        if (posicao == -1) {
            return false;
        }
        tabela[posicao] = VAZIO;
        return true;
    }

    // Remove uma chave gravando REMOVIDO na posição e retorna true se removeu ou false se não encontrou
    public static boolean remove(int[] tabela, int chave) {
        int posicao = busca(tabela, chave);
        if (posicao == -1) {
            return false;
        }
        tabela[posicao] = REMOVIDO;
        return true;
    }
}