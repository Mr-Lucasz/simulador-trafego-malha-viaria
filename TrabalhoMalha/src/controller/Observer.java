package controller;

public interface Observer {
    void updateCarro(Integer [][] posiao);
    void updateControle(boolean estado);
    void updateContador(int counter);
    void iniEstrada(String[] estrada);
    void reset();
}
