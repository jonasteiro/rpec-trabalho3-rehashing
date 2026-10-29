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

    // Executa os 26 testes de validação e imprime se cada um foi aprovado ou reprovado
    public static void executaTestes() {
        int aprovados = 0;

        // Teste G1: sorteia 5 chaves e confere se são iguais aos do PDF
        Random sorteio = new Random(1990);
        int[] chaves = geraChaves(sorteio, 5);
        if (chaves[0] == 4429279 && chaves[1] == 3976163 && chaves[2] == 9187615
                && chaves[3] == 1167825 && chaves[4] == 9651672 && duplicadasDescartadas == 0) {
            System.out.println("TESTE G1: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE G1: REPROVADO");
        }

        // Teste G2: continua com o mesmo sorteio, gera 3 ausentes e confere os valores
        int[] ausentes = geraAusentes(sorteio, chaves, 3);
        if (ausentes[0] == 8282916 && ausentes[1] == 7251645 && ausentes[2] == 6739522) {
            System.out.println("TESTE G2: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE G2: REPROVADO");
        }

        // Teste G3: cria um novo Random(1990), gera 700 chaves e confere a última (posicao 699)
        sorteio = new Random(1990);
        chaves = geraChaves(sorteio, 700);
        if (chaves[699] == 9691227) {
            System.out.println("TESTE G3: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE G3: REPROVADO");
        }

        // Teste G4: continua com o mesmo sorteio, gera 350 ausentes e confere 4 posições delas
        ausentes = geraAusentes(sorteio, chaves, 350);
        if (ausentes[0] == 7816661 && ausentes[1] == 1376564
                && ausentes[2] == 1172019 && ausentes[349] == 6734863) {
            System.out.println("TESTE G4: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE G4: REPROVADO");
        }

        // Teste N1: cria tabela de 11 posições e insere 22, 33, 44 e 55
        int[] tabela = criaTabela(11);
        int retorno1 = insere(tabela, 22);
        int retorno2 = insere(tabela, 33);
        int retorno3 = insere(tabela, 44);
        int retorno4 = insere(tabela, 55);
        if (retorno1 == 0 && retorno2 == 1 && retorno3 == 2 && retorno4 == 3
                && tabela[0] == 22 && tabela[1] == 33 && tabela[2] == 44 && tabela[3] == 55
                && contaVazias(tabela) == 7) {
            System.out.println("TESTE N1: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE N1: REPROVADO");
        }

        // Teste N2: remove a chave 33 com removeIngenua e confere se a posição 1 virou VAZIO
        boolean removeu = removeIngenua(tabela, 33);
        if (removeu && tabela[1] == VAZIO) {
            System.out.println("TESTE N2: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE N2: REPROVADO");
        }

        // Teste N3: busca a chave 44 (que dá defeito e retorna -1 por causa do VAZIO na posição 1)
        acessos = 0;
        int resultado = busca(tabela, 44);
        if (resultado == -1 && acessos == 2) {
            System.out.println("TESTE N3: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE N3: REPROVADO");
        }

        // Teste N4: busca a chave 55 (que também dá defeito e retorna -1 com 2 acessos)
        acessos = 0;
        resultado = busca(tabela, 55);
        if (resultado == -1 && acessos == 2) {
            System.out.println("TESTE N4: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE N4: REPROVADO");
        }

        // Teste C1: cria tabela nova de 11, insere 22, 33, 44 e 55 e remove 33 com remove (correta)
        tabela = criaTabela(11);
        insere(tabela, 22);
        insere(tabela, 33);
        insere(tabela, 44);
        insere(tabela, 55);
        reaproveitamentos = 0;
        removeu = remove(tabela, 33);
        if (removeu && tabela[1] == REMOVIDO) {
            System.out.println("TESTE C1: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE C1: REPROVADO");
        }

        // Teste C2: busca a chave 44 (agora encontra na posição 2 com 3 acessos: posições 0, 1 e 2)
        acessos = 0;
        resultado = busca(tabela, 44);
        if (resultado == 2 && acessos == 3) {
            System.out.println("TESTE C2: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE C2: REPROVADO");
        }

        // Teste C3: busca a chave 55 (encontra na posição 3 com 4 acessos: posições 0, 1, 2 e 3)
        acessos = 0;
        resultado = busca(tabela, 55);
        if (resultado == 3 && acessos == 4) {
            System.out.println("TESTE C3: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE C3: REPROVADO");
        }

        // Teste C4: busca a chave 77 (ausente, olha as posições 0, 1, 2, 3 e para no VAZIO da posição 4 = 5 acessos)
        acessos = 0;
        resultado = busca(tabela, 77);
        if (resultado == -1 && acessos == 5) {
            System.out.println("TESTE C4: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE C4: REPROVADO");
        }

        // Teste C5: busca a chave 33 (já removida, olha de 0 a 4 e retorna -1 com 5 acessos)
        acessos = 0;
        resultado = busca(tabela, 33);
        if (resultado == -1 && acessos == 5) {
            System.out.println("TESTE C5: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE C5: REPROVADO");
        }

        // Teste C6: tenta remover 99 (ausente, retorna false com 5 acessos, mantendo 3 ocupadas e 1 removida)
        acessos = 0;
        removeu = remove(tabela, 99);
        if (!removeu && acessos == 5 && contaOcupadas(tabela) == 3 && contaRemovidas(tabela) == 1) {
            System.out.println("TESTE C6: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE C6: REPROVADO");
        }

        // Teste C7: tenta inserir 44 (duplicada, passa pelo REMOVIDO na posição 1, vê o 44 na posição 2 e retorna -1)
        acessos = 0;
        reaproveitamentos = 0;
        resultado = insere(tabela, 44);
        if (resultado == -1 && acessos == 3 && reaproveitamentos == 0) {
            System.out.println("TESTE C7: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE C7: REPROVADO");
        }

        // Teste C8: insere 66 (vai até o VAZIO na posição 4 com 5 acessos e grava sobre o REMOVIDO da posição 1)
        acessos = 0;
        reaproveitamentos = 0;
        resultado = insere(tabela, 66);
        if (resultado == 1 && acessos == 5 && reaproveitamentos == 1
                && tabela[1] == 66 && contaRemovidas(tabela) == 0) {
            System.out.println("TESTE C8: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE C8: REPROVADO");
        }

        // 6.4 Tabela de 3 posições, sem laço infinito (F1 a F5)
        // Teste F1: cria tabela de 3 posições, lota com 3, 6 e 9 e busca 12 (para após 3 acessos)
        tabela = criaTabela(3);
        insere(tabela, 3);
        insere(tabela, 6);
        insere(tabela, 9);
        acessos = 0;
        resultado = busca(tabela, 12);
        if (tabela[0] == 3 && tabela[1] == 6 && tabela[2] == 9
                && resultado == -1 && acessos == 3) {
            System.out.println("TESTE F1: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE F1: REPROVADO");
        }

        // Teste F2: tenta inserir 12 na tabela cheia (retorna -2 após 3 acessos)
        acessos = 0;
        resultado = insere(tabela, 12);
        if (resultado == -2 && acessos == 3) {
            System.out.println("TESTE F2: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE F2: REPROVADO");
        }

        // Teste F3: remove 6 (posição 1 vira REMOVIDO) e busca 12 (termina após 3 acessos mesmo sem VAZIO)
        remove(tabela, 6);
        acessos = 0;
        resultado = busca(tabela, 12);
        if (tabela[1] == REMOVIDO && resultado == -1 && acessos == 3) {
            System.out.println("TESTE F3: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE F3: REPROVADO");
        }

        // Teste F4: insere 12 (dá a volta completa com 3 acessos e reaproveita o REMOVIDO da posição 1)
        acessos = 0;
        reaproveitamentos = 0;
        resultado = insere(tabela, 12);
        if (resultado == 1 && acessos == 3 && reaproveitamentos == 1 && tabela[1] == 12) {
            System.out.println("TESTE F4: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE F4: REPROVADO");
        }

        // Teste F5: busca 15 na tabela cheia de novo (retorna -1 após 3 acessos)
        acessos = 0;
        resultado = busca(tabela, 15);
        if (resultado == -1 && acessos == 3) {
            System.out.println("TESTE F5: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE F5: REPROVADO");
        }

        // 6.5 Reorganização (O1 a O5)
        // Teste O1: insere 22, 33, 44, 55 em tabela de 11, remove 33 e chama reorganiza
        tabela = criaTabela(11);
        insere(tabela, 22);
        insere(tabela, 33);
        insere(tabela, 44);
        insere(tabela, 55);
        remove(tabela, 33);
        reorganiza(tabela);
        if (tabela[0] == 22 && tabela[1] == 44 && tabela[2] == 55 && tabela[3] == VAZIO
                && contaRemovidas(tabela) == 0 && contaOcupadas(tabela) == 3) {
            System.out.println("TESTE O1: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE O1: REPROVADO");
        }

        // Teste O2: busca 55 na tabela reorganizada de O1 (agora está na vaga 2, com 3 acessos)
        acessos = 0;
        resultado = busca(tabela, 55);
        if (resultado == 2 && acessos == 3) {
            System.out.println("TESTE O2: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE O2: REPROVADO");
        }

        // Teste O3: cria tabela de 11, insere 12 (posição 1), 22 (posição 0) e 33 (colide na 0 e na 1, vai pra 2), remove 22
        tabela = criaTabela(11);
        insere(tabela, 12);
        insere(tabela, 22);
        insere(tabela, 33);
        remove(tabela, 22);
        if (tabela[0] == REMOVIDO && tabela[1] == 12 && tabela[2] == 33) {
            System.out.println("TESTE O3: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE O3: REPROVADO");
        }

        // Teste O4: reorganiza a tabela de O3 (o 33 volta para a posição 0 e o 12 fica na posição 1)
        reorganiza(tabela);
        if (tabela[0] == 33 && tabela[1] == 12 && tabela[2] == VAZIO && contaRemovidas(tabela) == 0) {
            System.out.println("TESTE O4: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE O4: REPROVADO");
        }

        // Teste O5: busca 33 na tabela de O4 (encontra direto na posição 0 com apenas 1 acesso)
        acessos = 0;
        resultado = busca(tabela, 33);
        if (resultado == 0 && acessos == 1) {
            System.out.println("TESTE O5: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE O5: REPROVADO");
        }

        System.out.println("TESTES: " + aprovados + " de 26 aprovados");
    }

    // Executa os 5 experimentos em uma tabela de 1000 posições e imprime os resultados
    static void executaExperimentos() {
        Random sorteio = new Random(1990);
        int[] chaves = geraChaves(sorteio, 700);
        int descartadas = duplicadasDescartadas;
        int[] ausentes = geraAusentes(sorteio, chaves, 350);

        // Experimento 0: antes das remoções
        int[] tabela0 = criaTabela(1000);
        for (int indice = 0; indice < 700; indice++) {
            insere(tabela0, chaves[indice]);
        }

        acessos = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            busca(tabela0, chaves[indice]);
        }

        float acessosSucessoMedia0 = (float) acessos / 350;

        acessos = 0;
        for (int indice = 0; indice < 350; indice++) {
            busca(tabela0, ausentes[indice]);
        }

        float acessosFalhaMedia0 = (float) acessos / 350;
        System.out.printf("EXPERIMENTO 0 (antes das remocoes): ocupadas=%d removidas=%d vazias=%d acessosSucessoMedia=%.2f acessosFalhaMedia=%.2f%n",
                contaOcupadas(tabela0), contaRemovidas(tabela0), contaVazias(tabela0), acessosSucessoMedia0, acessosFalhaMedia0);

        // Experimento 1: remoção ingênua
        int[] tabela1 = criaTabela(1000);
        for (int indice = 0; indice < 700; indice++) {
            insere(tabela1, chaves[indice]);
        }

        int removidasComSucesso1 = 0;
        for (int indice = 0; indice < 700; indice += 2) {
            if (removeIngenua(tabela1, chaves[indice])) {
                removidasComSucesso1++;
            }
        }

        int falsosNegativos1 = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            if (busca(tabela1, chaves[indice]) == -1) {
                falsosNegativos1++;
            }
        }

        System.out.printf("EXPERIMENTO 1 (remocao ingenua): removidasComSucesso=%d falsosNegativos=%d ocupadas=%d vazias=%d%n",
                removidasComSucesso1, falsosNegativos1, contaOcupadas(tabela1), contaVazias(tabela1));

        // Experimento 2: remoção correta (com REMOVIDO)
        int[] tabela2 = criaTabela(1000);
        for (int indice = 0; indice < 700; indice++) {
            insere(tabela2, chaves[indice]);
        }

        int removidasComSucesso2 = 0;
        for (int indice = 0; indice < 700; indice += 2) {
            if (remove(tabela2, chaves[indice])) {
                removidasComSucesso2++;
            }
        }

        int falsosNegativos2 = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            if (busca(tabela2, chaves[indice]) == -1) {
                falsosNegativos2++;
            }
        }

        acessos = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            busca(tabela2, chaves[indice]);
        }

        float acessosSucessoMedia2 = (float) acessos / 350;
        acessos = 0;

        for (int indice = 0; indice < 350; indice++) {
            busca(tabela2, ausentes[indice]);
        }

        float acessosFalhaMedia2 = (float) acessos / 350;
        System.out.printf("EXPERIMENTO 2 (remocao com REMOVIDO): removidasComSucesso=%d falsosNegativos=%d ocupadas=%d removidas=%d vazias=%d acessosSucessoMedia=%.2f acessosFalhaMedia=%.2f%n",
                removidasComSucesso2, falsosNegativos2, contaOcupadas(tabela2), contaRemovidas(tabela2), contaVazias(tabela2), acessosSucessoMedia2, acessosFalhaMedia2);

        // Experimento 3: após reorganiza (usa a mesma tabela2 do Experimento 2)
        reorganiza(tabela2);
        int falsosNegativos3 = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            if (busca(tabela2, chaves[indice]) == -1) {
                falsosNegativos3++;
            }
        }

        acessos = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            busca(tabela2, chaves[indice]);
        }

        float acessosSucessoMedia3 = (float) acessos / 350;
        acessos = 0;
        for (int indice = 0; indice < 350; indice++) {
            busca(tabela2, ausentes[indice]);
        }

        float acessosFalhaMedia3 = (float) acessos / 350;
        System.out.printf("EXPERIMENTO 3 (apos reorganiza): falsosNegativos=%d ocupadas=%d removidas=%d vazias=%d acessosSucessoMedia=%.2f acessosFalhaMedia=%.2f%n",
                falsosNegativos3, contaOcupadas(tabela2), contaRemovidas(tabela2), contaVazias(tabela2), acessosSucessoMedia3, acessosFalhaMedia3);

        // Experimento 4: reinserção
        int[] tabela4 = criaTabela(1000);
        for (int indice = 0; indice < 700; indice++) {
            insere(tabela4, chaves[indice]);
        }

        for (int indice = 0; indice < 700; indice += 2) {
            remove(tabela4, chaves[indice]);
        }

        reaproveitamentos = 0;
        int inseridas4 = 0;

        for (int indice = 0; indice < 350; indice++) {
            if (insere(tabela4, ausentes[indice]) >= 0) {
                inseridas4++;
            }
        }

        int reaproveitamentos4 = reaproveitamentos;
        int falsosNegativos4 = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            if (busca(tabela4, chaves[indice]) == -1) {
                falsosNegativos4++;
            }
        }

        for (int indice = 0; indice < 350; indice++) {
            if (busca(tabela4, ausentes[indice]) == -1) {
                falsosNegativos4++;
            }
        }

        acessos = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            busca(tabela4, chaves[indice]);
        }

        for (int indice = 0; indice < 350; indice++) {
            busca(tabela4, ausentes[indice]);
        }

        float acessosSucessoMedia4 = (float) acessos / 700;
        System.out.printf("EXPERIMENTO 4 (reinsercao): inseridas=%d reaproveitamentos=%d falsosNegativos=%d ocupadas=%d removidas=%d vazias=%d acessosSucessoMedia=%.2f%n",
                inseridas4, reaproveitamentos4, falsosNegativos4, contaOcupadas(tabela4), contaRemovidas(tabela4), contaVazias(tabela4), acessosSucessoMedia4);
    }

    // Ponto de entrada do programa que executa os testes de validação e depois os experimentos
    public static void main(String[] args) {
        executaTestes();
        executaExperimentos();
    }
}