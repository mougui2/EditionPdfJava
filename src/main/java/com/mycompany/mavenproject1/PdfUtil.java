/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileNotFoundException;
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

        com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader(DocumentToChangePath);
        com.lowagie.text.Document document = new com.lowagie.text.Document(reader.getPageSizeWithRotation(1));

        // create writer and assign document and destination
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(destinationPath));
        document.open();

        // read original page and copy to writer
        int numberOfPages = document.getPageNumber();
        for (int i = 1; i <= numberOfPages; i++) {
            if (i < pageNumberToDelete) {
                PdfImportedPage page = copy.getImportedPage(reader, i);
                copy.addPage(page);
            }
            if (i > pageNumberToDelete) {
                PdfImportedPage page = copy.getImportedPage(reader, i - 1);
                copy.addPage(page);
            }
        }
        document.close();
    }

    public static void MergePdf(String pdfAPath, String pdfBPath, String destinationPath) throws IOException {
        File file = new File(destinationPath);
        file.getParentFile().mkdirs();

        com.lowagie.text.Document document = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter writer = com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(destinationPath));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        
            com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader(pdfAPath);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }
            
            reader = new com.lowagie.text.pdf.PdfReader(pdfBPath);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }
        
        document.close();
    }

    public static void SplitPdf(String pathToDoc, String destinationPath) throws FileNotFoundException, IOException {
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

    public static void ConvertImageIntoPdf(String imagePath, String DestPdfPath) throws IOException {
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(DestPdfPath));
        document.open();
        Image img = Image.getInstance(imagePath);
        img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
        document.add(img);
        document.close();
    }
}
