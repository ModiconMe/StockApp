package io.modicon.cqrsbus;

public interface QueryHandler<R, Q extends Query<R>> {

    R handle(Q query);

}
