import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessamentoFinal {
	
	public static List<String> processar(List<String> old, List<String> current) {	
		List<String> end = new ArrayList<>();
		Map<String, String> Old = new HashMap<>();
        Map<String, String> Current = new HashMap<>();

        for (String linha : old) {
            if (linha == null || linha.isBlank()) continue;
            String chave = chavePessoa(linha);
            if (chave != null) Old.put(chave, linha);
        }

        for (String linha : current) {
            if (linha == null || linha.isBlank()) continue;
            String chave = chavePessoa(linha);
            if (chave != null) Current.put(chave, linha);
        }
     
        for (String chave : Old.keySet()) {
            if (Current.containsKey(chave)) {end.add("permanencia;" + removerPrefixo(Current.get(chave)));}
        }

        for (String chave : Old.keySet()) {
            if (!Current.containsKey(chave)) {end.add("demitidos;" + removerPrefixo(Old.get(chave)));}
        }

        for (String chave : Current.keySet()) {
            if (!Old.containsKey(chave)) { end.add("contratados;" + removerPrefixo(Current.get(chave)));}
        }
        
        Edita.OrdenarNome(end);
        return end;
    }

    private static String chavePessoa(String linha) {
        String[] p = linha.split(";", -1);
        if (p.length < 4 || !p[1].matches("\\d+") || p[3].isBlank()) {
            return null;
        }
        return p[1] + ";" + p[3]; // matrícula + nome
    }

    private static String removerPrefixo(String linha) {
        return linha.replaceFirst("^(Antiga|Nova);", "");
    }
 
}


