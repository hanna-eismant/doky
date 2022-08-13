package org.hkurh.doky.facades;

import org.modelmapper.ModelMapper;

public class MapperFactory {

    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
    }

    private MapperFactory() {
    }

    public static ModelMapper getModelMapper() {
        return modelMapper;
    }
}
