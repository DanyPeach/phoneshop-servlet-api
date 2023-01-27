package com.es.phoneshop.web.utils;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;

public class ItemPropertyParsing {
    public int getQuantity(String quantity, HttpServletRequest req) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(req.getLocale());
        return numberFormat.parse(quantity).intValue();
    }
}
