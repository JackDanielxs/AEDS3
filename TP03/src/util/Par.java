package util;

public final class Par<T,R> {
    private T primeiro;
    private R segundo;

    public Par(final T primeiro, final R segundo){
        this.primeiro = primeiro;
        this.segundo = segundo;
    }

    public T getFirst(){ return this.primeiro; }
    public R getSecond() { return this.segundo; }
}