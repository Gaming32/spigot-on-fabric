package io.github.gaming32.spigotonfabric.impl.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class LazyHashSet<E> implements Set<E> {
    Set<E> reference = null;

    @Override
    public int size() {
        return getReference().size();
    }

    @Override
    public boolean isEmpty() {
        return getReference().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getReference().contains(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return getReference().iterator();
    }

    @Override
    public Object @NotNull [] toArray() {
        return getReference().toArray();
    }

    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
        return getReference().toArray(a);
    }

    @Override
    public boolean add(E o) {
        return getReference().add(o);
    }

    @Override
    public boolean remove(Object o) {
        return getReference().remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return getReference().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return getReference().addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return getReference().retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return getReference().removeAll(c);
    }

    @Override
    public void clear() {
        getReference().clear();
    }

    public Set<E> getReference() {
        Set<E> reference = this.reference;
        if (reference != null) {
            return reference;
        }
        return this.reference = makeReference();
    }

    abstract Set<E> makeReference();

    public boolean isLazy() {
        return reference == null;
    }

    @Override
    public int hashCode() {
        return 157 * getReference().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        LazyHashSet<?> that = (LazyHashSet<?>) obj;
        return (this.isLazy() && that.isLazy()) || this.getReference().equals(that.getReference());
    }

    @Override
    public String toString() {
        return getReference().toString();
    }
}
