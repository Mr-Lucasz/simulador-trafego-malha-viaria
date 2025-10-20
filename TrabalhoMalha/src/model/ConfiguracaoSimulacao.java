package model;

/**
 * Classe responsável por agrupar os parâmetros de configuração da simulação.
 * Extraída da classe Controller para melhorar a coesão e reduzir o acoplamento.
 */
public class ConfiguracaoSimulacao {
    private int quantidadeCarros;
    private int delayInsercaoMs;
    private String arquivoMalha;
    private int tipoExclusaoMutua; // 1: Semáforo, 2: Monitor

    /**
     * Construtor da configuração da simulação.
     * 
     * @param quantidadeCarros Número máximo de carros simultâneos
     * @param delayInsercao Delay de inserção em centésimos de segundo (será convertido para ms)
     * @param arquivoMalha Caminho para o arquivo da malha viária
     * @param tipo Tipo de exclusão mútua (1: Semáforo, 2: Monitor)
     */
    public ConfiguracaoSimulacao(int quantidadeCarros, int delayInsercao, 
                                  String arquivoMalha, int tipo) {
        this.quantidadeCarros = quantidadeCarros;
        this.delayInsercaoMs = delayInsercao * 100; // Converte para milissegundos
        this.arquivoMalha = arquivoMalha;
        this.tipoExclusaoMutua = tipo;
    }

    // Getters
    public int getQuantidadeCarros() { 
        return quantidadeCarros; 
    }
    
    public int getDelayInsercaoMs() { 
        return delayInsercaoMs; 
    }
    
    public String getArquivoMalha() { 
        return arquivoMalha; 
    }
    
    public int getTipoExclusaoMutua() { 
        return tipoExclusaoMutua; 
    }
}
