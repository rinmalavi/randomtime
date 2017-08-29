package de.atron.randomtime.root;

import de.atron.randomtime.root.path.AtTimeRandomResource;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.netflix.ribbon.RibbonArchive;

public class Main {

    public static void main(String[] args) throws Exception {

        Logger logger = Logger.getLogger("main");
        int port = Integer.valueOf(System.getProperty("swarm.http.port"));

        logger.infof("Startup at port %d", port);

        Swarm swarm = new Swarm();
        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "root.war");
        deployment.addResource(AtTimeRandomResource.class);
        deployment.addAllDependencies();
        deployment.as(RibbonArchive.class).advertise("root");
        swarm.start().deploy(deployment);
    }
}
