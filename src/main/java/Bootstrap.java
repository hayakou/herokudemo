
import java.io.File;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hayakou
 */
public class Bootstrap {

    public static void main(String[] args) {
        try {
            GlassFishProperties gfProps = new GlassFishProperties();
            gfProps.setPort("http-listener",
                    Integer.parseInt(System.getenv("PORT")));
            final GlassFish glassfish = GlassFishRuntime.bootstrap()
                    .newGlassFish(gfProps);
            glassfish.start();
            
            System.out.println("PORT >>>>>>>> " + System.getenv("PORT"));

            ScatteredArchive war = new ScatteredArchive("herokudemo",
                    ScatteredArchive.Type.WAR, new File("src/main/webapp"));
            war.addClassPath(new File("target/classes"));
            glassfish.getDeployer().deploy(war.toURI());

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println(glassfish + " shutdown now!!");
                        glassfish.dispose();
                    } catch (GlassFishException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
