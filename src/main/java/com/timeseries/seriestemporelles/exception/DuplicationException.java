package com.timeseries.seriestemporelles.exception;

import com.sun.jdi.request.DuplicateRequestException;

public class DuplicationException extends DuplicateRequestException {
    public DuplicationException(String exception) {
        super(exception);
    }
}
