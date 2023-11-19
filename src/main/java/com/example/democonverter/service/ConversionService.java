package com.example.democonverter.service;

import java.io.IOException;

public interface ConversionService {

    Runnable convert(String name, int newWidth, int newHeight) throws IOException;


}
