package org.hkurh.doky

import org.modelmapper.ModelMapper

class MapperFactory private constructor() {
    companion object {
        val modelMapper: ModelMapper by lazy {
            ModelMapper()
        }
    }
}
