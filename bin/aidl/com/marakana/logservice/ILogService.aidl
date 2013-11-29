package com.marakana.logservice;

import com.marakana.logservice.Message;

interface ILogService {
    void log_d(String tag, String message);
    void log(in Message msg);
    }