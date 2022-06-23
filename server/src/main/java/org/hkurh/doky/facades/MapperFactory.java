package org.hkurh.doky.facades;

import org.modelmapper.ModelMapper;

public class MapperFactory {

    private static final ModelMapper userModelMapper;

    static {
        userModelMapper = new ModelMapper();

    }

    private MapperFactory() {
    }

    public static ModelMapper getUserModelMapper() {
        return userModelMapper;
    }
}
