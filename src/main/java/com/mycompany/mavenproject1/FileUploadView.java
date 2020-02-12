package com.mycompany.mavenproject1;

import java.io.File;
import java.io.IOException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class FileUploadView {

    private UploadedFile file;
    private UploadedFile file1;
    
    private String path = System.getProperty("user.home") + File.separator + "Downloads" + File.separator ;
    
    private String page;

    public String getpage() {
        return page;
    }

    public void setpage(String page) {
        this.page = page;
    }
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getFile1() {
        return file1;
    }

    public void setFile1(UploadedFile file) {
        this.file1 = file;
    }

    public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void mergeServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("MergeServlet?path=" +path+ file.getFileName()+ "&path2=" +path+ file1.getFileName());
    }

    public void deletePageServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("DeletePageServlet?path=" +path+ file.getFileName() + "&pageToDelete="+page);
    }

    public void splitServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("SplitServlet?path=" +path+ file.getFileName());
    }

    public void extractPageServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("ExtractPageServlet?path="+path+ file.getFileName() + "&page="+page);
    }

    public void convertImageServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("ConvertImageServlet?path=" +path+ file.getFileName());
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
