import java.io.IOException;
import java.util.List;

public class Processamento {

	public static void processar(List<String> lista, String L) throws IOException {

        if (lista == null || lista.isEmpty()) {
            return;
        }
        Edita.removerAcentos(lista);
        Edita.VirPorPont(lista);
        Edita.paraUpperCase(lista);
        Edita.inserirDepartamento(lista);
        Edita.duplicadas(lista);
        Edita.linhasIndesejadas(lista);
        Edita.formatarColunas(lista);
        Edita.Cargos(lista);
        Edita.removerVirgulasDuplicadas(lista);
        Edita.NomeLista(lista, L);
        Edita.limparEspacos(lista);
    }
}
