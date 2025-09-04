package Elements;

public class Subintend extends Legal_Element{
	private int subintendID;

	public Subintend(String name,String title,String letter,int number,String description,String exception,String information,String reference,int subintendID) {
		super(name,title,letter,number,description,exception,reference);
		this.subintendID=subintendID;
	}
	public int getSubintendID() {
        return subintendID;
    }
    
    public void setSubintendID(int subintendID) {
        this.subintendID = subintendID;
}
}
