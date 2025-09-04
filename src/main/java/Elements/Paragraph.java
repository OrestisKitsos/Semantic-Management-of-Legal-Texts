package Elements;

public class Paragraph extends Legal_Element{
	private int paragraphID;

	public Paragraph(String name,String title,String letter,int number,String description,String exception,String information,String reference,int paragraphID) {
		super(name,title,letter,number,description,exception,reference);
		this.paragraphID=paragraphID;
	}
	public int getParagraphID() {
        return paragraphID;
    }
    
    public void setParagraphID(int paragraphID) {
        this.paragraphID = paragraphID;
}
}