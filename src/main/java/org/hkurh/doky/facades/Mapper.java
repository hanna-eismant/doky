package org.hkurh.doky.facades;

import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.entities.UserEntity;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class Mapper {

    private static final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    static {
        mapperFactory.classMap(UserEntity.class, UserDto.class).byDefault().register();
    }

    private Mapper() {
        throw new IllegalStateException();
    }

    public static MapperFacade getMapper() {
        return mapperFactory.getMapperFacade();
    }
}
