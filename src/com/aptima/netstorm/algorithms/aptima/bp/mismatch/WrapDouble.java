package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

/**Double wrapper class
 * 
 * @author Aptima
 *
 */
public class WrapDouble {
	private double d = 0.0;
	
	/**Constructor that initializes a boolean variable to 0.0
	 * 
	 */
	public WrapDouble() {
		d = 0.0;
	}
	
	/**Set the double value to the argument
	 * 
	 * @param d				Double to set the value to
	 */
	public void set(double d) {
		this.d = d;
	}
	
	/**Get the double value
	 * 
	 * @return				Get the value of WrapDouble
	 */
	public double get() {
		return d;
	}
}
