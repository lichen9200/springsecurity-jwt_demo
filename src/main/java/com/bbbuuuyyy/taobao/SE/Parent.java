package com.bbbuuuyyy.taobao.SE;

public class Parent {
     void m4(){
        System.out.println("我是爸爸！");
    }

}
class Son extends Parent{
    public  void m4(){
        System.out.println("我是儿子");
    }

    public static void main(String[] args) {
        Parent parent =  new Son();
        new Son().m4();

    }
}
