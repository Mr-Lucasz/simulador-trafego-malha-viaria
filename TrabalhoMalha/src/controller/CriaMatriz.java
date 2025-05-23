package controller;

import model.Direcoes;
import model.Estrada;
import model.Mutex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CriaMatriz {
    private static CriaMatriz instance;

    public static  CriaMatriz getInstance()
    {
        if(instance==null)
            instance = new CriaMatriz();
        return instance;
    }

    public CriaMatriz() {
    }

    private Mutex[][] matriz;
    List<Mutex> entradas = new ArrayList<>();
    List<Mutex> saidas = new ArrayList<>();

    public Mutex[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(Mutex[][] matriz) {
        this.matriz = matriz;
    }

    public List<Mutex> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Mutex> entradas) {
        this.entradas = entradas;
    }

    public List<Mutex> getSaidas() {
        return saidas;
    }

    public void setSaidas(List<Mutex> saidas) {
        this.saidas = saidas;
    }

    public void gerarMatriz(String caminho)
    {
        Path path = Paths.get(caminho);
        List<String> list = new ArrayList<>();
        try {
            list = Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        matriz = new Mutex[Integer.parseInt(list.get(0))][Integer.parseInt(list.get(1))];
        StringBuilder str = new StringBuilder();

        for(int i=2;i<list.size();i++)
        {
            String[] linha = list.get(i).split("\t");
            for(int j=0;j<linha.length;j++)
            {
                matriz[i-2][j] = new Mutex(Integer.parseInt(linha[j]), i-2,j);
                str.append(linha[j]+" ");
            }
            str.append("\n");
        }
        for(int i=0;i<matriz.length;i++)
        {
            for(int j=0;j<matriz[i].length;j++)
                matriz[i][j].setProxima(matriz);
        }
        gerarEntradas();
        gerarSaidas();
    }

    public int getDirecao(int i, int j){
        return matriz[i][j].getDirecao();
    }

    public void gerarSaidas() {
        saidas.clear();
        for(int i=0;i< matriz.length;i++)
        {
            for(int j=0;j<matriz[i].length;j++)
            {
                if (getDirecao(i, j) > 0 && getDirecao(i, j) < 5 && matriz[i][j].getProxima() == null)
                {
                    matriz[i][j].setSaida(true);
                    saidas.add(matriz[i][j]);
                }
            }
        }
        System.out.println("saidas");
        for(Estrada saida:saidas)
            System.out.println(saida.getLinha()+" "+saida.getColuna());
    }

    public void gerarEntradas(){
        entradas.clear();
        for(int i=matriz.length-1;i<matriz.length;i++)
        {
            for(int j=0;j<matriz[i].length;j++)
            {
                if(this.getDirecao(i,j)== Direcoes.estradaCima.getValor())
                    entradas.add(matriz[i][j]);
            }
        }

        for(int i=0;i<1;i++)
        {
            for(int j=0;j<matriz[i].length;j++)
            {
                if(this.getDirecao(i,j)== Direcoes.estradaBaixo.getValor())
                    entradas.add(matriz[i][j]);
            }
        }

        for(int i=0;i<matriz.length;i++)
        {
            for(int j=0;j<1;j++)
            {
                if(this.getDirecao(i,j)== Direcoes.estradaDireita.getValor())
                    entradas.add(matriz[i][j]);
            }
        }
        for(int i=0;i<matriz.length;i++)
        {
            for(int j=matriz[i].length-1;j<matriz[i].length;j++)
            {
                if(this.getDirecao(i,j)== Direcoes.estradaEsquerda.getValor())
                    entradas.add(matriz[i][j]);
            }
        }

        Collections.shuffle(getEntradas());
        System.out.println("Entradas");
        for(Estrada entrada : entradas)
        {
            System.out.println(entrada.getLinha()+" "+entrada.getColuna());
        }
    }

    public int getLinhas(){
        return matriz.length;
    }

    public int getColunas(){
        return  matriz[0].length;
    }

    public Mutex getEstrada(int i, int j)
    {
        if(i<=getLinhas()&&j<=getColunas())
            return matriz[i][j];
        return null;
    }

}
