package core.forkjoin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveAction;

/**
 * Запускает RecursiveAction
 */
public class ForkJoinPerformer implements IForkJoinPerformer {

    private final List<RecursiveAction> actions=new CopyOnWriteArrayList<>();

    @Override
    public void submitTask(Runnable action) {
        submitTask(action,true);
    }

    @Override
    public void submitTask(Runnable activity, boolean forkImmediate) {
        RecursiveAction action=new RecursiveAction() {
            @Override
            protected void compute() {
                activity.run();
            }
        };

        actions.add(action);
        if (forkImmediate) action.fork();
    }

    @Override
    public void fork() {
        actions.forEach((tRecursiveAction-> tRecursiveAction.fork()));
    }
    @Override
    public void join() {
        actions.stream().forEach((action)->
        {
            action.join();
        });
    }
}
