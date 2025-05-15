package model;

import java.util.HashMap;
import java.util.Map;

public enum Direcoes {
    semEstrada(0, "imagens/semEstrada.png"),
    estradaCima(1, "imagens/estradaCima.png"),
    estradaDireita(2, "imagens/estradaDir.png"),
    estradaBaixo(3, "imagens/estradaBaixo.png"),
    estradaEsquerda(4, "imagens/estradaEsq.png"),
    cruzamentoC(5, "imagens/cruzamento.png"),
    cruzamentoD(6, "imagens/cruzamento.png"),
    cruzamentoB(7,"imagens/cruzamento.png"),
    cruzamentoE(8,"imagens/cruzamento.png"),
    cruzamentoCD(9, "imagens/cruzamento.png"),
    cruzamentoCE(10, "imagens/cruzamento.png"),
    cruzamentoBD(11, "imagens/cruzamento.png"),
    cruzamentoBE(12,"imagens/cruzamento.png");

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
