package com.jmz.serverorder.exception.exceptions;

import com.jmz.serverorder.exception.base.BaseException;
 
public class ServersException extends BaseException {
    public ServersException(String message) {
        super("servers", message);
    }
} 