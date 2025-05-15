package controller;

import model.Carro;
import model.Direcoes;

import java.io.File;
import java.util.*;

public class Controller extends Thread{

    private final CriaMatriz matrizInstance = CriaMatriz.getInstance();
    private static Controller instance;
    private StateCarro state;
    private List<Observer> observers = new ArrayList<>();
    private Map<String, String> caminhos = new HashMap<>();
    private String arquivo = "src/malhas/malha-exemplo-1.txt";

    public Controller() {
        Direcoes.mapear();
        matrizInstance.gerarMatriz(arquivo);
        initMapa();
        state = new Finished(this);
        this.start();
    }

    public static Controller getInstance(){
        if(instance==null)
            instance = new Controller();
        return instance;
    }

    public void addObserver(Observer obs){
        this.observers.add(obs);
    }

    public void notificarMov(Integer [][] posicao){
        for(Observer obs: observers)
            obs.updateCarro(posicao);
    }

    public void notificarControle(){
        for(Observer obs: observers)
            obs.updateControle(isStop());
    }

    public void notificarContador(){
        for(Observer obs:observers)
            obs.updateContador(carros.size());
    }

    public void notificarArquivo(){
        Set<String> chave = caminhos.keySet();
        for(Observer obs:observers)
            obs.iniEstrada(chave.toArray(new String[chave.size()]));
    }

    public void nitifcarReset(){
        for(Observer obs:observers)
            obs.reset();
    }

    private void initMapa() {
        File pasta = new File("src/malhas");
        File[] malhas = pasta.listFiles();
        String[] nomeMalha = new String[malhas.length];
        for(int i=0;i<malhas.length;i++)
        {
            if(malhas[i].isFile())
            {
                nomeMalha[i]="Malha "+i;
                caminhos.put(nomeMalha[i],malhas[i].getPath());
            }
        }
    }

    private List<Carro> carros = new ArrayList<>();
    private int qtdCarro;
    private int await;
    private boolean stop = true;

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public int getAwait() {
        return await;
    }

    public void setAwait(int await) {
        this.await = await*100;
    }

    public int getQtdCarro() {
        return qtdCarro;
    }

    public void setQtdCarro(int qtdCarro) {
        this.qtdCarro = qtdCarro;
    }

    public List<Carro> getCarros() {
        return carros;
    }

    public StateCarro getStateCarro(){
        return state;
    }

    public void setStateCarro(StateCarro state){
        this.state=state;
    }

    public CriaMatriz getMatrizInstance() {
        return matrizInstance;
    }

    public Map<String, String> getCaminhos() {
        return caminhos;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public void await(){
        try {
            sleep(await);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true){
            state.execute();
        }
    }
}
