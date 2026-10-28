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

    // Insere uma chave na tabela (reaproveitando REMOVIDO, se possível) e retorna a posição, -1 se duplicada ou -2 se cheia
    public static int insere(int[] tabela, int chave) {
        int tamanho = tabela.length;
        int posicao = funcaoHash(chave, tamanho);
        int passos = 0;
        int primeiraRemovida = -1;
        while (passos < tamanho) {
            acessos++;
            passos++;
            if (tabela[posicao] == VAZIO) {
                if (primeiraRemovida != -1) {
                    tabela[primeiraRemovida] = chave;
                    reaproveitamentos++;
                    return primeiraRemovida;
                } else {
                    tabela[posicao] = chave;
                    return posicao;
                }
            }
            if (tabela[posicao] == chave) {
                return -1;
            }
            if (tabela[posicao] == REMOVIDO && primeiraRemovida == -1) {
                primeiraRemovida = posicao;
            }
            posicao = rehash(posicao, tamanho);
        }
        if (primeiraRemovida != -1) {
            tabela[primeiraRemovida] = chave;
            reaproveitamentos++;
            return primeiraRemovida;
        }
        return -2;
    }

    // Reconstrói a tabela em uma nova tabela limpa sem posições REMOVIDO
    public static void reorganiza(int[] tabela) {
        int[] nova = criaTabela(tabela.length);
        for (int posicao = 0; posicao < tabela.length; posicao++) {
            if (tabela[posicao] >= 0) {
                insere(nova, tabela[posicao]);
            }
        }
        for (int posicao = 0; posicao < tabela.length; posicao++) {
            tabela[posicao] = nova[posicao];
        }
    }

    // Gera um vetor de chaves distintas de 7 dígitos e retorna o vetor gerado
    public static int[] geraChaves(Random sorteio, int quantidade) {
        int[] chaves = new int[quantidade];
        int gerados = 0;
        duplicadasDescartadas = 0;
        while (gerados < quantidade) {
            int candidata = 1000000 + sorteio.nextInt(9000000);
            boolean repetida = false;
            for (int indice = 0; indice < gerados; indice++) {
                if (chaves[indice] == candidata) {
                    repetida = true;
                }
            }
            if (repetida) {
                duplicadasDescartadas++;
            } else {
                chaves[gerados] = candidata;
                gerados++;
            }
        }
        return chaves;
    }

    // Gera um vetor de chaves de 7 dígitos que não estão no vetor chaves e retorna o vetor gerado
    public static int[] geraAusentes(Random sorteio, int[] chaves, int quantidade) {
        int[] ausentes = new int[quantidade];
        int gerados = 0;
        while (gerados < quantidade) {
            int candidata = 1000000 + sorteio.nextInt(9000000);
            boolean existe = false;
            for (int indice = 0; indice < chaves.length; indice++) {
                if (chaves[indice] == candidata) {
                    existe = true;
                }
            }
            if (!existe) {
                ausentes[gerados] = candidata;
                gerados++;
            }
        }
        return ausentes;
    }
}