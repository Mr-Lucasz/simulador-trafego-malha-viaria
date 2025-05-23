package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.*;

public class CarroView extends JPanelImage {
    private JLabel carro;

    public CarroView(String caminho, int largura, int altura) {
        super(caminho, largura, altura);
        setLayout(new GridBagLayout());
    }

    public JLabel getCarro() {
        return carro;
    }

    public void setCarro(JLabel carro) {
        this.carro = carro;
    }

    public void addCarro(){
        if(carro==null)
        {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            int tamanho = (int) (this.getWidth() * 0.6);
            ImageIcon icon = new ImageIcon(new ImageIcon("TrabalhoMalha/src/imagens/carro.png").
                    getImage().getScaledInstance(tamanho, tamanho, Image.SCALE_SMOOTH));
            JLabel carrinho = new JLabel(icon);
            add(carrinho, gridBagConstraints);
            setCarro(carrinho);
            repaint();
            revalidate();
        }
    }
    public void reset(){
        if(carro!=null)
        {
            remove(carro);
        }
        setCarro(null);
        repaint();
        revalidate();
    }
}
