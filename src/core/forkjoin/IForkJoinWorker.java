package core.forkjoin;

import java.util.function.Supplier;

public interface IForkJoinWorker<T,C> {
    void submitTask(Supplier<T> supply);
    void submitTask(Supplier<T> supply,boolean forkImmediate);
    void fork();
    C join();

}
