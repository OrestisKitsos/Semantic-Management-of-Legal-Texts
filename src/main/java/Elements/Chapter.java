package Elements;

public class Chapter extends Legal_Element{
	private int chapterID;

	public Chapter(String name,String title,String letter,int number,String description,String exception,String information,String reference,int chapterID) {
		super(name,title,letter,number,description,exception,reference);
		this.chapterID=chapterID;
	}
	public int getChapterID() {
        return chapterID;
    }
    
    public void setChapterID(int chapterID) {
        this.chapterID = chapterID;
}
    
}