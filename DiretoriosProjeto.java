import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;

public final class DiretoriosProjeto {

    private DiretoriosProjeto() {
    }

    public static File obterDiretorio(String nome) throws IOException {
        Path diretorio = obterRaizProjeto().resolve(nome);
        Files.createDirectories(diretorio);
        return diretorio.toFile();
    }

    private static Path obterRaizProjeto() {
        Path localizacao = obterLocalizacaoAplicacao();
        Path raizProjeto = procurarRaizProjeto(localizacao);
        if (raizProjeto != null) {
            return raizProjeto;
        }

        Path diretorioAtual = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        raizProjeto = procurarRaizProjeto(diretorioAtual);
        return raizProjeto != null ? raizProjeto : localizacao;
    }

    private static Path obterLocalizacaoAplicacao() {
        CodeSource codeSource = DiretoriosProjeto.class.getProtectionDomain().getCodeSource();
        if (codeSource != null) {
            try {
                Path localizacao = Paths.get(codeSource.getLocation().toURI()).toAbsolutePath().normalize();
                return Files.isDirectory(localizacao) ? localizacao : localizacao.getParent();
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Não foi possível localizar a pasta da aplicação.", e);
            }
        }

        return Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    }

    private static Path procurarRaizProjeto(Path inicio) {
        for (Path diretorio = inicio; diretorio != null; diretorio = diretorio.getParent()) {
            if (Files.isRegularFile(diretorio.resolve("pom.xml"))) {
                return diretorio;
            }
        }
        return null;
    }
}
