package com.mycompany.mavenproject1;

import java.io.File;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.ws.rs.Path;

@WebServlet(name = "ConvertImageServlet")
public class ConvertImageServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String imagePath = request.getParameter("path");
        File f = new File(imagePath);
        String destinationPath = System.getProperty("user.home") + File.separator + "Téléchargements"+File.separator+f.getName()+".pdf";
        PdfUtil.ConvertImageIntoPdf(imagePath, destinationPath);
    }
}
