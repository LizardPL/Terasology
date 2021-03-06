package org.terasology.entitySystem.metadata.core;

import com.google.common.collect.Lists;
import org.terasology.entitySystem.metadata.TypeHandler;
import org.terasology.protobuf.EntityData;

import java.util.List;

/**
 * @author Immortius <immortius@gmail.com>
 */
public class IntTypeHandler implements TypeHandler<Integer> {

    public EntityData.Value serialize(Integer value) {
        return EntityData.Value.newBuilder().addInteger(value).build();
    }

    public Integer deserialize(EntityData.Value value) {
        if (value.getIntegerCount() > 0) {
            return value.getInteger(0);
        }
        return null;
    }

    public Integer copy(Integer value) {
        return value;
    }

    public EntityData.Value serialize(Iterable<Integer> value) {
        return EntityData.Value.newBuilder().addAllInteger(value).build();
    }

    public List<Integer> deserializeList(EntityData.Value value) {
        return Lists.newArrayList(value.getIntegerList());
    }
}
