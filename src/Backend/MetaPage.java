package Backend;

public class MetaPage {

//    page :  ~~ARRAY~~  MUSIC1.json []
//    {
//        number : "1"
//        guid   : "22412"
//        size   : "1024"
//    }*/

    private int number;
    private long guid;
    private long size;

    public MetaPage() {
        number = 0;
        guid = 0;
        size = 0;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getGuid() {
        return guid;
    }

    public void setGuid(long guid) {
        this.guid = guid;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
