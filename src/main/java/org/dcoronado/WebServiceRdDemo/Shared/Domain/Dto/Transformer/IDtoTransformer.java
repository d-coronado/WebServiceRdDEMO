package org.dcoronado.WebServiceRdDemo.Shared.Domain.Dto.Transformer;

public interface IDtoTransformer<T, U> {
    T fromObject(U u);

    Iterable<T> fromObjects(Iterable<U> u);
}
