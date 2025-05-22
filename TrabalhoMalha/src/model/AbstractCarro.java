package model;

public abstract class AbstractCarro extends Thread{
    public abstract int getVelocidade();
    public abstract void setVelocidade();
    public abstract boolean isDesligado();
    public abstract void setDesligado(boolean desligado);
    public abstract Mutex getAtual();
    public abstract void setAtual(Mutex atual);
    public abstract boolean entrarMalha(Mutex entrada);
    public abstract void moverMalha();
    public abstract void run();
}
