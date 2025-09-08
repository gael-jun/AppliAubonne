package export.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public final class PdfDocumentBuilder implements AutoCloseable {
    private final PDDocument doc = new PDDocument();
    private PDPageContentStream content;
    private float y = 750f;

    public PdfDocumentBuilder() throws IOException {
        newPage();
    }

    public PdfDocumentBuilder addTitle(String title) throws IOException {
        float titleFontSize = 18f;
        content.setFont(PDType1Font.HELVETICA_BOLD, titleFontSize);
        float lineTopY = y; // reference top line for logo/title alignment
        float titleX = 50f;
        float spacingAfterTitle = 30f; // minimum spacing after the title line
        // Try to load the logo from classpath first, then from the resources folder
        try {
            BufferedImage logoImg = null;
            java.io.InputStream is = PdfDocumentBuilder.class.getResourceAsStream("/ressources/logo.png");
            if (is != null) {
                logoImg = ImageIO.read(is);
                is.close();
            }
            if (logoImg == null) {
                File f = new File("ressources/logo.png");
                if (f.exists()) {
                    logoImg = ImageIO.read(f);
                }
            }
            if (logoImg != null) {
                PDImageXObject pdLogo = LosslessFactory.createFromImage(doc, logoImg);
                float desiredH = 72f; // match Grid page (triple size)
                float desiredW = (float) logoImg.getWidth() * desiredH / (float) logoImg.getHeight();
                // Draw logo top-aligned with the title line
                content.drawImage(pdLogo, 50f, lineTopY - desiredH, desiredW, desiredH);
                titleX = 50f + desiredW + 10f; // place title to the right of the logo
                spacingAfterTitle = Math.max(spacingAfterTitle, desiredH + 8f); // ensure content starts below the logo
            }
        } catch (Exception ignore) {
            // no logo available; continue without it
        }
        // Compute text baseline to top-align with logo using font ascent, then offset slightly downward
        float ascentPt = 0.72f * titleFontSize; // fallback ascent
        try {
            var fd = PDType1Font.HELVETICA_BOLD.getFontDescriptor();
            if (fd != null) ascentPt = (fd.getAscent() / 1000f) * titleFontSize;
        } catch (Throwable ignore) {}
        float titleYOffset = 30f; // lower the title a few points without moving the logo
        content.beginText();
        content.newLineAtOffset(titleX, lineTopY - ascentPt - titleYOffset);
        content.showText(title);
        content.endText();
        y -= spacingAfterTitle;
        return this;
    }

    public PdfDocumentBuilder addKeyValueLines(List<String[]> lines) throws IOException {
    // Ajoute un interligne supplémentaire sous le titre/logo avant l'inscription
    y -= 12f;
    content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.beginText();
        content.newLineAtOffset(50, y);
        content.showText("Paramètres d'entrée :");
        content.endText();
        y -= 18;
        for (String[] kv : lines) {
            ensureSpace(18);
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 10);
            content.newLineAtOffset(55, y);
            content.showText(kv[0] + " : " + kv[1]);
            content.endText();
            y -= 13;
        }
    // Augmente l'espace après un tableau pour éviter la proximité avec le contenu suivant (ex: VAN totale)
    y -= 20;
        return this;
    }

    public PdfDocumentBuilder addTable(String[] headers, List<List<String>> rows) throws IOException {
        float tableStartX = 50f;
        float rowHeight = 18f;
        float tableWidth = 480f;
        float[] colWidths = new float[headers.length];
        float total = 0f;
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = tableWidth / headers.length;
            total += colWidths[i];
        }
        // Header background
        content.setStrokingColor(Color.BLACK);
        content.setNonStrokingColor(Color.LIGHT_GRAY);
        content.addRect(tableStartX, y - rowHeight, total, rowHeight);
        content.fill();
        content.setNonStrokingColor(Color.BLACK);
        float nextX = tableStartX;
        for (int i = 0; i < headers.length; i++) {
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 10);
            content.newLineAtOffset(nextX + 2, y - rowHeight + 4);
            content.showText(headers[i]);
            content.endText();
            nextX += colWidths[i];
        }
        y -= rowHeight;
        for (List<String> row : rows) {
            ensureSpace(rowHeight + 10);
            nextX = tableStartX;
            for (int j = 0; j < headers.length; j++) {
                content.setStrokingColor(Color.BLACK);
                content.addRect(nextX, y - rowHeight, colWidths[j], rowHeight);
                content.stroke();
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(nextX + 2, y - rowHeight + 4);
                String val = (j < row.size()) ? row.get(j) : "";
                content.showText(val);
                content.endText();
                nextX += colWidths[j];
            }
            y -= rowHeight;
        }
        y -= 20;
        return this;
    }

    public PdfDocumentBuilder addImage(BufferedImage img, float width, float height) throws IOException {
        ensureSpace(height + 50);
        PDImageXObject pdImage = LosslessFactory.createFromImage(doc, img);
        content.drawImage(pdImage, 50, y - height, width, height);
        y -= (height + 20);
        return this;
    }

    // Ajoute un titre de section (gras 12) avec un léger espacement dessous
    public PdfDocumentBuilder addSectionTitle(String text) throws IOException {
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.beginText();
        content.newLineAtOffset(50, y);
        content.showText(text);
        content.endText();
        y -= 20f;
        return this;
    }

    // Ajoute une ligne de texte (gras 11 par défaut) et espace dessous
    public PdfDocumentBuilder addTextLine(String text) throws IOException {
        content.setFont(PDType1Font.HELVETICA_BOLD, 11);
        content.beginText();
        content.newLineAtOffset(50, y);
        content.showText(text);
        content.endText();
        y -= 24f;
        return this;
    }

    public void saveTo(File file) throws IOException {
        // Close current content stream to allow appending footers
        if (content != null) content.close();

        // Add page numbers (Page X / Y) centered at the bottom of each page
        int total = doc.getNumberOfPages();
        float fontSize = 9f;
        for (int i = 0; i < total; i++) {
            PDPage page = doc.getPage(i);
            float pageWidth = page.getMediaBox().getWidth();
            String text = "Page " + (i + 1) + " / " + total;
            float textWidth = PDType1Font.HELVETICA.getStringWidth(text) / 1000f * fontSize;
            float x = (pageWidth - textWidth) / 2f;
            float yFooter = 30f;
            try (PDPageContentStream footer = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                footer.setFont(PDType1Font.HELVETICA, fontSize);
                footer.beginText();
                footer.newLineAtOffset(x, yFooter);
                footer.showText(text);
                footer.endText();
            }
        }

        doc.save(file);
        doc.close();
    }

    private void newPage() throws IOException {
        PDPage page = new PDPage();
        doc.addPage(page);
        content = new PDPageContentStream(doc, page);
        y = 750f;
    }

    private void ensureSpace(float needed) throws IOException {
        if (y - needed < 100) {
            content.close();
            newPage();
        }
    }

    @Override
    public void close() throws IOException {
        // no-op, use saveTo to close resources
    }
}
