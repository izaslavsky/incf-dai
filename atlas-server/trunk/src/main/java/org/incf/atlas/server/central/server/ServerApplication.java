package org.incf.atlas.server.central.server;

import org.incf.atlas.server.central.resource.CapabilitiesResource;
import org.incf.atlas.server.central.resource.FaviconResource;
import org.incf.atlas.server.central.resource.GenesByPOIResource;
import org.incf.atlas.server.central.resource.PingResource;
import org.incf.atlas.server.central.resource.StructureNamesByPOIResource;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class ServerApplication extends Application {

	private static final Logger logger = LoggerFactory.getLogger(
			ServerApplication.class);

	@Override
	public Restlet createRoot() {

		Router router = new Router(getContext());

		// attach resource handlers based on the URI
		router.attach("/favicon.ico", FaviconResource.class);

		router.attach("/ping/{pingType}", PingResource.class);
		
		/* GetCapabilities
		 * ?request=GetCapabilities&output=xml
		 */
		router.attach("/?Request=GetCapabilities", 
				CapabilitiesResource.class);
		router.attach("/?Request=GetCapabilities"
				+ "&ResponseFormat={responseFormat}", 
				CapabilitiesResource.class);
		
		/* GetGenesByPOI
		 * ?Request=Execute&Identifier=GetGenesByPOI
		 * &DataInputs=srsCode=WHS;x=263.7;y=159.4;z=227.8
		 * &ResponseFormat=text/xml
		 */
		router.attach("?Request=Execute&Identifier=GetGenesByPOI"
				+ "&DataInputs={dataInputs}"
				+ "&ResponseFormat={responseFormatA}/{responseFormatB}",
				GenesByPOIResource.class);
		router.attach("?Request=Execute&Identifier=GetGenesByPOI"
				+ "&DataInputs={dataInputs}",
				GenesByPOIResource.class);

		//GetStructureNamesByPOI
		//             ?Request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsCode=a;x=1;y=2;z=3;vocabulary=ss;filter=kk
		router.attach("?Request=Execute&Identifier=GetStructureNamesByPOI"
				+ "&DataInputs={dataInputs}",
				StructureNamesByPOIResource.class);

		return router;
	}

	public static void main(String[] args) {
		
		// redirect restlet logging to slf4j
		// see http://wiki.restlet.org/docs_1.1/13-restlet/48-restlet/101-restlet.html
		// "Replacing default JDK logging with log4j"
		SLF4JBridgeHandler.install();
		
		
		StringBuilder buf = new StringBuilder();
		buf.append("\n");
		buf.append("       *******************************************\n");
		buf.append("       *          Starting INCF Server           *\n");
		buf.append("       *******************************************\n");
		buf.append("\n  Server version   : ");
		buf.append("\nWaiting for connections ...");
		logger.info(buf.toString());
		
		try {
			Component component = new Component();
			component.getServers().add(Protocol.HTTP, 8182);
			component.getDefaultHost().attach(new ServerApplication());
			component.start();

		} catch (Exception e) {
			logger.error("Exception in main().", e);
		}
	}
	
}
