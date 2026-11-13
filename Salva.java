import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Salva {

    public static void salvar(List<String> end) {
        String cabecalho = "SITUACAO;MATRICULA;LOCAL;NOME;CARGO;CATEGORIA;HOR;NF;ND;DT_ADM;SIN;OPT;SALARIO";

        try {
            File pastaSaida = DiretoriosProjeto.obterDiretorio("Cruzamento_Dados");
            JFileChooser chooser = new JFileChooser(pastaSaida);
            chooser.setDialogTitle("Escolha a pasta para salvar");
            chooser.setSelectedFile(new File(pastaSaida, "Cruzamento_Dados.csv"));

            int opcao = chooser.showSaveDialog(null);
            if (opcao != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "Operação cancelada!");
                return;
            }
            File arquivo = chooser.getSelectedFile();

            if (!arquivo.getName().toLowerCase().endsWith(".csv")) {
                arquivo = new File(arquivo.getParentFile(), arquivo.getName() + ".csv");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
                writer.write(cabecalho);
                writer.newLine();
                for (String linha : end) {
                    writer.write(linha);
                    writer.newLine();
                }
            }
            JOptionPane.showMessageDialog(null, "Arquivo salvo com sucesso em:\n" + arquivo.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo:\n" + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
