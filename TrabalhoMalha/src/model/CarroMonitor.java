package model;

import controller.Controller;

import java.util.*;

public class CarroMonitor extends AbstractCarro {
    private int velocidade;
    private boolean desligado;
    private Controller controller;
    private Estrada atual;

    public CarroMonitor(Controller controller) {
        this.controller = Controller.getInstance();
        setVelocidade();
    }

    @Override
    public int getVelocidade() {
        return velocidade;
    }

    @Override
    public void setVelocidade() {
        Random r = new Random();
        this.velocidade = (int) (1 + r.nextFloat() * (10 - 1) * 100) + 375;
    }

    @Override
    public boolean isDesligado() {
        return desligado;
    }

    @Override
    public void setDesligado(boolean desligado) {
        this.desligado = desligado;
    }

    @Override
    public Mutex getAtual() {
        return null;
    }

    @Override
    public void setAtual(Mutex atual) {

    }

    @Override
    public synchronized boolean entrarMalha(Mutex entrada) {
        atual = entrada;
        atual.setCarro(this);

        return true;
    }

    @Override
    public synchronized void moverMalha() {
        Estrada proximo = atual.getProxima();
        proximo.setCarro(this);
        Integer[][] positions = {
                { atual.getLinha(), atual.getColuna() },
                { proximo.getLinha(), proximo.getColuna() }
        };
        controller.notificarMov(positions);
        atual.setCarro(null);
        atual = proximo;
    }

    private synchronized void sairMalha() {
        desligado = true;
        Integer[][] posicao = {
                { atual.getLinha(), atual.getColuna() },
                { null, null }
        };
        controller.notificarMov(posicao);
        atual.setCarro(null);
        controller.getCarros().remove(this);
        controller.notificarContador();
    }

    private synchronized List<Estrada> mapearCruzamento() throws InterruptedException {
        Map<Integer, Estrada> opcoes = new HashMap<>();
        int i = atual.getLinha();
        int j = atual.getColuna();

        switch (atual.getDirecao()) {
            case 1:
                opcoes.put(0, controller.getMatrizInstance().getEstrada(i - 1, j));
                opcoes.put(1, controller.getMatrizInstance().getEstrada(i - 2, j));
                opcoes.put(2, controller.getMatrizInstance().getEstrada(i - 2, j - 1));
                opcoes.put(3, controller.getMatrizInstance().getEstrada(i - 1, j - 1));
                break;
            case 2:
                opcoes.put(0, controller.getMatrizInstance().getEstrada(i, j + 1));
                opcoes.put(1, controller.getMatrizInstance().getEstrada(i, j + 2));
                opcoes.put(2, controller.getMatrizInstance().getEstrada(i - 1, j + 2));
                opcoes.put(3, controller.getMatrizInstance().getEstrada(i - 1, j + 1));
                break;
            case 3:
                opcoes.put(0, controller.getMatrizInstance().getEstrada(i + 1, j));
                opcoes.put(1, controller.getMatrizInstance().getEstrada(i + 2, j));
                opcoes.put(2, controller.getMatrizInstance().getEstrada(i + 2, j + 1));
                opcoes.put(3, controller.getMatrizInstance().getEstrada(i + 1, j + 1));
                break;
            case 4:
                opcoes.put(0, controller.getMatrizInstance().getEstrada(i, j - 1));
                opcoes.put(1, controller.getMatrizInstance().getEstrada(i, j - 2));
                opcoes.put(2, controller.getMatrizInstance().getEstrada(i + 1, j - 2));
                opcoes.put(3, controller.getMatrizInstance().getEstrada(i + 1, j - 1));
                break;
        }
        List<Estrada> opcoesSaida = new ArrayList<>();
        for (Estrada opcao : opcoes.values()) {
            switch (opcao.getDirecao()) {
                case 9:
                    Estrada d = controller.getMatrizInstance().getEstrada(opcao.getLinha(), opcao.getColuna() + 1);
                    if (d.getDirecao() == 2)
                        opcoesSaida.add(d);
                    break;
                case 10:
                    Estrada c = controller.getMatrizInstance().getEstrada(opcao.getLinha() - 1, opcao.getColuna());
                    if (c.getDirecao() == 1)
                        opcoesSaida.add(c);
                    break;
                case 11:
                    Estrada b = controller.getMatrizInstance().getEstrada(opcao.getLinha() + 1, opcao.getColuna());
                    if (b.getDirecao() == 3)
                        opcoesSaida.add(b);
                    break;
                case 12:
                    Estrada e = controller.getMatrizInstance().getEstrada(opcao.getLinha(), opcao.getColuna() - 1);
                    if (e.getDirecao() == 2)
                        opcoesSaida.add(e);
                    break;
            }
        }

        Estrada escolhido = opcoesSaida.get(new Random().nextInt(opcoesSaida.size()));
        List<Estrada> caminho = new ArrayList<>();
        escolhido.setCarro(this);
        if (atual.getDirecao() - escolhido.getDirecao() == 0) {
            for (int r = 0; r < 2; r++)
                caminho.add(opcoes.get(r));
        }
        if (Math.abs(atual.getDirecao() - escolhido.getDirecao()) == 2) {
            for (int r = 0; r < 4; r++)
                caminho.add(opcoes.get(r));
        }
        if (atual.getDirecao() - escolhido.getDirecao() == -1 || atual.getDirecao() - escolhido.getDirecao() == 3) {
            caminho.add(opcoes.get(0));
        }
        if (atual.getDirecao() - escolhido.getDirecao() == 1 || atual.getDirecao() - escolhido.getDirecao() == -3) {
            for (int r = 0; r < 3; r++)
                caminho.add(opcoes.get(r));
        }
        caminho.add(escolhido);
        return caminho;
    }

    private void moverCruzamento() {
        List<Estrada> caminhoA = null;
        try {
            caminhoA = mapearCruzamento();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (caminhoA != null && !caminhoA.isEmpty()) {
            boolean atravessou = false;
            Random rand = new Random();
            while (!atravessou) {
                List<Estrada> acquired = new ArrayList<>();
                boolean conseguiuTodos = true;
                try {
                    for (Estrada pos : caminhoA) {
                        if (!pos.getLock().tryLock(100, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                            conseguiuTodos = false;
                            break;
                        } else {
                            acquired.add(pos);
                        }
                    }
                    if (conseguiuTodos) {
                        // Marca todas as posições como ocupadas
                        for (Estrada pos : caminhoA) {
                            pos.setCarro(this);
                        }
                        // Faz a travessia
                        for (int i = 0; i < caminhoA.size(); i++) {
                            Estrada caminho = caminhoA.get(i);
                            Integer[][] posicoes = {
                                    { atual.getLinha(), atual.getColuna() },
                                    { caminho.getLinha(), caminho.getColuna() }
                            };
                            controller.notificarMov(posicoes);
                            atual.setCarro(null);
                            atual = caminho;
                            if (i < caminhoA.size() - 1) {
                                try {
                                    Thread.sleep(velocidade);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        // Libera todas as posições do caminho
                        for (Estrada pos : caminhoA) {
                            pos.setCarro(null);
                        }
                        atravessou = true;
                    } else {
                        // Não conseguiu todos, libera os já adquiridos
                        for (Estrada pos : acquired) {
                            pos.getLock().unlock();
                        }
                        try {
                            Thread.sleep(100 + rand.nextInt(400));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    // Garante que todos os locks sejam liberados em caso de erro
                    for (Estrada pos : acquired) {
                        if (pos.getLock().isHeldByCurrentThread()) {
                            pos.getLock().unlock();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while (!desligado) {
            try {
                Thread.currentThread().sleep(velocidade);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (atual.isSaida())
                sairMalha();
            else if (atual.getProxima().isCruzamento())
                moverCruzamento();
            else
                moverMalha();
        }
        sairMalha();
        System.out.println("yipeee");
    }
}
