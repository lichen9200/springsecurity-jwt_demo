package com.bbbuuuyyy.taobao;

public class Father {
    private String name;
    public String getName()
    {
        System.out.println("我是父亲。。。");
        return name;
    }

}
class SubSon extends Father {
    private String nickName;
    public String getNickName()
    {
        return nickName;
    }
//    @Override
//    public String getName()
//    {
//        System.out.println("我是儿子。。。");
//        return nickName;
//
//    }
/*
 1.多态只能调用父子类共有的public方法。
 2.动态绑定：运行时，被调用的是子类。
 */
    public static void main(String[] args) {
       Father father =  new SubSon();
       father.getName();
    }


}