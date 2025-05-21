package model;

import controller.Controller;

public class MonitorCarroFactory implements CarroFactory{
    @Override
    public AbstractCarro criarCarro(Controller controller) {
        return new CarroMonitor(controller);
    }
}
