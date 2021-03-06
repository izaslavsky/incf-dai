package org.incf.central.atlas.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXML {

	public static void main ( String [] s ) {
	
		ReadXML readXML = new ReadXML();

		try { 

			//Step 1 - read xml response string
			//String xmlString = readXML.convertFromURLToString("http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3");
			//System.out.println("XML String is - " + xmlString);

/*			
			//Step 2 - Create a xml document from the string 
			Element xmlElement = readXML.getDocumentElementFromString(xmlString);

			//Step 3 - Get a value from the xml document
			String [] elementValues = readXML.getStringValuesForXMLTag(xmlElement, "ows:Identifier");

			for (int i = 0; i < elementValues.length; i++) {
				System.out.println("Element Value is - " + elementValues[i]);
			}
*/

			//String xmlString = "http://incf-dev-local.crbs.ucsd.edu/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3";
			//String xmlString = "http://132.239.131.188:8080/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:sagittal;tolerance=3";
			//xmlString = "http://132.239.131.188:8080/atlas-central?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsname=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3";
			//String xmlString = "http://incf-dev-local.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_AGEA_1.0;x=6600;y=4000;z=5600;filter=maptype:coronal";
			//String xmlString = "http://incf-dev-local.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&identifier=GetGenesByPOI&DataInputs=srsName=Mouse_AGEA_1.0;x=6600;y=4000;z=5600;filter=NONE";
			String xmlString = "http://incf-dev-local.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_Paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=;filter=structureset:anatomic";
			ArrayList list = new ArrayList();
			CentralServiceVO cvo = null;
			list = readXML.getStructureData(xmlString, list, "ABA", cvo);

			xmlString = "http://incf-dev-local.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_Paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=;filter=NONE";
			cvo = null;
			list = readXML.getStructureData(xmlString, list, "UCSD", cvo);
			
			xmlString = "http://incf-dev-local.crbs.ucsd.edu/whs/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_Paxinos_1.0;x=1;y=4.3;z=1.78;vocabulary=;filter=NONE";
			cvo = null;
			list = readXML.getStructureData(xmlString, list, "WHS", cvo);
			//list = readXML.get2DImageDataList(xmlString);
			//list = readXML.getGenesDataList(xmlString, list);
			System.out.println("*****************List = "+list.size());
			CentralServiceVO vo1 = null;
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				vo1 = (CentralServiceVO)iterator.next();
				System.out.println(vo1.getStructureCode());
				System.out.println(vo1.getSrsName());
			}			
			
			
			CentralServiceVO vo = null;
			int z = 0;
			while (iterator.hasNext()) {
				
				vo = (CentralServiceVO)iterator.next();
				if (vo.getFlag().equals("GENE") ){
					System.out.println("Inside Gene Symbol" + z++ +" Gene: "+ vo.getGeneSymbol());
				} else if (vo.getFlag().equals("EXPRESSIONLEVEL") ){
					System.out.println("Inside EXPRESSION " + z++ +" GenExpressionLevel: "+ vo.getExpressionLevelGeneSymbol());
				}
				//System.out.println("*****************URL = "+vo.getWms());
				//System.out.println("*****************TFW Values = "+vo.getTfwValues());
			}


/*	        ArrayList completeStructureList = new ArrayList();
			CentralServiceVO vo = new CentralServiceVO();
	        // 2a - Call the method from ABA Hub
	        String abaURL = "http://incf-dev.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine";
	        completeStructureList = readXML.getStructureData(abaURL, completeStructureList);

	        // 2b - Call the method from UCSD Hub
	        String ucsdURL = "http://incf-dev.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine";
	        completeStructureList = readXML.getStructureData(ucsdURL, completeStructureList);

	        // 2c - Call the method from WHS Hub
	        String whsURL = "http://incf-dev.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine";
	        completeStructureList = readXML.getStructureData(whsURL, completeStructureList);
			Iterator iterator = completeStructureList.iterator();

			KeyValueBean keyValue = null;
			while (iterator.hasNext()) {
				keyValue = (KeyValueBean)iterator.next();
				
				System.out.println("Name = "+keyValue.getKey());
				System.out.println("Description = "+keyValue.getValue());
			}
*/			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public ArrayList get2DImageDataList(String urlString) {

		ArrayList list = new ArrayList();

		try {

			ReadXML readXML = new ReadXML();
			
			System.out.println("************************URLString is******************* - " + urlString);
			String xmlString = readXML.convertFromURLToString(urlString); 
			
			System.out.println("XMLString is - " + xmlString);
			list = readXML.getImageData(xmlString, list);
			System.out.println("List Size is - " + list.size());
			
			Iterator iterator = list.iterator();
/*			CentralServiceVO vo = null;
			while (iterator.hasNext()) {
				vo = (CentralServiceVO)iterator.next();
				//System.out.println("URL = "+vo.getWms());
				//System.out.println("TFW Values = "+vo.getTfwValues());
			}
*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}


	public ArrayList getGenesDataList(String urlString, ArrayList list) {

		try {

			ReadXML readXML = new ReadXML();
			
			System.out.println("************************URLString is******************* - " + urlString);
			String xmlString = readXML.convertFromURLToString(urlString); 
			
			System.out.println("XMLString is - " + xmlString);
			//list = readXML.getImageData(xmlString);
			list = readXML.getGenesData(xmlString);
			//System.out.println("List Size is - " + list.size());

			Iterator iterator = list.iterator();
/*			CentralServiceVO vo = null;
			while (iterator.hasNext()) {
				vo = (CentralServiceVO)iterator.next();
				//System.out.println("URL = "+vo.getWms());
				//System.out.println("TFW Values = "+vo.getTfwValues());
			}
*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	
	public ArrayList getStructureData(String urlString, ArrayList list, String hubName, CentralServiceVO cvo) {

		cvo = new CentralServiceVO();
		Element xmlElement = null;
		
		try {

			//System.out.println("1");
			ReadXML readXML = new ReadXML();
			//System.out.println("2");
			
			String xmlString = readXML.convertFromURLToString(urlString); 
			//System.out.println("3");

			xmlElement = readXML.getDocumentElementFromString(xmlString);
			//System.out.println("9");
			cvo.setStructureCode(readXML.getStringValueForXMLTag(xmlElement, "Code"));
			//System.out.println("10");
			cvo.setStructureDescription(readXML.getStringValueForXMLTag(xmlElement, "Description")); 
			//System.out.println("11");

			if (hubName.equalsIgnoreCase("aba")) {
				cvo.setSrsName(cvo.getStructureDescription()+"::Mouse_ABAreference_1.0");
				cvo.setFlag("ABA");
			} else if (hubName.equalsIgnoreCase("ucsd")) {
				cvo.setSrsName(cvo.getStructureDescription()+"::Mouse_Paxinos_1.0");
				cvo.setFlag("UCSD");
			} else if (hubName.equalsIgnoreCase("whs")) {
				cvo.setSrsName(cvo.getStructureDescription()+"::Mouse_WHS_0.9");
				cvo.setFlag("WHS");
			}
			
			list.add(cvo);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			xmlElement = null;
		}
	
		return list;
		
	}


	public ArrayList getCorrelationData(String urlString, ArrayList list) {

		KeyValueBean keyValue = new KeyValueBean();

		try {

			ReadXML readXML = new ReadXML();
			
			String xmlString = readXML.convertFromURLToString(urlString); 

			Element xmlElement = readXML.getDocumentElementFromString(xmlString);

			keyValue.setKey(readXML.getStringValueForXMLTag(xmlElement, "CorrelationUrl"));
			keyValue.setValue(readXML.getStringValueForXMLTag(xmlElement, "CorrelationUrl")); 
			
			list.add(keyValue);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return list;
		
	}

	
 	public String convertFromURLToString(String stringURL) {

 		String responseString = "";
		System.out.println("1" + stringURL);
		URL url = null;
		URLConnection urlCon = null;
		
 		try {
			url = new URL(stringURL);
			System.out.println("2");
			urlCon = url.openConnection();
			System.out.println("3");
			urlCon.setUseCaches(false);
			System.out.println("4");
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			System.out.println("5");
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				responseString = responseString + inputLine;
			}
			in.close();
		} catch (MalformedURLException ex) {
			System.out.println("^^^^ERROR1^^^^^");
			System.err.println(ex);
	    } catch (IOException ex) {
			System.out.println("^^^^ERROR2^^^^^");
			System.err.println(ex);
			responseString = "transformation-error";
			return responseString;
	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			url = null;
			urlCon = null;
		}
		return responseString.replace("&", "&amp;");
 	}

	
	/**
	 * Get root element from XML String
	 * 
	 * @param arg
	 *            XML String
	 * @return Root Element
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Element getDocumentElementFromString(String arg)
			throws ParserConfigurationException, SAXException, IOException {

		System.out.println("1 - before: " +arg);

		String xml = arg.trim().replaceFirst("^([\\W]+)<","<");

		DocumentBuilderFactory dbf;
		System.out.println("2 - after: " + xml);
		DocumentBuilder db;
		System.out.println("3");
		Document document;
		System.out.println("4");
		dbf = DocumentBuilderFactory.newInstance();
		System.out.println("5");
		db = dbf.newDocumentBuilder();
		System.out.println("6");
		document = db.parse(new InputSource(new StringReader(xml)));
		System.out.println("7");
		Element element = document.getDocumentElement();
		System.out.println("8");
		
		db = null;
		dbf = null;
		document = null;

		return element;

	}

	public static String[] getStringValuesForXMLTag(Element xmlElement, String key) {

		NodeList nl = xmlElement.getElementsByTagName(key);

		if (nl.getLength() > 0) {

			String[] output = new String[nl.getLength()];

			for ( int i = 0; i < nl.getLength(); i++ ) { 
			Node node = nl.item(i).getFirstChild();
			if (node != null) {
				output[i] = node.getNodeValue().trim();
			}
			}
			return output;
		}
		return null;
	}

	
	public static String getStringValueForXMLTag(Element xmlElement, String key) {

		NodeList nl = xmlElement.getElementsByTagName(key);

		if (nl.getLength() > 0) {

			String output = "";

			for ( int i = 0; i < nl.getLength(); i++ ) { 
			Node node = nl.item(i).getFirstChild();
			if (node != null) {
				output = node.getNodeValue().trim();
			}
			}
			return output;
		}
		
		return null;
	}

	
    public ArrayList getImageData(String data, ArrayList list) {

    	SAXBuilder builder = new SAXBuilder();
    	//ArrayList list = new ArrayList();
    	CentralServiceVO vo = null;
    	
    	try {

    		org.jdom.Document document = builder.build(new ByteArrayInputStream(data.getBytes()));

    		Iterator responseList = document.getRootElement().getChildren()
            .listIterator();

    		org.jdom.Element root = document.getRootElement();
    		//root.getChild("wps:ProcessOutputs").getChild("wps:Output").getChild("wps:Data").getChild("wps:ComplexData").getChild("ImagesResponse").getChild("Image2DCollection");
    		
    		System.out.println("****Data****" + data);
    		
    		Namespace ns = Namespace.getNamespace("http://www.opengis.net/wps/1.0.0");
            //while (responseList.hasNext()) { 

            	org.jdom.Element imageResponseElements = root.getChild("ProcessOutputs", ns).getChild("Output", ns).getChild("Data", ns).getChild("ComplexData", ns).getChild("ImagesResponse").getChild("Image2Dcollection");
            	
                if (imageResponseElements.getName().equalsIgnoreCase("Image2Dcollection")) {

                    Iterator image2DIterator = imageResponseElements.getChildren().listIterator();
                    
                    int z = 0;
                    while (image2DIterator.hasNext()) { 
                    	z = z++;
                    	vo = new CentralServiceVO();
                    	System.out.println("IMAGE-2DCOUNT: " + z);
                    	
                        org.jdom.Element image2DElement = (org.jdom.Element) image2DIterator.next();

	                    List columns = image2DElement.getChildren();
	                    for (int j = 0; j < columns.size(); j++) {
	                    	
	                    	org.jdom.Element column = (org.jdom.Element) columns.get(j);

	                    	if ( column.getName().equalsIgnoreCase("imageposition")) {
		                    	System.out.println("IMAGEPOSITION-COUNT: " + z);
	                    		//System.out.println("IMAGEPOSITION: " + z);	
	                    		List imagePositionList = column.getChildren();

		                        //System.out.println("ImagePosition List is - "+imagePositionList.size());
	    	                    for (int x = 0; x < imagePositionList.size(); x++) {

	    	                    	org.jdom.Element imagePositionElements = (org.jdom.Element) imagePositionList.get(x);
			                        if (imagePositionElements.getName().equalsIgnoreCase("ImagePlanePlacement") ) {
			                        	//System.out.println("TFW in ImagePlanePlacement: " + imagePositionElements.getText()); 
			                        	vo.setTfwValues(imagePositionElements.getText());
			                        } 

			                        if (imagePositionElements.getName().equalsIgnoreCase("corners")) {
				                        List cornerList = column.getChild("Corners").getChildren(); 
				                        
				                        //1st corner
				                        org.jdom.Element corner = (org.jdom.Element) cornerList.get(0);
		    	                    	//System.out.println("Corner::: "+corner.getAttributeValue("position"));
		    	                    	List pointsList = corner.getChildren();
			    	                    for (int l = 0; l < pointsList.size(); l++) {
			    	                    	org.jdom.Element point = (org.jdom.Element) pointsList.get(l);
			    	                    	//System.out.println("position::: "+point.getChildren().size());
			    	                    	List posList = point.getChildren();
				    	                    for (int m = 0; m < posList.size(); m++) {
				    	                    	org.jdom.Element pos = (org.jdom.Element) posList.get(m);
				    	                    	vo.setTopLeft(pos.getText());
				    	                    	//System.out.println("::Top Left::: "+vo.getTopLeft());
				    	                    }
			    	                    }

		                        	//2nd corner
	    	                    	corner = (org.jdom.Element) cornerList.get(1);
	    	                    	//System.out.println("Corner::: "+corner.getAttributeValue("position"));
	    	                    	pointsList = corner.getChildren();
		    	                    for (int l = 0; l < pointsList.size(); l++) {
		    	                    	org.jdom.Element point = (org.jdom.Element) pointsList.get(l);
		    	                    	//System.out.println("position::: "+point.getChildren().size());
		    	                    	List posList = point.getChildren();
			    	                    for (int m = 0; m < posList.size(); m++) {
			    	                    	org.jdom.Element pos = (org.jdom.Element) posList.get(m);
			    	                    	vo.setBottomLeft(pos.getText());
			    	                    	//System.out.println("::Bottom Left::: "+vo.getBottomLeft());
			    	                    }
		    	                    }
	    	                    	
		    	                    //3rd corner
	    	                    	corner = (org.jdom.Element) cornerList.get(2);
	    	                    	//System.out.println("Corner::: "+corner.getAttributeValue("position"));
	    	                    	pointsList = corner.getChildren();
		    	                    for (int l = 0; l < pointsList.size(); l++) {
		    	                    	org.jdom.Element point = (org.jdom.Element) pointsList.get(l);
		    	                    	//System.out.println("position::: "+point.getChildren().size());
		    	                    	List posList = point.getChildren();
			    	                    for (int m = 0; m < posList.size(); m++) {
			    	                    	org.jdom.Element pos = (org.jdom.Element) posList.get(m);
			    	                    	vo.setTopRight(pos.getText());
			    	                    	//System.out.println("::Top Right::: "+vo.getTopRight());
			    	                    }
		    	                    }

		    	                    //4th corner
	    	                    	corner = (org.jdom.Element) cornerList.get(3);
	    	                    	//System.out.println("Corner::: "+corner.getAttributeValue("position"));
	    	                    	pointsList = corner.getChildren();
		    	                    for (int l = 0; l < pointsList.size(); l++) {
		    	                    	org.jdom.Element point = (org.jdom.Element) pointsList.get(l);
		    	                    	//System.out.println("position::: "+point.getChildren().size());
		    	                    	List posList = point.getChildren();
			    	                    for (int m = 0; m < posList.size(); m++) {
			    	                    	org.jdom.Element pos = (org.jdom.Element) posList.get(m);
			    	                    	vo.setBottomRight(pos.getText());
			    	                    	//System.out.println("::Bottom Right::: "+vo.getBottomRight());
					                    	if (!vo.getBottomRight().equals("")) {
					                    		vo.setFlag("UCSD");
					                    	} else {
					                    		vo.setFlag("ABA");
					                    	}

			    	                    }
		    	                    }
//}				                        
			                        }
			                    }

	                    	if ( column.getName().equalsIgnoreCase("imageplaneequation")) {
	                    		vo.setImagePlaneEquation(column.getText());
	                    	}

                    } else if ( column.getName().equalsIgnoreCase("imagesource")) {
	                    		System.out.println("IMAGESOURCE-COUNT- " + z + column.getText());
		                    	vo.setSrsName(column.getAttribute("srsName").getValue());
		                        vo.setWms(column.getText());
		                        //System.out.println("WMS - " + vo.getWms());
	                }	                        

	                    }
                    	list.add(vo);
                    }
                    System.out.println("*** LIST IS: *** " +list.size());
                }
            //}

    	} catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;

    }

    
    public ArrayList getGenesData(String data) {

    	SAXBuilder builder = new SAXBuilder();
    	ArrayList list = new ArrayList();
    	CentralServiceVO vo = null;
    	
    	try {

    		org.jdom.Document document = builder.build(new ByteArrayInputStream(data.getBytes()));

    		Iterator responseList = document.getRootElement().getChildren()
            .listIterator();

    		org.jdom.Element root = document.getRootElement();
    		
    		Namespace ns = Namespace.getNamespace("http://www.opengis.net/wps/1.0.0");

        	org.jdom.Element genesResponseElements = root.getChild("ProcessOutputs", ns).getChild("Output", ns).getChild("Data", ns).getChild("ComplexData", ns).getChild("GenesResponse").getChild("GenesByPOI");

                if (genesResponseElements.getName().equalsIgnoreCase("GenesByPOI")) {

                    Iterator genesByPOIIterator = genesResponseElements.getChildren().listIterator();

                    int z = 0;
                    while (genesByPOIIterator.hasNext()) { 
                    	
                    	z++;
                    	
                    	System.out.println("********COUNT*********" +z);
                    	
                    	vo = new CentralServiceVO();
                    	//System.out.println("GENE-COUNT: " + z);

                        org.jdom.Element geneElement = (org.jdom.Element) genesByPOIIterator.next();
                        System.out.println("***GENE NAME***"+geneElement);
                        if (geneElement.getName().equalsIgnoreCase("Gene")) {
                            System.out.println("***Inside Gene Element***");
                        
                            vo.setFlag("GENE");
                            vo.setGeneName(geneElement.getChild("Name").getText());
	                        vo.setGeneSymbol(geneElement.getChild("Symbol").getText());
	                        vo.setGeneOrganism(geneElement.getChild("Organism").getText());
	                        
	                        
	                        System.out.println("Symbol: " + vo.getGeneSymbol());
	                        System.out.println("Name: " + vo.getGeneName());
	                        System.out.println("Organism: " + vo.getGeneOrganism());

	                        //for (int i = 0; i < geneElement.getChildren().size(); i++ ) {
		                        vo.setGenePrefix(geneElement.getChild("MarkerAccessionId").getChild("Prefix").getText());
		                        vo.setGeneIdentifier(geneElement.getChild("MarkerAccessionId").getChild("Identifier").getText());
		                        vo.setGeneFullIdentifier(geneElement.getChild("MarkerAccessionId").getChild("FullIdentifier").getText());

	                        	System.out.println("***Prefix***" + vo.getGenePrefix());
	                        	System.out.println("***Identifier***" + vo.getGeneIdentifier());
	                        	System.out.println("***FullIdentifier***" + vo.getGeneFullIdentifier());
	                        //}

                        } else if (geneElement.getName().equalsIgnoreCase("ExpressionLevel")) {
                            System.out.println("***Inside ExpressionLevel Element***");
                        
                            vo.setFlag("EXPRESSIONLEVEL");
                            vo.setExpressionLevelGeneSymbol(geneElement.getChild("GeneSymbol").getText());
	                        vo.setExpressionLevelCodeSpace(geneElement.getChild("Level").getAttributeValue("codeSpace"));
	                        vo.setExpressionLevelValue(geneElement.getChild("Level").getText());
	                        
	                        System.out.println("ExpressionLevelGeneSymbol: " + vo.getExpressionLevelGeneSymbol());
	                        System.out.println("ExpressionLevelCodeSpace: " + vo.getExpressionLevelCodeSpace());
	                        System.out.println("ExpressionLevelValue: " + vo.getExpressionLevelValue());

                        }

                        list.add(vo);
                    }
                   System.out.println("*** LIST IS: *** " +list.size());
                }

    	} catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;

    }


}
