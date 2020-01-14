package com.comic.mario.ui.present;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseUtil {

    public static String parseOption(Element element, String els) {
        if (null == els.split("@") || els.split("@").length == 0) {
            if (els.split("@").length == 1) {
                String option = els.split("!")[0];
                return "" + doOptiton(element, option, "");
            } else {
                String option = els.split("!")[0];
                String value = els.split("!")[1];
                return "" + doOptiton(element, option, value);
            }
        } else {
            String[] array = els.split("@");
            Object object = null;
            for (String el : array) {
                if (el.split("!").length == 1) {
                    String option = el.split("!")[0];
                    if (object == null) {
                        object = doOptiton(element, option, "");
                    } else if (object instanceof Element) {
                        object = doOptiton((Element) object, option, "");
                    } else if (object instanceof Elements) {
                        object = doOptiton((Elements) object, option, "");
                    }
                } else {
                    String option = el.split("!")[0];
                    String value = el.split("!")[1];
                    if (object == null) {
                        object = doOptiton(element, option, value);
                    } else if (object instanceof Element) {
                        object = doOptiton((Element) object, option, value);
                    } else if (object instanceof Elements) {
                        object = doOptiton((Elements) object, option, value);
                    }
                }
            }
            return "" + object;
        }
    }

    public static String parseOption(Elements element, String els) {
        if (null == els.split("@") || els.split("@").length == 0) {
            if (els.split("@").length == 1) {
                String option = els.split("!")[0];
                return "" + doOptiton(element, option, "");
            } else {
                String option = els.split("!")[0];
                String value = els.split("!")[1];
                return "" + doOptiton(element, option, value);
            }
        } else {
            String[] array = els.split("@");
            Object object = null;
            for (String el : array) {
                if (el.split("!").length == 1) {
                    String option = el.split("!")[0];
                    if (object == null) {
                        object = doOptiton(element, option, "");
                    } else if (object instanceof Element) {
                        object = doOptiton((Element) object, option, "");
                    } else if (object instanceof Elements) {
                        object = doOptiton((Elements) object, option, "");
                    }
                } else {
                    String option = el.split("!")[0];
                    String value = el.split("!")[1];
                    if (object == null) {
                        object = doOptiton(element, option, value);
                    } else if (object instanceof Element) {
                        object = doOptiton((Element) object, option, value);
                    } else if (object instanceof Elements) {
                        object = doOptiton((Elements) object, option, value);
                    }
                }
            }
            return "" + object;
        }
    }

    private static Object doOptiton(Element element, String option, String value) {
        switch (option) {
            case "select":
                return element.select("" + value);
            case "attr":
                return element.attr("" + value);
            case "text":
                return element.text();
            default:
                return "";
        }
    }

    private static Object doOptiton(Elements element, String option, String value) {
        switch (option) {
            case "select":
                return element.select("" + value);
            case "attr":
                return element.attr("" + value);
            case "text":
                return element.text();
            case "get":
                return element.get(Integer.parseInt(value));
            case "first":
                return element.first();
            case "last":
                return element.last();
            default:
                return "";
        }
    }
}
