package controller;

public abstract class StateCarro {
    Controller controller;

    public StateCarro(Controller controller){
        this.controller=controller;
    }

    public abstract void execute();
    public abstract void nextState();
    public abstract String getNextAction();
}
