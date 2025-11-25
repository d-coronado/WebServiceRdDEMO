package org.dcoronado.WebServiceRdDemo.Shared.Domain.Dto.Transformer;

import java.util.ArrayList;
import java.util.List;

public abstract class DtoTransformer<T, U> implements IDtoTransformer<T, U> {

    @Override
    public Iterable<T> fromObjects(Iterable<U> items) {
        List<T> result = new ArrayList<>();
        for (U item : items) {
            result.add(fromObject(item));
        }
        return result;
    }

    public abstract T fromObject(U item);
}
