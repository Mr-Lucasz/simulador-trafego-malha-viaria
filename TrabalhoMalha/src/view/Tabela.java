package view;

import controller.Controller;
import controller.Observer;
import model.Direcoes;

import javax.swing.*;
import java.awt.*;

public class Tabela extends JPanel implements Observer {
    @Override
    public void updateCarro(Integer[][] posiao) {
        Integer antigaPos[] = posiao[0];
        Integer novaPos[] = posiao[1];

        if(antigaPos[0]!=null && antigaPos[1]!=null)
            carroViews[antigaPos[0]][antigaPos[1]].reset();
        if(novaPos[0]!=null && novaPos[1]!=null)
            carroViews[novaPos[0]][novaPos[1]].addCarro();
    }

    @Override
    public void updateControle(boolean estado) {

    }

    @Override
    public void updateContador(int counter) {

    }

    @Override
    public void iniEstrada(String[] estrada) {

    }

    @Override
    public void reset() {
        draw();
    }
    private final int size = 575;
    private int cellSize;
    private int rows;
    private int cols;
    private Controller c;
    private CarroView[][] carroViews;

    public Tabela() {
        this.c = Controller.getInstance();
        c.addObserver(this);
        setOpaque(false);
        draw();
    }
    public void draw(){
        rows = c.getMatrizInstance().getLinhas();
        cols = c.getMatrizInstance().getColunas();
        carroViews = new CarroView[rows][cols];
        setLayout(new GridLayout(rows, cols));
        cellSize = size / rows;
        setPreferredSize(new Dimension(cellSize * (int) Math.round(cols * 1.2), cellSize * (int) Math.round(rows * 1.2)));

        this.removeAll();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                String p = Direcoes.getLocal(c.getMatrizInstance().getDirecao(i,j));
                CarroView cell = new CarroView(p, cellSize, cellSize);
                carroViews[i][j] = cell;
                this.add(cell);
            }
        }

        this.revalidate();
    }
}
