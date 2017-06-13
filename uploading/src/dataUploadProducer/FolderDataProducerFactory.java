package dataUploadProducer;

import core.data.CadastralEntry;
import core.dataProducer.CSVDataProducerFactory;
import core.dataProducer.IProducerFactory;
import core.dataProducer.PartialDataProducer;
import core.forkjoin.IForkJoinWorker;
import core.input.GzipInputStreamFactory;
import core.input.IInputStreamFactory;
import core.mapper.DefaultPathMaker;
import core.mapper.IPathMaker;
import core.mapper.IResultMapperFactory;
import core.mapper.ResultMapperFactory;
import data.FolderData;

import java.io.InputStream;
import java.util.Collection;
import java.util.function.Supplier;

public class FolderDataProducerFactory<T,R> implements IProducerFactory<Folder,PartialDataProducer<R,?>> {
    private final IProducerFactory<InputStream,PartialDataProducer<Collection<T>,?>> producerFactory;
    private final Supplier<IForkJoinWorker<Collection<T>,Collection<T>>> forkJoinSupplier;
    private final IInputStreamFactory<String> inputStreamFactory;
    private final IResultMapperFactory<R,Collection<T>,Folder> resultMapperFactory;
    private final IPathMaker<String> pathMaker;
    public FolderDataProducerFactory(IProducerFactory<InputStream, PartialDataProducer<Collection<T>, ?>> producerFactory,
                                     Supplier<IForkJoinWorker<Collection<T>, Collection<T>>> forkJoinSupplier, IInputStreamFactory<String> inputStreamFactory, IResultMapperFactory<R, Collection<T>, Folder> resultMapperFactory, IPathMaker<String> pathMaker) {
        this.producerFactory = producerFactory;
        this.forkJoinSupplier = forkJoinSupplier;
        this.inputStreamFactory = inputStreamFactory;
        this.resultMapperFactory = resultMapperFactory;
        this.pathMaker = pathMaker;
    }

    

    @Override
    public PartialDataProducer<R,?> create(Folder source) {
        return new FolderDataProducer<>(producerFactory,source, this.resultMapperFactory.create(source),forkJoinSupplier.get(), inputStreamFactory, pathMaker);
    }
}
