/**
 * @author Sonal Joshi
 *
 */
public class fibonode {
	private int degree;
	fibonode left, right, child, parent;
	private String keyword;
	private int count;
	private boolean childcut;
	
	fibonode(String keyword, int count){
		this.right = this;
		this.left = this;
		this.parent = null;
		this.child = null;
		this.degree = 0;
		this.count = count;
		this.keyword = keyword;
		this.childcut = false;
	}
	/**
	 * @return the degree
	 */
	public int getDegree() {
		return degree;
	}

	/**
	 * @param degree the degree to set
	 */
	public void setDegree(int degree) {
		this.degree = degree;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the childcut
	 */
	public boolean isChildcut() {
		return childcut;
	}
	/**
	 * @param childcut the childcut to set
	 */
	public void setChildcut(boolean childcut) {
		this.childcut = childcut;
	}
}
