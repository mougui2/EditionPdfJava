package com.mycompany.mavenproject1;

import java.io.File;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ExtractPageServlet")
public class ExtractPageServlet extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        int pageNumber = int.class.cast( request.getParameter("page"));
        String filePath = request.getParameter("path");
        File f = new File(filePath);
        String destinationPath = System.getProperty("user.home") + File.separator + "Téléchargements"+File.separator+f.getName()+".pdf";
        PdfUtil.ExtractPage(pageNumber,filePath,destinationPath);
    }
}
