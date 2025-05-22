package model;

import controller.Controller;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CarroSemaforo extends AbstractCarro{
    private int velocidade;
    private boolean desligado;
    private Mutex atual;
    private Controller controller;

    public CarroSemaforo(Controller controller) {
        this.controller = Controller.getInstance();
        setVelocidade();
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade() {
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
        System.out.println("socorro");
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
        controller.notificarMov(positions);
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
        controller.notificarMov(posicao);
        atual.getSemaphore().release();
        atual.setCarro(null);
        controller.getCarros().remove(this);
        controller.notificarContador();
    }

    private List<Mutex> mapearCruzamento() throws InterruptedException {
        Map<Integer, Mutex> opcoes = new HashMap<>();
        int i=atual.getLinha();
        int j=atual.getColuna();

        switch (atual.getDirecao())
        {
            case 1:
                opcoes.put(0,controller.getMatrizInstance().getEstrada(i-1,j));
                opcoes.put(1, controller.getMatrizInstance().getEstrada(i-2,j));
                opcoes.put(2,controller.getMatrizInstance().getEstrada(i-2,j-1));
                opcoes.put(3,controller.getMatrizInstance().getEstrada(i-1,j-1));
                break;
            case 2:
                opcoes.put(0,controller.getMatrizInstance().getEstrada(i,j+1));
                opcoes.put(1, controller.getMatrizInstance().getEstrada(i,j+2));
                opcoes.put(2,controller.getMatrizInstance().getEstrada(i-1,j+2));
                opcoes.put(3,controller.getMatrizInstance().getEstrada(i-1,j+1));
                break;
            case 3:
                opcoes.put(0,controller.getMatrizInstance().getEstrada(i+1,j));
                opcoes.put(1, controller.getMatrizInstance().getEstrada(i+2,j));
                opcoes.put(2,controller.getMatrizInstance().getEstrada(i+2,j+1));
                opcoes.put(3,controller.getMatrizInstance().getEstrada(i+1,j+1));
                break;
            case 4:
                opcoes.put(0,controller.getMatrizInstance().getEstrada(i,j-1));
                opcoes.put(1, controller.getMatrizInstance().getEstrada(i,j-2));
                opcoes.put(2,controller.getMatrizInstance().getEstrada(i+1,j-2));
                opcoes.put(3,controller.getMatrizInstance().getEstrada(i+1,j-1));
                break;
        }
        List<Mutex> opcoesSaida = new ArrayList<>();
        for(Mutex opcao: opcoes.values())
        {
            switch (opcao.getDirecao())
            {
                case 9:
                    Mutex d = controller.getMatrizInstance().getEstrada(opcao.getLinha(),opcao.getColuna()+1);
                    if(d.getDirecao()==2)
                        opcoesSaida.add(d);
                    break;
                case 10:
                    Mutex c = controller.getMatrizInstance().getEstrada(opcao.getLinha()-1,opcao.getColuna());
                    if(c.getDirecao()==1)
                        opcoesSaida.add(c);
                    break;
                case 11:
                    Mutex b = controller.getMatrizInstance().getEstrada(opcao.getLinha()+1,opcao.getColuna());
                    if(b.getDirecao()==3)
                        opcoesSaida.add(b);
                    break;
                case 12:
                    Mutex e = controller.getMatrizInstance().getEstrada(opcao.getLinha(),opcao.getColuna()-1);
                    if(e.getDirecao()==2)
                        opcoesSaida.add(e);
                    break;
            }
        }

        Mutex escolhido = opcoesSaida.get(new Random().nextInt(opcoesSaida.size()));
        List<Mutex> caminho = new ArrayList<>();
        boolean pegarCaminho = escolhido.getSemaphore().tryAcquire(100, TimeUnit.MILLISECONDS);
        if(pegarCaminho)
        {
            escolhido.setCarro(this);
            if(atual.getDirecao()-escolhido.getDirecao()==0)
            {
                for(int r=0;r<2;r++)
                    caminho.add(opcoes.get(r));
            }
            if(Math.abs(atual.getDirecao()-escolhido.getDirecao())==2)
            {
                for(int r=0;r<4;r++)
                    caminho.add(opcoes.get(r));
            }
            if(atual.getDirecao()-escolhido.getDirecao()==-1 || atual.getDirecao()-escolhido.getDirecao()==3)
            {
                caminho.add(opcoes.get(0));
            }
            if(atual.getDirecao()-escolhido.getDirecao()==1 || atual.getDirecao()-escolhido.getDirecao()==-3)
            {
                for(int r=0;r<3;r++)
                    caminho.add(opcoes.get(r));
            }
            for(Mutex caminho1 : caminho)
            {
                List<Semaphore> acquired = new ArrayList<>();
                acquired.add(caminho1.getSemaphore());
                boolean b = caminho1.getSemaphore().tryAcquire(100, TimeUnit.MILLISECONDS);
                if(!b)
                {
                    escolhido.getSemaphore().release();
                    for(Semaphore semaphore : acquired)
                        semaphore.release();
                    Thread.currentThread().sleep(velocidade);
                    return null;
                }
            }
            caminho.add(escolhido);
            return caminho;
        }
        Thread.currentThread().sleep(velocidade);
        return null;
    }

    private void moverCruzamento(){
        List<Mutex> caminhoA = null;
        try {
            caminhoA=mapearCruzamento();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(caminhoA!=null)
        {
            for(int i=0;i<caminhoA.size();i++)
            {
                Mutex caminho = caminhoA.get(i);
                Integer[][] posicoes =
                        {
                                {atual.getLinha(), atual.getColuna()},
                                {caminho.getLinha(),caminho.getColuna()}
                        };
                controller.notificarMov(posicoes);
                atual.getSemaphore().release();
                atual.setCarro(null);
                atual=caminho;
                if(i<caminhoA.size()-1)
                {
                    try {
                        Thread.currentThread().sleep(velocidade);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    @Override
    public void run() {
        while (!desligado){
            try {
                Thread.currentThread().sleep(velocidade);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(atual.isSaida())
                sairMalha();
            else
                if(atual.getProxima().isCruzamento())
                    moverCruzamento();
                else
                    moverMalha();
        }
        sairMalha();
        System.out.println("yipeee");
    }
}
