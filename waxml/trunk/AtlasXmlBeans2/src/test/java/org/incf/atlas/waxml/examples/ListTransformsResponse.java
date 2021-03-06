package org.incf.atlas.waxml.examples;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.PointType;

import org.incf.atlas.waxml.generated.CoordinateChainTransformType;
import org.incf.atlas.waxml.generated.ListTransformationsResponseDocument;
import org.incf.atlas.waxml.generated.TransformationResponseDocument;
import org.incf.atlas.waxml.generated.CoordinateTransformationChainResponseType.CoordinateTransformationChain;
import org.incf.atlas.waxml.generated.ListTransformationsResponseType.TransformationList;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.generated.QueryInfoType.QueryUrl;

import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.utilities.*;
import org.junit.Test;

public class ListTransformsResponse {
	@Test
	public void validFullResponse() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		XmlObject co = completeResponse();
		ArrayList errorList = new ArrayList();
		boolean validXml = Utilities.validateXml(opt, co, errorList);
		assertTrue(errorList.toString(), validXml);

	}
	
public String asXml(){
	XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
	opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
	opt.setSaveNamespacesFirst();
	opt.setSaveAggressiveNamespaces();
	opt.setUseDefaultNamespace();
	
	ListTransformationsResponseDocument co = completeResponse();
	
	
	
	
	 ArrayList errorList = new ArrayList();
	 opt.setErrorListener(errorList);
	
	 
	 // Validate the XML.
	 boolean isValid = co.validate(opt);
	 
	 // If the XML isn't valid, loop through the listener's contents,
	 // printing contained messages.
	 if (!isValid)
	 {
	      for (int i = 0; i < errorList.size(); i++)
	      {
	          XmlError error = (XmlError)errorList.get(i);
	          
	          System.out.println("\n");
	          System.out.println("Message: " + error.getMessage() + "\n");
	          System.out.println("Location of invalid XML: " + 
	              error.getCursorLocation().xmlText() + "\n");
	      }
	 }
	 
	
	return co.xmlText(opt);

	
}

public ListTransformationsResponseDocument completeResponse() {
	ListTransformationsResponseDocument co =   ListTransformationsResponseDocument.Factory.newInstance();
co.addNewListTransformationsResponse();
	
co.getListTransformationsResponse().newCursor().insertComment("Generated " + Calendar.getInstance().getTime());

	
	//Query Info
	co.getListTransformationsResponse().addNewQueryInfo();
	QueryInfoType qi = co.getListTransformationsResponse().getQueryInfo();
	QueryUrl url = QueryUrl.Factory.newInstance();
	url.setName("ListTransformations");
	url.setStringValue("URL");
	qi.setQueryUrl(url);
     Criteria criterias = qi.addNewCriteria();


	InputType input1 =criterias.addNewInput();
	InputStringType inputSrsConstraint = (InputStringType) input1.changeType(InputStringType.type);

	//InputStringType inputSrsConstraint = InputStringType.Factory.newInstance();
	inputSrsConstraint.setName("inputSrsName");
	inputSrsConstraint.setValue("Mouse_Paxinos_1.0");
		
	InputType input2 =criterias.addNewInput();
	InputStringType ouputSrsConstraint  = (InputStringType) input2.changeType(InputStringType.type);
	
	//InputStringType ouputSrsConstraint = InputStringType.Factory.newInstance();
	ouputSrsConstraint.setName("outputSrsName");
	ouputSrsConstraint.setValue("Mouse_ABAreference_1.0");
	
	 
	TransformationList ct= co.getListTransformationsResponse().addNewTransformationList();
	ct.setHubCode("HUBA");
	
	CoordinateTransformationInfoType ex1 = ct.addNewCoordinateTransformation();
	
	
	ex1.setCode("Mouse_Paxinos_1.0_To_Mouse_WHS_1.0_1.0");
	ex1.setHub("UCSD");
	ex1.setInputSrsName(new QName("Mouse_Paxinos_1.0"));
	ex1.setOutputSrsName(new QName("Mouse_WHS_1.0"));
	ex1.setDisplacement(1);
	ex1.setStringValue("RequestUrl_1");
	
	CoordinateTransformationInfoType ex2 =ct.addNewCoordinateTransformation();
	
	ex2.setCode("Mouse_WHS_1.0_To_Mouse_AGEA_1.0_1.0");
	ex2.setHub("ABA");
	ex2.setInputSrsName(new QName("Mouse_WHS_1.0"));
	ex2.setOutputSrsName(new QName("Mouse_AGEA_1.0"));
	ex2.setDisplacement(1);
	ex2.setStringValue("RequestUrl_2");
	return co;
}
}
