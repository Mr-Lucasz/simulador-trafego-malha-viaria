package model;

import java.util.concurrent.Semaphore;

public class Mutex extends Estrada{

    private Semaphore semaphore = new Semaphore(1);

    public Mutex(int direcao, int linha, int coluna) {
        super(direcao, linha, coluna);
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
