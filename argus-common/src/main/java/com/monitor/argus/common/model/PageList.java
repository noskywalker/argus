package com.monitor.argus.common.model;

import java.util.*;

/**
 * 用于分页，通过分页器和当前页数据
 *
 * @author aj
 *
 * @param <T>
 */
public class PageList<T extends Object> implements java.io.Serializable,
        Iterable<T> {

    private static final long serialVersionUID = -2369279401146592072L;

    private Paginator paginator;

    private ArrayList<T> _list;

    public PageList() {
        paginator = new Paginator();
        _list = new ArrayList<T>();
    }

    public PageList(Paginator paginator) {
        this.paginator = paginator;
        _list = new ArrayList<T>();
    }

    public PageList(int items, int pageSize, int pageNo) {
        Paginator paginator = new Paginator();
        paginator.setItems(items);
        paginator.setPage(pageNo);
        paginator.setItemsPerPage(pageSize);
        this.paginator = paginator;
        _list = new ArrayList<T>();
    }

    public PageList(Collection<T> c) {
        this(c, null);
    }

    public PageList(Collection<T> c, Paginator paginator) {
        this._list.addAll(c);
        this.paginator = (paginator == null) ? new Paginator() : paginator;
    }

    public boolean add(T e) {
        if (e != null && e instanceof Paginator) {
            setPaginator((Paginator) e);
        } else {
            this._list.add(e);
        }
        return true;
    }

    public int size() {
        return _list.size();
    }

    public boolean isEmpty() {
        return _list.size() == 0;
    }

    public boolean contains(Object o) {
        return _list.contains(o);
    }

    public int indexOf(Object o) {
        return _list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return _list.lastIndexOf(o);
    }

    @SuppressWarnings("rawtypes")
    public Object clone() {
        return ((ArrayList) _list).clone();
    }

    public Object[] toArray() {
        return _list.toArray();
    }

    public T get(int index) {
        return _list.get(index);
    }

    public T set(int index, T element) {
        return _list.set(index, element);
    }

    public void add(int index, T element) {
        _list.add(index, element);
    }

    public T remove(int index) {
        return _list.remove(index);
    }

    public boolean remove(Object o) {
        return _list.remove(o);
    }

    public void clear() {
        _list.clear();
    }

    public boolean addAll(Collection<? extends T> c) {
        return _list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return _list.addAll(index, c);
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public void setPaginator(Paginator paginator) {
        if (paginator != null) {
            this.paginator = paginator;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<T> {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;

        /**
         * Index of element returned by most recent call to next or previous.
         * Reset to -1 if this element is deleted by a call to remove.
         */
        int lastRet = -1;

        /**
         * The modCount value that the iterator believes that the backing List
         * should have. If this expectation is violated, the iterator has
         * detected concurrent modification.
         */
        int expectedModCount = _list.size();

        public boolean hasNext() {
            return cursor != size();
        }

        public T next() {
            checkForComodification();
            try {
                T next = get(cursor);
                lastRet = cursor++;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (lastRet == -1)
                throw new IllegalStateException();
            checkForComodification();

            try {
                PageList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = _list.size();
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (_list.size() != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    public boolean containsAll(Collection<?> c) {
        return _list.containsAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return _list.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return _list.retainAll(c);
    }

    public ListIterator<T> listIterator() {
        return _list.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return _list.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return _list.subList(fromIndex, toIndex);
    }

    public <E> E[] toArray(E[] a) {
        return _list.toArray(a);
    }

    public List<T> getList(){
        return _list;
    }

    public static void main(String[] args) {
        PageList<String> list = new PageList<String>();
        list.add("1");
        list.add("2");
        list.add("2");
        list.add("2");
        list.add("2");
        list.add("3");
        list.add("2");
        for (String a : list) {
            System.out.println(a);
        }
    }
}
