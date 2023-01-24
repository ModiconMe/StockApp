package io.modicon.cqrsbus;

public interface CommandHandler<R, C extends Command<R>>{

    R handle(C command);

}
