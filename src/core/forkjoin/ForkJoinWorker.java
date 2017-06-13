package core.forkjoin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ForkJoinWorker<T,C> implements IForkJoinWorker<T,C> {
    private final Supplier<C> resultSupply;
    private final BiConsumer<C,T> resultConsumer;

    private final List<RecursiveTask<T>> tasks=new CopyOnWriteArrayList<>();

    public ForkJoinWorker(Supplier<C> resultSupply, BiConsumer<C, T> resultConsumer) {
        this.resultSupply = resultSupply;
        this.resultConsumer = resultConsumer;
    }

    @Override
    public void submitTask(Supplier<T> supply) {
        submitTask(supply,true);
    }

    @Override
    public void submitTask(Supplier<T> supply, boolean forkImmediate) {
        RecursiveTask<T> task=new RecursiveTask<T>() {
            @Override
            protected T compute() {
                return supply.get();
            }
        };
        tasks.add(task);
        if (forkImmediate) task.fork();
    }

    @Override
    public void fork() {
        tasks.forEach((tRecursiveTask -> tRecursiveTask.fork()));
    }

    @Override
    public C join() {
        C result=resultSupply.get();
        tasks.stream().forEach((task)->
        {
            resultConsumer.accept(result,task.join());
        });
        tasks.clear();
        return result;
    }
}
