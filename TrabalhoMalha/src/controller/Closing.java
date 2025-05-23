package controller;

import model.AbstractCarro;

import java.util.List;

public class Closing extends StateCarro {

    public Closing(Controller controller) {
        super(controller);
    }

    @Override
    public void execute() {
        controller.await();
        if (controller.getCarros().isEmpty())
            nextState();
    }

    @Override
    public void nextState() {
        ThreadKiller tk = new ThreadKiller(controller.getCarros());
        tk.start();
        try {
            tk.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        controller.getCarros().clear();
        controller.getMatrizInstance().gerarMatriz(controller.getArquivo());
        controller.setStateCarro(new Finished(controller));
        controller.notificarContador();
        controller.nitifcarReset();
        controller.notificarControle();
    }

    @Override
    public String getNextAction() {
        return "FINALIZAR";
    }

    class ThreadKiller extends Thread {
        List<AbstractCarro> cars;

        public ThreadKiller(List<AbstractCarro> cars) {
            this.cars = cars;
        }

        @Override
        public void run() {
            for (AbstractCarro carro : cars) {
                carro.setDesligado(true);
            }
            for (AbstractCarro carro : cars) {
                try {
                    carro.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
