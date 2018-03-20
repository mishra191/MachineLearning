/**
 * 
 */
package io.jta.examples;

/**
 * @author Anurag
 *
 */
public class TransformationBean {

	/**
	 * make upper case...
	 * @param body
	 * @return
	 */
	public String makeUpperCase(String body) {
		
		String transformedBody = body.toUpperCase();
		
		System.out.println("upper case ----------------------------------------------------------------------------------" + transformedBody);
		
		return transformedBody;
	}

}
