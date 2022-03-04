package jason.fuzzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.ObjectName;
import javax.swing.JOptionPane;

import jason.JasonException;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.directives.DirectiveProcessor;
import jason.asSyntax.directives.Include;
import jason.infra.centralised.CentralisedAgArch;
import jason.infra.centralised.RunCentralisedMAS;
import jason.infra.repl.ReplAgGUI;
import jason.mas2j.AgentParameters;
import jason.runtime.MASConsoleGUI;
import jason.runtime.MASConsoleLogHandler;
import jason.runtime.RuntimeServicesFactory;
import jason.runtime.Settings;
import jason.runtime.SourcePath;
import jason.util.Config;


public class RunMASCustomized extends RunCentralisedMAS {
    public static void main(String[] args) throws JasonException {
        logger = Logger.getLogger(RunMASCustomized.class.getName());
        RunMASCustomized r = new RunMASCustomized();    
        runner = r;    
        r.init(args);
        r.registerMBean();
        r.create();
        r.start();
        System.out.println("########### Started and will wait for the end");
        r.waitEnd();
        System.out.println("########### Reached the end");
        r.finish(0, false, 0);
        try {
            System.out.println();
            if (Files.size(Path.of(stopMASFileName, "")) > 0){
                System.out.println("should break application!");
                assert false;
            }
                
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("########### Application is done");
    }
}