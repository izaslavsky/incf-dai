/**
 * This is a service bean to write the business logic
 */
package edu.ucsd.crbs.incf.components.services.whs;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.ucsd.crbs.incf.exception.UserDefinedException;
import edu.ucsd.crbs.incf.common.INCFLogger;


/**
 * @author Asif Memon
 *
 */
public class WHSServiceBean {


	/**
	 * This method retrieves the data from the database which was entered 
	 * before running the image service
	 * 
	 * @param Value object with the data
	 * 
	 * @return void 
	 */
	public void getImage() 
		throws UserDefinedException, Exception {
	
		INCFLogger.logDebug( WHSServiceBean.class,
		 " Start - WHSServiceBean.getDataForCreateImageService");
		
		try { 
		
			
		} catch ( Exception e ) {
			
			e.getStackTrace();
		
		}
		
	}


}
