package com.mycompany.mavenproject1;

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

    public void MergeServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("MergeServlet?path=" + file.getFileName() + "&path2=" + file1.getFileName());
    }

    public void DeletePageServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("DeletePageServlet?path=" + file.getFileName() + "&page="+page);//TODO
    }

    public void SplitServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("SplitServlet?path=" + file.getFileName());
    }

    public void ExtractPageServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("ExtractPageServlet?path=" + file.getFileName() + "&page="+page);
    }

    public void ConvertImageServlet() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("ConvertImageServlet?path=" + file.getFileName());
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
