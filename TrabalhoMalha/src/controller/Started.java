package controller;

import model.Carro;
import model.Mutex;

public class Started extends StateCarro {

    public Started(Controller controller) {
        super(controller);
    }

    @Override
    public void execute() {
        if(!controller.isStop()){
            inicializarEstrada();
        }
    }

    private void inicializarEstrada() {
        int i=0;
        while(!controller.isStop())
        {
            if(controller.getCarros().size()<controller.getQtdCarro())
            {
                Carro novoCarro = new Carro(controller);
                Mutex entrada = controller.getMatrizInstance().getEntradas().get(i);
                Integer[][] posicoes = {{null,null}, {entrada.getLinha(),entrada.getColuna()}};
                if(novoCarro.entrarMalha(entrada))
                {
                    controller.getCarros().add(novoCarro);
                    controller.notificarContador();
                    controller.notificarMov(posicoes);
                    novoCarro.start();
                }
                else
                    System.out.println("n entrou");
                i++;
                if(i==controller.getMatrizInstance().getEntradas().size())
                    i=0;
                controller.await();
            }
            if(controller.getCarros().size()== controller.getQtdCarro())
                controller.await();
        }
    }

    @Override
    public void nextState() {
        controller.setStop(true);
        controller.setStateCarro(new Closing(controller));
        controller.notificarControle();
    }

    @Override
    public String getNextAction() {
        return "PARAR";
    }

}
