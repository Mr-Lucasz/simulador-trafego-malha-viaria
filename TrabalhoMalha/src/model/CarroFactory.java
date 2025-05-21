package model;

import controller.Controller;

public interface CarroFactory {
    AbstractCarro criarCarro(Controller controller);
}
