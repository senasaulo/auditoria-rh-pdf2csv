import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {

		String Old = Seleciona.selecionarArquivoPDF("ANTIGO");
		if (Old == null) {
			return;
		}

		String Current = Seleciona.selecionarArquivoPDF("NOVO");
		if (Current == null) {
			return;
		}
		
		List<String> old = LerPdf.ler(Old);
		List<String> current = LerPdf.ler(Current);
		
		if(old != null && current != null) {
			try {
				Processamento.processar(old,"Antiga");
				Processamento.processar(current,"Nova");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Não foi possível ler os arquivos de configuração:\n"
						+ e.getMessage(),
						"Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	
		List<String> end  = ProcessamentoFinal.processar(old,current);
		Salva.salvar(end);

	}

}
