import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class Edita {

	private static final String ARQUIVO_LINHAS_INDESEJADAS = "linhas_indesejadas.txt";
	private static final String ARQUIVO_CARGOS = "cargos.txt";
	private static final List<String> TERMOS_INDESEJADOS_PADRAO = List.of(
			"DEPARTAMENTO",
			"TOTAL DE EMPREGADOS",
			"RELAÇÃO DE EMPREGADOS",
			"INSTITUTO DE GESTAO APLICADA",
			"HORAS",
			"HOR",
			"NF",
			"ND",
			"SIN",
			"OPT",
			"SISTEMA LICENCIADO",
			"CÓDIGO");
	private static final List<String> CARGOS_PADRAO = List.of(
			"ASSISTENTE ADMINISTRATIVO",
			"TECNICO DE ENFERMAGEM",
			"AGENTE DE ACAO SOCIAL",
			"ASSISTENTE SOCIAL",
			"FISIOTERAPEUTA",
			"FONOAUDIOLOGO",
			"NUTRICIONISTA",
			"FARMACEUTICO",
			"RECEPCIONISTA",
			"PSICOLOGO",
			"ODONTOLOGO",
			"ENFERMEIRO",
			"DENTISTA",
			"TERAPEUTA",
			"AUXILIAR",
			"GERENTE",
			"EDUCADOR",
			"ASSESSOR",
			"VIGIA");

	public static void paraUpperCase(List<String> lista) {
		for (int i = 0; i < lista.size(); i++) {
			lista.set(i, lista.get(i).toUpperCase());
		}
	}

	public static List<String> duplicadas(List<String> lista) {
		Set<String> set = new HashSet<>(lista);
		return new ArrayList<>(set);
	}

	public static void linhasIndesejadas(List<String> lista) throws IOException {
		List<String> termos = lerConfiguracao(ARQUIVO_LINHAS_INDESEJADAS, TERMOS_INDESEJADOS_PADRAO);

		lista.removeIf(linha -> {
			if (linha == null) {
				return false;
			}
			String linhaNormalizada = linha.toUpperCase(Locale.ROOT);
			return termos.stream().anyMatch(linhaNormalizada::startsWith);
		});
	}

	private static List<String> lerConfiguracao(String nomeArquivo, List<String> valoresPadrao) throws IOException {
		Path arquivo = DiretoriosProjeto.obterDiretorio("Arquivos").toPath().resolve(nomeArquivo);
		if (!Files.exists(arquivo)) {
			try {
				Files.write(arquivo, valoresPadrao, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
			} catch (FileAlreadyExistsException e) {
				// Another application instance created the configuration file.
			}
		}

		List<String> valores = new ArrayList<>();
		for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
			String valorNormalizado = semAcentos(linha.trim()).toUpperCase(Locale.ROOT);
			if (!valorNormalizado.isEmpty()) {
				valores.add(valorNormalizado);
			}
		}
		return valores;
	}
	
	public static void inserirDepartamento(List<String> lista) {
	    String departamentoAtual = "";
	    for (int i = 0; i < lista.size(); i++) {
	        String linha = lista.get(i);
	        if (linha == null || linha.isBlank()) continue;
	        if (linha.startsWith("DEPARTAMENTO")) {
	            departamentoAtual = linha.replaceFirst("(?i)DEPARTAMENTO\\s*:?\\s*", "").replaceFirst("^\\d+\\s*-", "").trim();
	            continue;
	        }
	        String trimmed = linha.trim();
	        if (!departamentoAtual.isEmpty() && trimmed.length() >= 3 && Character.isDigit(trimmed.charAt(0))) {
	            String novaLinha = linha.substring(0, 3) + departamentoAtual+"," + linha.substring(3);
	            lista.set(i, novaLinha);
	        }
	    }
	}
	
	public static void VirPorPont(List<String> lista) {
		for (int i = 0; i < lista.size(); i++) {
			lista.set(i, lista.get(i).replace(",", "."));
		}
	}
	
	public static void formatarColunas(List<String> lista) {
		for (int i = 0; i < lista.size(); i++) {
			String linha = lista.get(i);
			if (linha.length() > 3
					&& Character.isDigit(linha.charAt(0))
					&& Character.isDigit(linha.charAt(1))
					&& Character.isDigit(linha.charAt(2))) {
				linha = linha.substring(0, 3) + "," + linha.substring(3);}
			String mens = "MENSALISTA";
			linha = linha.replaceAll(
					"([A-ZÁÉÍÓÚÂÊÔÃÕÇ])MENSALISTA",
					"$1,MENSALISTA"
					);

			if (linha.toUpperCase().contains(mens)) {
				String[] partes = linha.split("(?i)\\bMENSALISTA\\b", 2);
				String antes = partes[0].trim();
				String depois = partes.length > 1 ? partes[1].trim() : "";
				depois = depois.replace(" ", ",");
				linha = antes + ",MENSALISTA," + depois;
			}
			lista.set(i, linha);
		}
	}	
	public static void Cargos(List<String> lista) throws IOException {
		List<String> cargos = lerConfiguracao(ARQUIVO_CARGOS, CARGOS_PADRAO);
		for (int i = 0; i < lista.size(); i++) {

			String linha = lista.get(i);
			for (String cargo : cargos) {
				if (cargo.equals("TERAPEUTA")) {
					linha = linha.replaceAll("([A-ZÁÉÍÓÚÂÊÔÃÕÇ])(?<!FISIO)(TERAPEUTA\\b)","$1, $2");
					linha = linha.replaceAll("(?<!,)\\s*(?<!FISIO)(TERAPEUTA\\b)",", $1");
					continue;
				}
				String cargoRegex = Pattern.quote(cargo);
				linha = linha.replaceAll("([A-ZÁÉÍÓÚÂÊÔÃÕÇ])(" + cargoRegex + "\\b)","$1, $2");
				linha = linha.replaceAll("(?<!,)\\s*(" + cargoRegex + "\\b)",", $1");
			}
			linha = linha.replaceAll(",{2,}", ",");
			lista.set(i, linha);
		}
	}

	public static void removerVirgulasDuplicadas(List<String> lista) {
		for (int i = 0; i < lista.size(); i++) {
			String linha = lista.get(i);
			linha = linha.replaceAll(",\\s*,+", ",");
			linha = linha.replace(",", ";");
			lista.set(i, linha);
		}
	}
	public static void removerAcentos(List<String> lista) {
		for (int i = 0; i < lista.size(); i++) {
			lista.set(i, semAcentos(lista.get(i)));
		}
	}

	private static String semAcentos(String texto) {
		return Normalizer.normalize(texto, Normalizer.Form.NFD)
				.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	}
	public static void NomeLista(List<String> lista, String x) {
		for (int i = 0; i < lista.size(); i++) {lista.set(i, x +";" + lista.get(i));}
		
	}
	public static void limparEspacos(List<String> lista) {
	    if (lista == null) return;
	    for (int i = 0; i < lista.size(); i++) {
	        String linha = lista.get(i);
	        if (linha != null) {
	            linha = linha.trim().replaceAll("\\s{2,}", " ").replaceAll("\\s*;\\s*", ";");
	            lista.set(i, linha);
	        }
	    }
	}
	
	public static void OrdenarNome(List<String> lista) {
	    lista.sort(Comparator.comparing(linha -> {
	    	String[] partes = linha.split(";");
	        return partes.length > 2 ? partes[2] : "";
	    }));
	}
	
	
}
