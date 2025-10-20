package view;

import controller.Controller;
import controller.Observer;
import model.ConfiguracaoSimulacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Index extends JFrame implements Observer {

    Tabela estrada;
    Controller controller;
    JPanelImage settings;
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints layoutConstraints = new GridBagConstraints();
    ArrayList<JComponent> menuCom = new ArrayList<>();

    JButton btControll;
    JLabel lblCar;
    JLabel lblVel;
    JLabel lvlCount;
    JLabel lblcarCount;
    JLabel lblTipo;
    JComboBox<String> matrizes;
    JSpinner tfVelInsercao;
    JSpinner tfCarro;
    JComboBox<String> cbTipo;

    public Index() {
        controller = Controller.getInstance();
        controller.addObserver(this);
        setTitle("Simulador de Tráfego em Malha Viária");
        setSize(1400, 825);
        setLayout(gbl);
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        buildPanels();
        setVisible(true);
        controller.notificarControle();
    }

    private void buildPanels() {
        layoutConstraints.gridx=0;
        layoutConstraints.gridy=0;
        layoutConstraints.weighty=0.1;


        settings = new JPanelImage("TrabalhoMalha/src/imagens/configs.png", 1300, 65);
        settings.setPreferredSize(new Dimension(settings.getWidth(),settings.getHeight()));
        settings.setOpaque(false);
        inializeMenu();
        add(settings,layoutConstraints);
        estrada = new Tabela();
        layoutConstraints.weighty=0.9;
        layoutConstraints.gridy=1;
        add(estrada,layoutConstraints);
        controller.notificarArquivo();
    }

    private void inializeMenu() {
        settings.setLayout(new GridBagLayout());
        GridBagConstraints mLayout = new GridBagConstraints();
        btControll = new JButton("Iniciar");
        btControll.setToolTipText("Inicia, pausa ou finaliza a simulação");
        matrizes = new JComboBox<>();
        matrizes.setToolTipText("Selecione a malha viária");
        lblCar = new JLabel("Quantidade de carros: ");
        lblVel = new JLabel("Delay de inserção: ");
        lblcarCount = new JLabel("Carros na malha:");
        lblTipo = new JLabel("Tipo: ");
        lvlCount = new JLabel("0");
        tfCarro = new JSpinner(new SpinnerNumberModel(1,1,200,1));
        tfCarro.setToolTipText("Máximo de veículos simultâneos");
        tfVelInsercao = new JSpinner(new SpinnerNumberModel(1,1,10,1));
        tfVelInsercao.setToolTipText("Intervalo de inserção de veículos (x100 ms)");
        cbTipo = new JComboBox<>(new String[]{"Semáforo", "Monitor"});
        cbTipo.setToolTipText("Escolha o mecanismo de exclusão mútua");

        menuCom.clear();
        menuCom.add(btControll);
        menuCom.add(matrizes);
        menuCom.add(lblCar);
        menuCom.add(tfCarro);
        menuCom.add(lblVel);
        menuCom.add(tfVelInsercao);
        menuCom.add(lblcarCount);
        menuCom.add(lvlCount);
        menuCom.add(lblTipo);
        menuCom.add(cbTipo);

        mLayout.gridx=0;
        mLayout.gridy=0;
        mLayout.insets = new Insets(0, 8, 0, 8);

        for(int i=0;i< menuCom.size();i++)
        {
            mLayout.gridx=i;
            mLayout.weightx=1.0;
            settings.add(menuCom.get(i), mLayout);
        }
        addActions();
    }

    private void addActions() {
        btControll.addActionListener((ActionEvent e) ->
        {
            if(controller.isStop())
            {
                // Cria o objeto de configuração com os dados da UI
                int qtdCarros = (int) tfCarro.getValue();
                int delay = (int) tfVelInsercao.getValue();
                int tipo = cbTipo.getSelectedIndex() + 1;
                String malha = controller.getCaminhos().get((String) matrizes.getSelectedItem());
                
                ConfiguracaoSimulacao config = new ConfiguracaoSimulacao(qtdCarros, delay, malha, tipo);
                
                // Passa o objeto para o Controller
                controller.iniciarSimulacao(config);
            }
            controller.getStateCarro().nextState();
        });
        matrizes.addActionListener((ActionEvent e) ->
        {
            controller.setArquivo(controller.getCaminhos().get((String) matrizes.getSelectedItem()));
            controller.getMatrizInstance().gerarMatriz(controller.getArquivo());
            estrada.draw();
        });
    }

    @Override
    public void updateCarro(Integer[][] posiao) {

    }

    @Override
    public void updateControle(boolean estado) {
        matrizes.setEnabled(estado);
        tfCarro.setEnabled(estado);
        tfVelInsercao.setEnabled(estado);
        cbTipo.setEnabled(estado);
        btControll.setEnabled(true);
        btControll.setText(controller.getStateCarro().getNextAction());
        if (!estado) {
            matrizes.setToolTipText("A simulação está em execução");
            tfCarro.setToolTipText("A simulação está em execução");
            tfVelInsercao.setToolTipText("A simulação está em execução");
            cbTipo.setToolTipText("A simulação está em execução");
        } else {
            matrizes.setToolTipText("Selecione a malha viária");
            tfCarro.setToolTipText("Máximo de veículos simultâneos");
            tfVelInsercao.setToolTipText("Intervalo de inserção de veículos (x100 ms)");
            cbTipo.setToolTipText("Escolha o mecanismo de exclusão mútua");
        }
    }

    @Override
    public void updateContador(int counter) {
        lvlCount.setText(counter + "");
    }

    @Override
    public void iniEstrada(String[] estrada) {
        for(String arquivo : estrada)
        {
           matrizes.addItem(arquivo);
        }
    }

    @Override
    public void reset() {

    }
}
