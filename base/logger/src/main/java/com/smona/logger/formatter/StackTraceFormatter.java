package com.smona.logger.formatter;

public class StackTraceFormatter implements Formatter<StackTraceElement[]> {
    @Override
    public String format(StackTraceElement[] stackTrace,boolean printOneline) {
        if (stackTrace == null || stackTrace.length == 0) {
            return null;
        } else {
            return "__" + stackTrace[0].toString()+"\n";
        }
    }


}
