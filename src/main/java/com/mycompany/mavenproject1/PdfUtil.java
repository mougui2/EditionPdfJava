/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.lowagie.text.Image;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TabAlignment;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.TreeMap;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Morgan
 */
public class PdfUtil {

    public static void DeletePage(int pageNumberToDelete, String destinationPath, String DocumentToChangePath) throws IOException {
        File file = new File(destinationPath);
        file.getParentFile().mkdirs();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(file));
        Document doc = new Document(pdfDoc);

        // Initialize a resultant document outlines in order to copy outlines from the source documents.
        // Note that outlines still could be copied even if in destination document outlines
        // are not initialized, by using PdfMerger with mergeOutlines value set as true.
        pdfDoc.initializeOutlines();

        // Copier contains the additional logic to copy acroform fields to a new page.
        // PdfPageFormCopier uses some caching logic which can potentially improve performance
        // in case of the reusing of the same instance.
        PdfPageFormCopier formCopier = new PdfPageFormCopier();

        // Copy all merging file's pages to the result pdf file 
        PdfDocument documentToChange = new PdfDocument(new PdfReader(DocumentToChangePath));

        int numberOfPages = documentToChange.getNumberOfPages();
        for (int i = 1; i <= numberOfPages; i++) {
            if (i < pageNumberToDelete) {
                documentToChange.copyPagesTo(i, i, pdfDoc, formCopier);
            }
            if (i > pageNumberToDelete) {
                documentToChange.copyPagesTo(i, i - 1, pdfDoc, formCopier);
            }
        }
        doc.close();
    }

    public static void MergePdf(String pdfAPath, String PdfBPath, String destinationPath) throws IOException {
        File file = new File(destinationPath);
        file.getParentFile().mkdirs();

//        new MergeWithToc().manipulatePdf(DEST);
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(destinationPath));
        Document doc = new Document(pdfDoc);

        // Initialize a resultant document outlines in order to copy outlines from the source documents.
        // Note that outlines still could be copied even if in destination document outlines
        // are not initialized, by using PdfMerger with mergeOutlines value set as true.
        pdfDoc.initializeOutlines();

        // Copier contains the additional logic to copy acroform fields to a new page.
        // PdfPageFormCopier uses some caching logic which can potentially improve performance
        // in case of the reusing of the same instance.
        PdfPageFormCopier formCopier = new PdfPageFormCopier();

        // Copy all merging file's pages to the result pdf file 
        Map<String, PdfDocument> filesToMerge = new TreeMap<String, PdfDocument>();

        filesToMerge.put(pdfAPath, new PdfDocument(new PdfReader(pdfAPath)));
        filesToMerge.put(PdfBPath, new PdfDocument(new PdfReader(PdfBPath)));

        Map<Integer, String> toc = new TreeMap<Integer, String>();
        int page = 1;
        for (Map.Entry<String, PdfDocument> entry : filesToMerge.entrySet()) {
            PdfDocument srcDoc = entry.getValue();
            int numberOfPages = srcDoc.getNumberOfPages();

            toc.put(page, entry.getKey());

            for (int i = 1; i <= numberOfPages; i++, page++) {
                Text text = new Text(String.format("Page %d", page));
                srcDoc.copyPagesTo(i, i, pdfDoc, formCopier);

                // Put the destination at the very first page of each merged document
                if (i == 1) {
                    text.setDestination("p" + page);
                }

                doc.add(new Paragraph(text)
                        .setFixedPosition(page, 549, 810, 40)
                        .setMargin(0)
                        .setMultipliedLeading(1));
            }
        }

//        PdfDocument tocDoc = new PdfDocument(new PdfReader(SRC3));
//        tocDoc.copyPagesTo(1, 1, pdfDoc, formCopier);
//        tocDoc.close();
        // Create a table of contents
        float tocYCoordinate = 750;
        float tocXCoordinate = doc.getLeftMargin();
        float tocWidth = pdfDoc.getDefaultPageSize().getWidth() - doc.getLeftMargin() - doc.getRightMargin();
        for (Map.Entry<Integer, String> entry : toc.entrySet()) {
            Paragraph p = new Paragraph();
            p.addTabStops(new TabStop(500, TabAlignment.LEFT, new DashedLine()));
            p.add(entry.getValue());
            p.add(new Tab());
            p.add(String.valueOf(entry.getKey()));
            p.setAction(PdfAction.createGoTo("p" + entry.getKey()));
            doc.add(p
                    .setFixedPosition(pdfDoc.getNumberOfPages(), tocXCoordinate, tocYCoordinate, tocWidth)
                    .setMargin(0)
                    .setMultipliedLeading(1));

            tocYCoordinate -= 20;
        }

        for (PdfDocument srcDoc : filesToMerge.values()) {
            srcDoc.close();
        }

        doc.close();
    }

    public static void SplitPdf(String pathToDoc,String destinationPath) throws FileNotFoundException, IOException {
        // read original pdf file
        com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader(pathToDoc);

        // get number of pages
        int n = reader.getNumberOfPages();
        System.out.println("Number of pages: " + n);

        // loop over all pages
        int i = 0;
        while (i < n) {

            // create destination file name
            String destination = destinationPath.substring(0, destinationPath.indexOf(".pdf")) + "-" + String.format("%03d", i + 1) + ".pdf";
            System.out.println("Writing " + destination);

            // create new document with corresponding page size
            com.lowagie.text.Document document = new com.lowagie.text.Document(reader.getPageSizeWithRotation(1));

            // create writer and assign document and destination
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(destination));
            document.open();

            // read original page and copy to writer
            PdfImportedPage page = copy.getImportedPage(reader, ++i);
            copy.addPage(page);

            // close and write the document
            document.close();
        }
    }

    public static void ExtractPage(int pageNumber, String originalfilePath, String destFilePath) throws IOException {
        com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader(originalfilePath);
        PdfStamper stamper;
        reader.selectPages(String.valueOf(pageNumber));
        stamper = new PdfStamper(reader, new FileOutputStream(destFilePath));
        stamper.close();
        reader.close();
    }
    
    public static void ConvertImageIntoPdf(String imagePath,String DestPdfPath) throws IOException{
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(DestPdfPath));
        document.open();
        Image img = Image.getInstance(imagePath);
        document.add(img);
        document.close();
    }
}

