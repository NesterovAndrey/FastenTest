package core.forkjoin;

public interface IForkJoinPerformer {
    void submitTask(Runnable action);
    void submitTask(Runnable action,boolean forkImmediate);
    void fork();
    void join();
}
