package psl.workflakes.littlejil;

import laser.littlejil.Diagram;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Keeps track of resources for different LittleJIL diagrams.
 * @author matias
 */
public class LittleJILResourceTable implements Serializable {

    private Hashtable table;

    public LittleJILResourceTable() {
        table = new Hashtable();
    }

    public void addResource(Diagram diagram, String name, Object resource) {

        Hashtable diagramAssets = (Hashtable) table.get(diagram);
        if (diagramAssets == null) {
            diagramAssets = new Hashtable();
            table.put(diagram, diagramAssets);
        }

        diagramAssets.put(name, resource);

    }

    public Object getResource(Diagram diagram, String name) {

        Hashtable diagramAssets = (Hashtable) table.get(diagram);
        if (diagramAssets == null) {
            return null;
        }

        return diagramAssets.get(name);
    }

}
