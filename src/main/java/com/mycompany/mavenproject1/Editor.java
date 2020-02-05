package com.mycompany.mavenproject1;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.primefaces.model.UploadedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Editor {

    public static void AddImage(UploadedFile file){
        System.out.println("Images");

        // step 1: creation of a document-object
        Document document = new Document();

        try {
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
            File f = new File(file.getFileName());
            PdfWriter.getInstance(document, new FileOutputStream(f));

            System.out.println(f);
            // step 3: we open the document
            document.open();

            // step 4:
            document.add(new Paragraph("A picture of my dogerhvqeruifvhq"));
        }
        catch(DocumentException | IOException de) {
            System.err.println(de.getMessage());
        }

        // step 5: we close the document
        document.close();
    }

}
