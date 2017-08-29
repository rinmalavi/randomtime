package de.atron.randomtime.deadletters;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.netflix.ribbon.RibbonArchive;
import de.atron.randomtime.deadletters.path.AllPathsResource;

public class Main {

    public static void main(String[] args) throws Exception {
        Swarm swarm = new Swarm();
        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "deadletters.war");
        deployment.addResource(AllPathsResource.class);
        deployment.addAllDependencies();
        deployment.as(RibbonArchive.class).advertise("deadletters");
        swarm.start().deploy(deployment);
    }
}
