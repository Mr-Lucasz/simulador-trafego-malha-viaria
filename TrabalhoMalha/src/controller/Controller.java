package controller;

import model.AbstractCarro;
import model.ConfiguracaoSimulacao;
import model.Direcoes;

import java.io.File;
import java.util.*;

public class Controller extends Thread{

    private final CriaMatriz matrizInstance = CriaMatriz.getInstance();
    private static Controller instance;
    private StateCarro state;
    private ConfiguracaoSimulacao configuracao;
    private List<Observer> observers = new ArrayList<>();
    private Map<String, String> caminhos = new HashMap<>();

    public Controller() {
        Direcoes.mapear();
        matrizInstance.gerarMatriz("TrabalhoMalha/src/malhas/malha-exemplo-1.txt");
        initMapa();
        state = new Finished(this);
        this.start();
    }

    /**
     * Inicia a simulação com a configuração fornecida.
     * 
     * @param config Configuração da simulação
     */
    public void iniciarSimulacao(ConfiguracaoSimulacao config) {
        this.configuracao = config;
        this.getStateCarro().nextState();
    }

    public int getTipo() {
        return (configuracao != null) ? configuracao.getTipoExclusaoMutua() : 0;
    }

    public void setTipo(int tipo) {
        // Mantido para compatibilidade com código existente
        // Em uma refatoração mais avançada, este método poderia ser removido
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

        File pasta = new File("TrabalhoMalha/src/malhas");
        File[] malhas = pasta.listFiles();
        if (malhas == null) {
            System.err.println("Diretório de malhas não encontrado: " + pasta.getAbsolutePath());
            return;
        }

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

    private List<AbstractCarro> carros = new ArrayList<>();
    private boolean stop = true;

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public int getAwait() {
        return (configuracao != null) ? configuracao.getDelayInsercaoMs() : 0;
    }

    public void setAwait(int await) {
        // Mantido para compatibilidade com código existente
        // Em uma refatoração mais avançada, este método poderia ser removido
    }

    public int getQtdCarro() {
        return (configuracao != null) ? configuracao.getQuantidadeCarros() : 0;
    }

    public void setQtdCarro(int qtdCarro) {
        // Mantido para compatibilidade com código existente
        // Em uma refatoração mais avançada, este método poderia ser removido
    }

    public List<AbstractCarro> getCarros() {
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
        return (configuracao != null) ? configuracao.getArquivoMalha() : "TrabalhoMalha/src/malhas/malha-exemplo-1.txt";
    }

    public void setArquivo(String arquivo) {
        // Mantido para compatibilidade com código existente
        // Em uma refatoração mais avançada, este método poderia ser removido
    }

    public void await(){
        try {
            sleep(getAwait());
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
