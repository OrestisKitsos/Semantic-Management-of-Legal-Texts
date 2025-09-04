package Elements;

public class Article extends Legal_Element {
	private int articleID;

	public Article(String name,String title,String letter,int number,String description,String exception,String information,String reference,int articleID) {
		super(name,title,letter,number,description,exception,reference);
		this.articleID=articleID;
	}
	public int getArticleID() {
        return articleID;
    }
    
    public void setArticleID(int articleID) {
        this.articleID = articleID;
}
}
