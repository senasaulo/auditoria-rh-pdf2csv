import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Seleciona {

    public static String selecionarArquivoPDF(String ordem) {

        JOptionPane.showMessageDialog(null, "Selecione o " + ordem + " arquivo PDF", "Atenção",
                JOptionPane.INFORMATION_MESSAGE);

        File pastaArquivos;
        try {
            pastaArquivos = DiretoriosProjeto.obterDiretorio("Arquivos");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível abrir a pasta Arquivos:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        JFileChooser chooser = new JFileChooser(pastaArquivos);
        chooser.setDialogTitle("Seleção do " + ordem + " arquivo PDF");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("Arquivos PDF", "pdf"));

        int retorno = chooser.showOpenDialog(null);

        if (retorno == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = chooser.getSelectedFile();
            return arquivoSelecionado.getAbsolutePath();
        }

        return null; // usuário cancelou
    }
}
