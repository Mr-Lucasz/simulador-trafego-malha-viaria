package model;

import controller.Controller;

public class SemaforoCarroFactory implements CarroFactory{

    @Override
    public AbstractCarro criarCarro(Controller controller) {
        return new CarroSemaforo(controller);
    }
}
