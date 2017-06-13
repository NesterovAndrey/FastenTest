package dataUploadProducer;

import core.dataProducer.IProducerFactory;
import core.dataProducer.PartialDataProducer;
import core.forkjoin.IForkJoinWorker;
import core.input.IInputStreamFactory;
import core.mapper.IPathMaker;
import core.mapper.IResultMapper;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обрабатывает файлы в каждой конкретной папке
 * @param <R> тип возвращаемых данных
 * @param <T> тип данных получаемых из обработчиков папок
 */
//Можно отрефакторить и вынести общий с CSVFilesDataProducer функционал отдельно.
//Но не стал тратить на это время.
public class FolderDataProducer<R,T> extends PartialDataProducer<R,Collection<PartialDataProducer<Collection<T>, ?>>> {

    //Показывают количество файлов обрабатываемых за один раз
    private static final Integer OFFSET=0;
    private static final Integer LIMIT=5;

    private final IProducerFactory<InputStream, PartialDataProducer<Collection<T>,?>> producerFactory;
    private final IInputStreamFactory<String> inputStreamFactory;
    private final IPathMaker<String> pathMaker;
    private final Folder folder;
    private final IResultMapper<R,Collection<T>> resultMapper;
    private  IForkJoinWorker<Collection<T>,Collection<T>> forkJoinWorker;

    private final Map<String, PartialDataProducer<Collection<T>, ?>> producersInWork = new HashMap<>();
    public FolderDataProducer(Integer offset, Integer limit,
                              IProducerFactory<InputStream, PartialDataProducer<Collection<T>, ?>> producerFactory,
                              IInputStreamFactory<String> inputStreamFactory, IPathMaker<String> pathMaker, Folder folder, IResultMapper<R, Collection<T>> resultMapper, IForkJoinWorker<Collection<T>, Collection<T>> forkJoinWorker) {
        super(offset,limit);
        this.producerFactory = producerFactory;
        this.inputStreamFactory = inputStreamFactory;
        this.pathMaker = pathMaker;
        this.folder = folder;
        this.resultMapper = resultMapper;
        this.forkJoinWorker = forkJoinWorker;
    }
    public FolderDataProducer(IProducerFactory<InputStream, PartialDataProducer<Collection<T>, ?>> producerFactory,
                              Folder folder,  IResultMapper<R,Collection<T>> resultMapper,IForkJoinWorker<Collection<T>, Collection<T>> forkJoinWorker, IInputStreamFactory<String> inputStreamFactory, IPathMaker<String> pathMaker) {
        this(OFFSET,LIMIT,producerFactory, inputStreamFactory, pathMaker, folder, resultMapper, forkJoinWorker);
    }

    @Override
    protected R rertrieve(Collection<PartialDataProducer<Collection<T>, ?>> data) {
        if (data.size()<=0)
        {
            complete();
            return null;
        }
        //Запускаем новую порцию обработчиков
        data.forEach((producer)->
        {
            //Обрабатываем каждый файл
            forkJoinWorker.submitTask(producer::produce);
        });
        //ждём завершения
        return resultMapper.map(forkJoinWorker.join());
    }

    @Override
    protected Collection<PartialDataProducer<Collection<T>, ?>> retrieveNext() {

        Collection<PartialDataProducer<Collection<T>, ?>> result;
        //Ищем незавершенные обработчики файлов
        result = producersInWork.entrySet().stream().filter((entry) ->
                !entry.getValue().isCompleted()).limit(getLimit()).map((Map.Entry::getValue)).collect(Collectors.toList());

        int count = getLimit()-result.size();
        if (this.folder.getFiles().size() > getOffset()) {
            //Если обработчиков недостаточно созаём новые для необработанных папок
            result.addAll(folder.getFiles().stream()
                    .filter((file) -> !producersInWork.containsKey(file)).limit(count)
                    .map((file) ->
                    {
                        PartialDataProducer<Collection<T>, ?> prod = this.crateProduer(file);
                        this.producersInWork.put(file, prod);
                        moveOffset(1);
                        return prod;
                    }).collect(Collectors.toList()));
        }
        return result;
    }
    private PartialDataProducer<Collection<T>, ?> crateProduer(String file)
    {
        PartialDataProducer<Collection<T>,?> producer=null;
        producer = producerFactory.create(inputStreamFactory.create(this.pathMaker.make(String.format("%s/%s",folder.getFolderName(), file))));
        return producer;
    }

}
