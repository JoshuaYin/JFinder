package im.joshua.finder.finder.rule;

public abstract class Rule<T, R> {
    private String name;

    public Rule() {
        name = "rule";
    }

    public Rule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract boolean isSatisfyRule(T target);

    public abstract R runRule(T target);
}
