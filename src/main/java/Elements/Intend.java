package Elements;

public class Intend extends Legal_Element{
	private int intendID;

	public Intend(String name,String title,String letter,int number,String description,String exception,String information,String reference,int intendID) {
		super(name,title,letter,number,description,exception,reference);
		this.intendID=intendID;
	}
	public int getIntendID() {
        return intendID;
    }
    
    public void setIntendID(int intendID) {
        this.intendID = intendID;
}
}
