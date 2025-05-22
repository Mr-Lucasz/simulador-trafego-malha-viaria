package model;

import java.util.HashMap;
import java.util.Map;

public enum Direcoes {
    semEstrada(0, "src/imagens/semEstrada.png"),
    estradaCima(1, "src/imagens/estradaCima.png"),
    estradaDireita(2, "src/imagens/estradaDir.png"),
    estradaBaixo(3, "src/imagens/estradaBaixo.png"),
    estradaEsquerda(4, "src/imagens/estradaEsq.png"),
    cruzamentoC(5, "src/imagens/cruzamento.png"),
    cruzamentoD(6, "src/imagens/cruzamento.png"),
    cruzamentoB(7,"src/imagens/cruzamento.png"),
    cruzamentoE(8,"src/imagens/cruzamento.png"),
    cruzamentoCD(9, "src/imagens/cruzamento.png"),
    cruzamentoCE(10, "src/imagens/cruzamento.png"),
    cruzamentoBD(11, "src/imagens/cruzamento.png"),
    cruzamentoBE(12,"src/imagens/cruzamento.png");

    private int valor;
    private String local;
    private static Map mapa = new HashMap<Integer, String>();

    Direcoes(int valor, String local) {
        this.valor = valor;
        this.local = local;
    }

    public static String getLocal(int valor) {
        return mapa.get(valor).toString();
    }

    public int getValor() {
        return valor;
    }
    public static void mapear(){
        for(Direcoes direcao : Direcoes.values())
        {
            mapa.put(direcao.valor, direcao.local);
        }
    }
}
