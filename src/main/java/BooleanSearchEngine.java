import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> answers = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File pdfFile : Objects.requireNonNull(pdfsDir.listFiles((f, name) -> name.endsWith(".pdf")))) {
            PdfDocument doc = new PdfDocument(new PdfReader(pdfFile));
            for (int i = 1; i < doc.getNumberOfPages() + 1; i++) {
                PdfPage page = doc.getPage(i);
                String text = PdfTextExtractor.getTextFromPage(page);
                String[] words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
                }
                for (String word : freqs.keySet()) {
                    List<PageEntry> pageEntries = answers.getOrDefault(word, new ArrayList<>());
                    pageEntries.add(new PageEntry(pdfFile.getName(), i, freqs.get(word)));
                    answers.put(word, pageEntries);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> answer = answers.get(word);
        if (!answer.isEmpty()) {
            Collections.sort(answer);
        }
        return answer;
    }
}
