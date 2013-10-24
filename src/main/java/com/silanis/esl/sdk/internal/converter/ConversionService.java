package com.silanis.esl.sdk.internal.converter;

import com.silanis.esl.sdk.Field;
import com.silanis.esl.sdk.internal.converter.sdk.FieldConverter;

public class ConversionService {

    private ConversionService() {}

    public static com.silanis.esl.api.model.Field convert( Field field ) {
        return new FieldConverter( field ).getESLField();
    }
}