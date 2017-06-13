import core.StringConstants;
import core.data.CadastralEntry;
import core.dataProducer.IDataProducer;
import core.dataConsumer.IDataConsumer;
import sun.tools.jar.CommandLine;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static core.environment.Environment.fillEnvironment;

public class UnloadingMain {

    public static void main(String[] args) {
        try {
            List<String> argList = Arrays.asList(CommandLine.parse(args));

            fillEnvironment(StringConstants.SELECT, "-s", "all", argList);
            fillEnvironment(StringConstants.FROM, "-f", "2016-01-01", argList);
            fillEnvironment(StringConstants.DATE, "-date", "2016-01-01", argList);
            fillEnvironment(StringConstants.YEAR, "-year", "2016", argList);
            fillEnvironment(StringConstants.MONTH, "-month", "01", argList);
            fillEnvironment(StringConstants.TO, "-t", "2017-12-31", argList);
            fillEnvironment(StringConstants.VALUE, "-v", "2017-01-01", argList);
            fillEnvironment(StringConstants.DRIVER, "-d", "com.mysql.jdbc.Driver", argList);
            fillEnvironment(StringConstants.DB_CONNECTION_URL, "-db", "jdbc:mysql://localhost/fastentest", argList);
            fillEnvironment(StringConstants.DB_TABLE, "-dt", "test_data", argList);
            fillEnvironment(StringConstants.USER, "-u", "root", argList);
            fillEnvironment(StringConstants.PASSWORD, "-p", "", argList);
            fillEnvironment(StringConstants.RESULT_FOLDER, "-rf", "result", argList);
            fillEnvironment(StringConstants.RESULT_FILE, "-rfl", "result.csv.gz", argList);
            fillEnvironment(StringConstants.FILE_TYPE, "-ft", ".csv.gz", argList);
            fillEnvironment(StringConstants.REQUEST, "-req", StringConstants.SELECT_ALL_REQUEST, argList);
            fillEnvironment(StringConstants.TIMEOUT, "-to", "5", argList);

            IDataProducer<Collection<CadastralEntry>> sqlDataProducer = UnloadingConfiguration.getSqlDataProducer();
            IDataConsumer dataConsumer = UnloadingConfiguration.getDataConsumer();

            CountDownLatch countDownLatch=new CountDownLatch(2);

            //Мжно запускать выгрузку данных в несколько потоков. Если создать несколько IDataProducer
            UnloadingConfiguration.executorService().submit(() -> {
                while (!sqlDataProducer.isCompleted()) {
                    sqlDataProducer.produce();
                }
                countDownLatch.countDown();
            });
            UnloadingConfiguration.executorService().submit(()->
            {
                dataConsumer.consume();
                countDownLatch.countDown();

            });
            countDownLatch.await();
            UnloadingConfiguration.executorService().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
