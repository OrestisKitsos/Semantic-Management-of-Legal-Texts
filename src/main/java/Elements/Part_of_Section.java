package Elements;

public class Part_of_Section extends Legal_Element{
	private int Part_of_SectionID;

	public Part_of_Section(String name,String title,String letter,int number,String description,String exception,String information,String reference,int Part_of_SectionID) {
		super(name,title,letter,number,description,exception,reference);
		this.Part_of_SectionID=Part_of_SectionID;
	}
	public int getPart_of_SectionID() {
        return Part_of_SectionID;
    }
    
    public void setPart_of_SectionID(int Part_of_SectionID) {
        this.Part_of_SectionID = Part_of_SectionID;
}
}