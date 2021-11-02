package edu.upf.taln.mindspaces.client;

import edu.upf.taln.welcome.slas.client.Main;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rcarlini
 */
public class MainTest {
    
    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testHelp() throws Exception {

        String[] args = new String[]{"-h"};
        Main.main(args);
    }
    
}
