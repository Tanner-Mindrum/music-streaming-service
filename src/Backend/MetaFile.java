package Backend;

import java.util.ArrayList;

public class MetaFile {

/*    file :  ~~ARRAY~~ MUSIC.json
    {
        name  : "File1"
        numberOfPages : "3"
        pageSize : "1024"
        size : "2291"
        page :  ~~ARRAY~~  MUSIC1.json []
        {
            number : "1"
            guid   : "22412"
            size   : "1024"
        }*/

    private String fileName;
    private int numPages;
    private long pageSize;
    private long size;
    private ArrayList<MetaPage> pages;

    public MetaFile() {
        fileName = "";
        numPages = 0;
        pageSize = 0;
        size = 0;
        pages = new ArrayList<MetaPage>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public ArrayList<MetaPage> getPages() {
        return pages;
    }

    public void setPages(ArrayList<MetaPage> pages) {
        this.pages = pages;
    }

    public String toString() {
        return "File: ";
    }

}
