package com.app.infrastructure.mongo.config.converter;

import com.app.domain.vo.Position;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.Map;

@WritingConverter
public class PositionMapToBSONObjectConverter implements Converter<Map<Position, Boolean>, BSONObject> {


    @Override
    public BSONObject convert(Map<Position, Boolean> map) {

        BasicBSONObject basicBSONObject = new BasicBSONObject();

         map.forEach((key,val) -> basicBSONObject.put(key.toString(), val));

        return basicBSONObject;
    }
}
