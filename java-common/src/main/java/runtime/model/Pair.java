package runtime.model;

public class Pair<L,R> {
    public L key;
    public R value;

    public Pair(L l, R value){this.key =l;this.value = value;}
}