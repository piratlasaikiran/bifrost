package org.bhavani.constructions;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.bhavani.constructions.config.ServerConfiguration;


public class Server extends Application<ServerConfiguration> {

    public static void main(String[] args) throws Exception {
        new Server().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void run(ServerConfiguration serverConfiguration, Environment environment) throws Exception {
    }
}
