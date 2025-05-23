package model;

import java.util.concurrent.locks.ReentrantLock;

public abstract class Estrada {
    protected boolean saida = false;
    protected boolean entrada;
    protected Estrada proxima;
    protected int direcao;
    protected AbstractCarro carroSemaforo;
    protected int linha;
    protected int coluna;
    protected ReentrantLock lock = new ReentrantLock();

    public Estrada(int direcao, int linha, int coluna) {
        this.direcao=direcao;
        this.linha=linha;
        this.coluna=coluna;
        this.proxima=null;
    }

    public boolean isSaida() {
        return saida;
    }

    public void setSaida(boolean saida) {
        this.saida = saida;
    }

    public boolean isEntrada() {
        return entrada;
    }

    public void setEntrada(boolean entrada) {
        this.entrada = entrada;
    }

    public Estrada getProxima() {
        return proxima;
    }

    public void setProxima(Estrada[][] proxima) {
        if(direcao==1&&((linha-1)>=0))
            this.proxima=proxima[linha-1][coluna];
        if(direcao==2&&((coluna+1)<proxima[0].length))
            this.proxima=proxima[linha][coluna+1];
        if(direcao==3&&((linha+1)<proxima.length))
            this.proxima=proxima[linha+1][coluna];
        if(direcao==4&&((coluna-1)>=0))
            this.proxima=proxima[linha][coluna-1];
    }

    public int getDirecao() {
        return direcao;
    }

    public void setDirecao(int direcao) {
        this.direcao = direcao;
    }

    public AbstractCarro getCarro() {
        return carroSemaforo;
    }

    public void setCarro(AbstractCarro carroSemaforo) {
        this.carroSemaforo = carroSemaforo;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public boolean isCruzamento(){
        if(this.direcao>4)
            return true;
        return false;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}
