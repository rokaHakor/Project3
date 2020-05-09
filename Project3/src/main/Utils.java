package main;

import org.jdatepicker.JDatePicker;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static Date getDateFromPicker(JDatePicker picker) {
        Calendar cal = Calendar.getInstance();
        cal.set(picker.getModel().getYear(), picker.getModel().getMonth(), picker.getModel().getDay());

        return cal.getTime();
    }
}
