package Elements;

public class Section extends Legal_Element{
	private int sectionID;

	public Section(String name,String title,String letter,int number,String description,String exception,String information,String reference,int sectionID) {
		super(name,title,letter,number,description,exception,reference);
		this.sectionID=sectionID;
	}
	public int getSectionID() {
        return sectionID;
    }
    
    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
}
}