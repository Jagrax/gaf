package gaf.util;

import java.util.Date;

public class FrontendValidator {

    public static boolean areEachDateAfter(Date... dates) {
        boolean first = true;
        Date a;
        for (Date date : dates) {
            if (date == null) continue;
            a = date;

            if (first) {
                first = !first;
                continue;
            }

            if (a.after(date)) {
                return false;
            }
        }
        return true;
    }

}
