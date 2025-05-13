package model;

import java.util.Random;

public class Carro extends Thread{
    private int velocidade;
    private boolean desligado;
    private Mutex atual;

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        Random r = new Random();
        this.velocidade = (int) (1+r.nextFloat()*(10-1)*100)+375;
    }

    public boolean isDesligado() {
        return desligado;
    }

    public void setDesligado(boolean desligado) {
        this.desligado = desligado;
    }

    public Mutex getAtual() {
        return atual;
    }

    public void setAtual(Mutex atual) {
        this.atual = atual;
    }

    public boolean entrarMalha(Mutex entrada){
        boolean teste = entrada.getSemaphore().tryAcquire();
        if(teste)
        {
            entrada.setCarro(this);
            this.setAtual(entrada);
        }
        return teste;
    }

    public void moverMalha()
    {
        Mutex proximo = (Mutex) atual.getProxima();
        try
        {
            proximo.getSemaphore().acquire();
            proximo.setCarro(this);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        Integer [][] positions =
        {
            {atual.getLinha(),atual.getColuna()},
            {proximo.getLinha(), proximo.getColuna()}
        };
        atual.getSemaphore().release();
        atual.setCarro(null);
        atual=proximo;
    }
    private void sairMalha(){
        desligado = true;
        Integer[][] posicao =
        {
            {atual.getLinha(), atual.getColuna()},
            {null, null}
        };
        atual.getSemaphore().release();
        atual.setCarro(null);
    }

}
