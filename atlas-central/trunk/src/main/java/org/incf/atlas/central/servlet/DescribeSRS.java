package org.incf.atlas.central.servlet;


import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.opengis.gml.x32.PointType;
import net.opengis.gml.x32.UnitOfMeasureType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.central.resource.CentralServiceDAOImpl;
import org.incf.atlas.central.resource.CentralServiceVO;
import org.incf.atlas.central.resource.Utilities;
import org.incf.atlas.central.util.DataInputs;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.DescribeSRSResponseType.Fiducials;
import org.incf.atlas.waxml.generated.DescribeSRSResponseType.Slices;
import org.incf.atlas.waxml.generated.ListSRSResponseType.Orientations;
import org.incf.atlas.waxml.generated.ListSRSResponseType.SRSList;
import org.incf.atlas.waxml.generated.OrientationType.Author;
import org.incf.atlas.waxml.generated.SRSType.Area;
import org.incf.atlas.waxml.generated.SRSType.DerivedFrom;
import org.incf.atlas.waxml.generated.SRSType.Name;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class DescribeSRS implements ExecuteProcessHandler {

	private final Logger logger = LoggerFactory.getLogger(
			DescribeSRS.class);

	//private String dataInputString;
	//private DataInputs dataInputs;
	int randomGMLID = 0;
	String authorCode = "";
	String authorName = "";
	String orientationDescription = "";

	private ServletContext context;

	String uri = null;
	

	public DescribeSRS(ServletContext context) {
		this.context = context;
	}

	public String getProcessResponse(ServletContext context, HttpServletRequest request,  
			HttpServletResponse response, DataInputs dataInputs) {

		CentralServiceVO vo = new CentralServiceVO();
		uri = request.getRequestURL().toString();
		System.out.println("URI is - " + uri);
		String url = request.getRequestURI();
		System.out.println("URL is - " + url);
		
        try { 

        	XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
        	opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
        	opt.setSaveNamespacesFirst();
        	opt.setSaveAggressiveNamespaces();
        	opt.setUseDefaultNamespace();

        	DescribeSRSResponseDocument document = completeResponse();

        	ArrayList errorList = new ArrayList();
        	 opt.setErrorListener(errorList);
        	 boolean isValid = document.validate(opt);

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
	        
	    //return document.xmlText(opt);
		return document.xmlText(opt);

        } catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void createNueroDimentions(NeurodimensionType dimension, String name, float maxValue, String xRef){
		dimension.setStringValue(name);
		dimension.setMaxValue(maxValue);
		//dimension.setHref(xRef);
	}
	public static void QueryInfoSrs(QueryInfoType queryInfo, String callUrl)
	{
		queryInfo.addNewQueryUrl().setStringValue(callUrl);
		
		return ;
	}

	public static OrientationType orientation(OrientationType orient, String code, String name, String gmlID, String authorCode, String authorName, String orientationDescription) { 
		
		orient.setCode(code);
		orient.setId(gmlID); // this is what is linked, to
		orient.setName(name);
		Author author = orient.addNewAuthor();
		author.setDateSubmitted(Calendar.getInstance());
		author.setAuthorCode(authorCode);
		author.setStringValue(authorName);
		
		Incfdescription desc = orient.addNewDescription();
		desc.setStringValue(orientationDescription);
		
		return orient;
		
	}

	//First SRS
	public static void addSRS(SRSList srsList, ArrayList list, int size){

		CentralServiceVO vo = null;
		
		try { 

	    Iterator iterator = list.iterator();
	    SRSType srs = null;
		
	    while (iterator.hasNext()) {

	    System.out.println("**************************Count is********************* " + list.size());
		srs =  srsList.addNewSRS();

		vo = (CentralServiceVO)iterator.next();
		
		Name name = srs.addNewName();
		name.setStringValue(vo.getSrsName());
		name.setSrsCode(vo.getSrsCode());
		name.setSrsBase(vo.getSrsDescription());
		name.setSrsVersion(vo.getSrsVersion());
		name.setSpecies(vo.getSpecies());
		//name.setUrn("ReferenceUrl");//Uncomment this once i find the value

		Incfdescription srsdescription = srs.addNewDescription();
		srsdescription.setStringValue(vo.getSrsDescription());

		AuthorType author = 	srs.addNewAuthor();
		author.setAuthorCode(vo.getSrsAuthorCode());
		author.setDateSubmitted(Calendar.getInstance());
		
		IncfCodeType origin = 	srs.addNewOrigin();
		//origin.setCodeSpace("URN");
		origin.setStringValue(vo.getOrigin());

	    //<Area structureName=�whole brain� structureURN=���/>
		Area area = srs.addNewArea();
		area.setStructureName(vo.getRegionOfValidity());
		//area.setStructureURN("URN");
	
		UnitOfMeasureType unit = srs.addNewUnits();
		unit.setUom(vo.getUnitsAbbreviation());
	
		NeurodimensionsType dimensions = srs.addNewNeurodimensions();
		NeurodimensionType minusX = dimensions.addNewMinusX();
		createNueroDimentions(minusX, vo.getNeuroMinusXCode(), Float.parseFloat(vo.getDimensionMinX()), "#"+vo.getNeuroMinusXCode());
		NeurodimensionType minusY = dimensions.addNewMinusY();
		createNueroDimentions(minusY, vo.getNeuroMinusYCode(), Float.parseFloat(vo.getDimensionMinY()), "#"+vo.getNeuroMinusYCode());
		NeurodimensionType minusZ = dimensions.addNewMinusZ();
		createNueroDimentions(minusZ, vo.getNeuroMinusZCode(), Float.parseFloat(vo.getDimensionMinZ()), "#"+vo.getNeuroMinusZCode());
		NeurodimensionType plusX =dimensions.addNewPlusX();
		createNueroDimentions(plusX, vo.getNeuroPlusXCode(), Float.parseFloat(vo.getDimensionMaxX()), "#"+vo.getNeuroPlusXCode());
		NeurodimensionType plusY =dimensions.addNewPlusY();
		createNueroDimentions(plusY, vo.getNeuroPlusYCode(), Float.parseFloat(vo.getDimensionMaxY()), "#"+vo.getNeuroPlusYCode());
		NeurodimensionType plusZ =dimensions.addNewPlusZ();
		createNueroDimentions(plusZ, vo.getNeuroPlusZCode(), Float.parseFloat(vo.getDimensionMaxZ()), "#"+vo.getNeuroPlusZCode());
	
		IncfUriSliceSource cite = srs.addNewSource();
		cite.setStringValue(vo.getSourceURI());
		cite.setFormat(vo.getSourceFileFormat());//Could be null
	    DerivedFrom derived = srs.addNewDerivedFrom();
	    derived.setSrsName(vo.getDerivedFromSRSCode());
	    //derived.setMethod("MethodName");
	    srs.setDateCreated(Calendar.getInstance());
		srs.setDateUpdated(Calendar.getInstance());

		}

	} catch (Exception e) {
		e.printStackTrace();
	}
		return;
	}

	public DescribeSRSResponseDocument completeResponse() {

		DescribeSRSResponseDocument document =	DescribeSRSResponseDocument.Factory.newInstance(); 
		
		DescribeSRSResponseType rootDoc =	document.addNewDescribeSRSResponse();
		//rootDoc.newCursor().insertComment("Test Comment");
		QueryInfoSrs(rootDoc.addNewQueryInfo(), uri.toString());
		SRSList srsList = rootDoc.addNewSRSList();

    	//Start - Get data from the database
		ArrayList list = new ArrayList();
		CentralServiceDAOImpl impl = new CentralServiceDAOImpl();
		list = impl.getSRSsData();
		//End
		
		addSRS(srsList, list, list.size());

		ArrayList list2 = impl.getOrientationData();
		Orientations o = null;
		
		Iterator iterator2 = list2.iterator();
		CentralServiceVO vo = null;

	    Random randomGenerator = new Random();
		while ( iterator2.hasNext()) {
		    for (int idx = 1; idx <= 10; ++idx){
			      randomGMLID = randomGenerator.nextInt(100);
			    }
			vo = (CentralServiceVO) iterator2.next(); 
			o = rootDoc.addNewOrientations();
			OrientationType orientaiton1 = o.addNewOrientation();
			orientation(orientaiton1,vo.getOrientationName(),vo.getOrientationName(),String.valueOf(randomGMLID), vo.getOrientationAuthor(), vo.getOrientationAuthor(), vo.getOrientationDescription());
		}

		//Start - Get Slice data for aba ref
		vo.setSpaceCode("ABA_ref");// FIXME - Remove the hardcoded value
		CentralServiceDAOImpl daoImpl1 = new CentralServiceDAOImpl();
		ArrayList sliceDataList1 = daoImpl1.getSliceData(vo);
		//Start - End Slice data
		
		Slices s1 = rootDoc.addNewSlices();
		Iterator iterator1 = sliceDataList1.iterator();
		
		while (iterator1.hasNext()) { 
			vo = (CentralServiceVO) iterator1.next();
			abaRefSliceElements( s1.addNewSlice(), 1, vo);
		}

		//Start - Get Slice data for ucsd paxinos
		vo.setSpaceCode("M_Pax");// FIXME - Remove the hardcoded value
		CentralServiceDAOImpl daoImpl2 = new CentralServiceDAOImpl();
		ArrayList sliceDataList2 = daoImpl2.getSliceData(vo);
		//Start - End Slice data
		
		Slices s2 = rootDoc.addNewSlices();
		Iterator iterator3 = sliceDataList2.iterator();
		
		while (iterator3.hasNext()) { 
			vo = (CentralServiceVO) iterator3.next();
			paxinosSliceElement( s2.addNewSlice(), 1, vo);
		}

/*		Fiducials f = rootDoc.addNewFiducials();
		exampleFiducial(f.addNewFiducial(), 1);
*/
		return document;
	}

	public static void abaRefSliceElements(SliceType slice, int identifier, CentralServiceVO vo){

			if ( vo.getValueDirection().equalsIgnoreCase("front") ) { 
				slice.setOrientation(SliceType.Orientation.CORONAL);

				//(Derived from the coordinate system from Ilya - specific to ABA_REF space only)
				if ( vo.getPlusX().equalsIgnoreCase("left") ) { 
					slice.setXOrientation("right");
				} 
				if ( vo.getPlusY().equalsIgnoreCase("down") ) {
					slice.setYOrientation("ventral"); 
				}
				
			} else if ( vo.getValueDirection().equalsIgnoreCase("right") ) {
				slice.setOrientation(SliceType.Orientation.SAGITTAL);
			} else {
				slice.setOrientation(SliceType.Orientation.HORIZONTAL);
			}

			System.out.println("Value is - " + vo.getSlideValue());
			System.out.println("Again Value is - " + Float.parseFloat(vo.getSlideValue()));

			//FIXME - Ask Dave to change the 
			slice.setConstant(Math.round(Float.parseFloat(vo.getSlideValue())));
			slice.setCode(vo.getSliceID());

	}

	
	public static void paxinosSliceElement(SliceType slice, int identifier, CentralServiceVO vo){

		if ( vo.getValueDirection().equalsIgnoreCase("front") ) { 
			slice.setOrientation(SliceType.Orientation.CORONAL); 

			//(Derived from the coordinate system from Ilya - specific to PAXINOS space only)
			if ( vo.getPlusX().equalsIgnoreCase("right") ) { 
				slice.setXOrientation("left");
			} 
			if ( vo.getPlusY().equalsIgnoreCase("down") ) {
				slice.setYOrientation("ventral"); 
			}

		} else if ( vo.getValueDirection().equalsIgnoreCase("right") ) {
			slice.setOrientation(SliceType.Orientation.SAGITTAL);

			//(Derived from the coordinate system from Ilya - specific to PAXINOS space only)
			if ( vo.getPlusY().equalsIgnoreCase("down") ) { 
				slice.setXOrientation("ventral");
			} 
			if ( vo.getPlusZ().equalsIgnoreCase("left") ) {
				slice.setYOrientation("right"); 
			}

		} else {
			slice.setOrientation(SliceType.Orientation.HORIZONTAL);
		}
		
		System.out.println("Value is - " + vo.getSlideValue());
		System.out.println("Again Value is - " + Float.parseFloat(vo.getSlideValue()));

		//FIXME - Ask Dave to change the int to float
		slice.setConstant(Math.round(Float.parseFloat(vo.getSlideValue())));
		slice.setCode(vo.getSliceID());

	}

}
