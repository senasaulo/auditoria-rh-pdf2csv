import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class LerPdf {

    public static List<String> ler(String caminhoArquivo) {

        List<String> lista = new ArrayList<>();

        try (PDDocument document = PDDocument.load(new File(caminhoArquivo))) {

            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(document);

            // Quebra o texto em linhas
            String[] linhas = texto.split("\\r?\\n");

            for (String linha : linhas) {
                if (!linha.trim().isEmpty()) {
                    lista.add(linha.trim());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}