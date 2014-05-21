package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

/**Boolean wrapper class
 * 
 * @author Aptima
 *
 */
public class WrapBoolean {
	private boolean b = false;

	/**Constructor that initializes a boolean variable to fals
	 * 
	 */
	public WrapBoolean() {
		b = false;
	}

	/**Method sets the boolean variable to the boolean argument
	 * 
	 * @param b				Input boolean to have set
	 */
	public void set(boolean b) {
		this.b = b;
	}

	/**Get the boolean variable
	 * 
	 * @return				Boolean value of the WrapBoolean
	 */
	public boolean get() {
		return b;
	}
}
