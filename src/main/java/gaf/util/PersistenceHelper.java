package gaf.util;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;

public class PersistenceHelper {

    private static Logger log = Logger.getLogger(PersistenceHelper.class);

    public static Object forceSingleResult(Collection<?> candidates) {
        Object selected = null;
        if (candidates != null && candidates.size() > 0) {
            Iterator<?> candidateObjects  = candidates.iterator();
            if (candidateObjects.hasNext()) {
                selected = candidateObjects.next();
            }
            if (candidateObjects.hasNext()) {
                log.warn("Hay [" + candidates.size() + "] resultados cuando busco uno solo. Devuelvo: [" + selected + "]");
            }
        }
        return selected;
    }
}
