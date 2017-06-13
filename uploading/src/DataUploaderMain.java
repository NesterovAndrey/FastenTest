import core.StringConstants;
import core.dataConsumer.IDataConsumer;
import core.dataProducer.IDataProducer;
import sun.tools.jar.CommandLine;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import static core.environment.Environment.fillEnvironment;

public class DataUploaderMain {


    public static void main(String[] args) {
        List<String> argList = null;
        try {
            argList = Arrays.asList(CommandLine.parse(args));

            fillEnvironment(StringConstants.SELECT, "-s", "all", argList);
            fillEnvironment(StringConstants.FROM, "-f", "2017-01-01", argList);
            fillEnvironment(StringConstants.TO, "-t", "2017-12-31", argList);
            fillEnvironment(StringConstants.VALUE, "-v", "2017-01-01", argList);
            fillEnvironment(StringConstants.DRIVER, "-d", "com.mysql.jdbc.Driver", argList);
            fillEnvironment(StringConstants.DB_CONNECTION_URL, "-db", "jdbc:mysql://localhost/fastentest", argList);
            fillEnvironment(StringConstants.DB_TABLE, "-dt", "test_data", argList);
            fillEnvironment(StringConstants.USER, "-u", "root", argList);
            fillEnvironment(StringConstants.PASSWORD, "-p", "", argList);
            fillEnvironment(StringConstants.RESULT_FOLDER, "-rf", "result", argList);
            fillEnvironment(StringConstants.RESULT_FILE, "-rfl", "result.csv.gz", argList);
            fillEnvironment(StringConstants.INPUT_FOLDER, "-if", "result", argList);
            fillEnvironment(StringConstants.INPUT_FILE, "-ifl", "result.csv.gz", argList);
            fillEnvironment(StringConstants.FILE_TYPE, "-ft", ".csv.gz", argList);
            fillEnvironment(StringConstants.TIMEOUT, "-to", "5", argList);
            CountDownLatch countDownLatch = new CountDownLatch(2);

            IDataProducer dataProducer = Configuration.getDataProducer();
            Configuration.executorService().submit(() ->
            {
                ForkJoinPool forkJoinPool = new ForkJoinPool();
                forkJoinPool.invoke(new RecursiveAction() {
                    @Override
                    protected void compute() {
                        while (!dataProducer.isCompleted())
                            dataProducer.produce();
                        countDownLatch.countDown();
                    }
                });
            });
            IDataConsumer dataConsumer = Configuration.getDataConsumer();
            Configuration.executorService().submit(() ->
            {
                ForkJoinPool forkJoinPool = new ForkJoinPool();
                forkJoinPool.invoke(new RecursiveAction() {
                    @Override
                    protected void compute() {
                        dataConsumer.consume();
                    }
                });

                countDownLatch.countDown();
            });
            countDownLatch.await();
            Configuration.executorService().shutdown();
            System.out.println("END");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
