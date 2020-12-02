/**
* abstract interface of Account class
* @author Khai Phan (02/11/2020)
*/

package com.seneca.accounts;

public interface Taxable {
	static final double tax_rate = 15;
	
	/**
	 * calculate tax with a given tax rate
	 */
	void calculateTax();
	
	/**
	 * calculate amount of tax based on given rate and given income
	 * @return amount of taxes
	 */
	double getTaxAmount();
	
	/**
	 * similar to to_string method, use to display information
	 * @return information to be display
	 */
	String createTaxStatement();
}
