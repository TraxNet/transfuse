package org.androidtransfuse.processor;

/**
 * Wrapper class to run a ScopedTransactionWorker within a Transaction and Runnable.
 *
 * @author John Ericksen
 */
public class ScopedTransaction<T extends TransactionWorker<V, R>, V, R> implements Transaction<V, R> {

    private final ScopedTransactionWorker<T, V, R> worker;
    private final V value;
    private R result = null;
    private boolean complete = false;

    public ScopedTransaction(V value, ScopedTransactionWorker<T, V, R> worker) {
        this.value = value;
        this.worker = worker;
    }

    @Override
    public boolean isComplete() {
        return complete && worker != null && worker.isComplete();
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public R getResult() {
        return result;
    }

    @Override
    public Exception getError() {
        return worker == null ? null : worker.getError();
    }

    @Override
    public void run() {
        result = worker.runScoped(value);
        complete = true;
    }
}
