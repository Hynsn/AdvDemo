package com.hynson.codeorder;

public class A {
    private int hash = 0;
    public A() {
        hash = System.identityHashCode(this);
        System.out.println("A()"+System.identityHashCode(this)+'\n');
    }

    public void find(){
        System.out.println("A hash: "+hash+'\n');
    }
}
