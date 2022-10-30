package jdbc_servlets.service.converter;

public interface Converter<E, T> {

    E from(T entity);

    T to(E entity);
}
