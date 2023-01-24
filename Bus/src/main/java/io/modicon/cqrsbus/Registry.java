package io.modicon.cqrsbus;

import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
@Component
public class Registry {

    private final Map<Class<? extends Command>, CommandHandlerProvider> commandHandlerProviders = new HashMap<>();
    private final Map<Class<? extends Query>, QueryHandlerProvider> queryHandlersProviders = new HashMap<>();

    public Registry(ApplicationContext applicationContext) {
        Map<String, CommandHandler> commandHandlersBeans = applicationContext.getBeansOfType(CommandHandler.class);
        commandHandlersBeans.values().forEach(h -> registerCommandHandler(applicationContext, h));

        Map<String, QueryHandler> queryHandlersBeans = applicationContext.getBeansOfType(QueryHandler.class);
        queryHandlersBeans.values().forEach(h -> registerQueryHandler(applicationContext, h));
    }

    private void registerCommandHandler(ApplicationContext applicationContext, CommandHandler commandHandler) {
        Class<?>[] handlerTypes = GenericTypeResolver.resolveTypeArguments(commandHandler.getClass(), CommandHandler.class); // return [Result.class, UserCommand.class]
        Class<? extends Command> commandType = (Class<? extends Command>) handlerTypes[1];
        commandHandlerProviders.put(commandType, new CommandHandlerProvider<>(applicationContext, commandHandler.getClass()));
    }

    private void registerQueryHandler(ApplicationContext applicationContext, QueryHandler queryHandler) {
        Class<?>[] handlerTypes = GenericTypeResolver.resolveTypeArguments(queryHandler.getClass(), QueryHandler.class);
        Class<? extends Query> queryType = (Class<? extends Query>) handlerTypes[1];
        queryHandlersProviders.put(queryType, new QueryHandlerProvider(applicationContext, queryHandler.getClass()));
    }

    <R, C extends Command<R>> CommandHandler<R, C> getCommandHandler(Class<C> commandType) {
        return commandHandlerProviders.get(commandType).get();
    }

    <R, Q extends Query<R>> QueryHandler<R, Q> getQueryHandler(Class<Q> queryType) {
        return queryHandlersProviders.get(queryType).get();
    }

}