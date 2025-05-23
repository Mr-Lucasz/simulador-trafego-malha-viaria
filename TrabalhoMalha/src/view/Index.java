package view;

import controller.Controller;
import controller.Observer;

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
    JSpinner tfTipo;

    public Index() {
        controller = Controller.getInstance();
        controller.addObserver(this);
        setTitle("Malha");
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
        matrizes = new JComboBox<>();
        lblCar = new JLabel("Quantidade de carros: ");
        lblVel = new JLabel("Delay de inserção: ");
        lblcarCount = new JLabel("Quantidade de carros:");
        lblTipo = new JLabel("Tipo: ");
        lvlCount = new JLabel("0");
        tfCarro = new JSpinner(new SpinnerNumberModel(1,1,200,1));
        tfVelInsercao = new JSpinner(new SpinnerNumberModel(1,1,10,1));
        tfTipo = new JSpinner(new SpinnerNumberModel(1,1,2,1));


        menuCom.add(btControll);
        menuCom.add(matrizes);
        menuCom.add(lblCar);
        menuCom.add(tfCarro);
        menuCom.add(lblVel);
        menuCom.add(tfVelInsercao);
        menuCom.add(lblcarCount);
        menuCom.add(lvlCount);
        menuCom.add(lblTipo);
        menuCom.add(tfTipo);

        mLayout.gridx=0;
        mLayout.gridy=0;

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
                controller.setQtdCarro((int) tfCarro.getValue());
                controller.setAwait((int) tfVelInsercao.getValue());
                controller.setTipo((int) tfTipo.getValue());
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
        btControll.setText(controller.getStateCarro().getNextAction());
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
