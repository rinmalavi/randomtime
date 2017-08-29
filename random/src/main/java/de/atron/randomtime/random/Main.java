package de.atron.randomtime.random;

import de.atron.randomtime.random.path.RandomnessResource;
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
        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "random.war");
        deployment.addResource(RandomnessResource.class);
        deployment.addAllDependencies();
        deployment.as(RibbonArchive.class).advertise("random");
        swarm.start().deploy(deployment);
    }
}
