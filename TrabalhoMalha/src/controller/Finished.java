package controller;

public class Finished extends StateCarro {

    public Finished(Controller controller) {
        super(controller);
    }

    @Override
    public void execute() {
        controller.await();
    }

    @Override
    public void nextState() {
        controller.setStop(false);
        controller.setStateCarro(new  Started(controller));
        controller.notificarControle();
    }

    @Override
    public String getNextAction() {
        return "INICIAR";
    }
}
