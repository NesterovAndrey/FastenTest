package dataUploadProducer;

import core.dataProducer.IProducerFactory;
import core.dataProducer.PartialDataProducer;
import core.forkjoin.IForkJoinWorker;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Возвращает данные из файлов лежащих в папках.
 * @param <T> типо вощвращаемых данных
 */
//Можно отрефакторить и вынести общий с FolderDataProducer функционал отдельно.
//Но не стал тратить на это время.
public class CSVFilesDataProducer<T> extends PartialDataProducer<Collection<T>, Collection<PartialDataProducer<T, ?>>> {

    //количество папок обрабатываемых за один раз
    private static final Integer OFFSET = 0;
    private static final Integer LIMIT = 1;

    private final Collection<Folder> folders;
    private final IProducerFactory<Folder, PartialDataProducer<T, ?>> producerFactory;

    private IForkJoinWorker<T, Collection<T>> forkJoinWorker;

    private final Map<Folder, PartialDataProducer<T, ?>> producersInWork = new HashMap<>();

    public CSVFilesDataProducer(Integer offset, Integer limit, Collection<Folder> folders,
                                IProducerFactory<Folder, PartialDataProducer<T, ?>> producerFactory,
                                IForkJoinWorker<T, Collection<T>> forkJoinWorker) {
        super(offset, limit);
        this.folders =new ArrayList<>(folders);
        this.producerFactory = producerFactory;
        this.forkJoinWorker = forkJoinWorker;
    }

    public CSVFilesDataProducer(Collection<Folder> folders,
                                IProducerFactory<Folder, PartialDataProducer<T, ?>> producerFactory,
                                IForkJoinWorker<T, Collection<T>> forkJoinWorker) {
        this(OFFSET, LIMIT, folders, producerFactory, forkJoinWorker);
    }

    @Override
    protected Collection<T> rertrieve(Collection<PartialDataProducer<T, ?>> data) {
        if (data.isEmpty()) {
            complete();
            return Collections.emptyList();
        }
        //обрабатываем папки порционно
        data.forEach((producer)->
        {
            //запускаем таск на обработку папок
            forkJoinWorker.submitTask(producer::produce);
        });
        //ждём выполненния
        return forkJoinWorker.join();
    }

    @Override
    protected Collection<PartialDataProducer<T, ?>> retrieveNext() {

        //Выбираем из запущеных обработчиков те которые еще не выполнились
        Collection<PartialDataProducer<T, ?>> result;

            result = producersInWork.entrySet().stream().filter((entry) ->
            {
                return !entry.getValue().isCompleted();
            }).limit(getLimit()).map((Map.Entry::getValue)).collect(Collectors.toList());

        int count = getLimit()-result.size();
        if (this.folders.size() > getOffset()) {
            //Если список не заполнен заполняем новыми обработчиками
            result.addAll(folders.stream()
                    .filter((folder) -> (!producersInWork.containsKey(folder))).limit(count)
                    .map((folder) ->
                    {
                        PartialDataProducer<T, ?> producer = this.producerFactory.create(folder);
                        this.producersInWork.put(folder, producer);
                        moveOffset(1);
                        return producer;
                    }).collect(Collectors.toList()));

        }
        return result;
    }
}
