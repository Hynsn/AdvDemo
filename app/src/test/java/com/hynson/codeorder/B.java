package com.hynson.codeorder;

public class B extends A{
    private int hash = 0;
    public B() {
        hash = System.identityHashCode(this);
        System.out.println("B()"+System.identityHashCode(this)+'\n');
    }
//    public void find(){
//        System.out.println("B hash:"+hash+'\n');
//    }
}
