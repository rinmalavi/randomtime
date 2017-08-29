package de.atron.randomtime.it;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(Arquillian.class)
public class RandomTimeRootIT {

    @Drone
    WebDriver browser;

    @Test
    public void rootIt() {
        String rootPort = "9000";

        browser.navigate().to("http://localhost:" + rootPort);

        String browserPageSource = browser.getPageSource();

        System.out.println(browserPageSource);

        assertThat(browserPageSource).contains("2017");
        assertThat(browserPageSource).contains(",\"randomString\":");
    }

}
