package com.hynson.objpool;

public class CachePool {

    public CachePool() {
    }

    /**
     * 池接口
     * @param <T>
     */
    interface Pool<T>{
        T obtain();
        boolean recycle(T instance);
    }

    public static class SimpleCachePool<T> implements Pool<T> {

        private Object[] mPool;
        private int mPoolSize;

        public SimpleCachePool(int size) {
            if(size <= 0){
                throw new IllegalArgumentException("size < 0");
            }
            mPool = new Object[size];
        }

        @Override
        public T obtain() {
            if(mPoolSize > 0){
                int last = mPoolSize-1;
                T obj = (T)mPool[last];

                mPool[last] = null;
                mPoolSize--;
                return obj;
            }
            return null;
        }

        @Override
        public boolean recycle(T instance) {
            if(isExist(instance)){
                throw new IllegalStateException("please many call recycle");
            }
            if(mPoolSize < mPool.length){
                mPool[mPoolSize] = instance;

                mPoolSize++;
                return true;
            }
            return false;
        }

        private boolean isExist(T t){
            for (Object o : mPool) {
                if(o == t){
                    return true;
                }
            }
            return false;
        }
    }

    public static class SyncSimpleCachePool<T> extends SimpleCachePool<T> {
        private final Object mLock = new Object();
        public SyncSimpleCachePool(int size) {
            super(size);
        }
        @Override
        public T obtain() {
            synchronized (mLock) {
                return super.obtain();
            }
        }

        @Override
        public boolean recycle(T instance) {
            synchronized (mLock) {
                return super.recycle(instance);
            }
        }
    }
}
